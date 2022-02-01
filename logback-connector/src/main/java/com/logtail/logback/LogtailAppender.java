package com.logtail.logback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;

import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.spi.IThrowableProxy;
import ch.qos.logback.classic.spi.StackTraceElementProxy;
import ch.qos.logback.classic.spi.ThrowableProxy;
import ch.qos.logback.core.UnsynchronizedAppenderBase;

/**
 * Logback appender for sending logs to kafka </a>.
 * 
 * @author tomas@logtail.com
 */
public class LogtailAppender extends UnsynchronizedAppenderBase<ILoggingEvent> {

	private static final String CUSTOM_USER_AGENT = "Logtail Logback Appender";

	private final Logger errorLog = LoggerFactory.getLogger(LogtailAppender.class);

	private final ObjectMapper dataMapper;

	private Client client;

	private boolean disabled;

	protected final MultivaluedMap<String, Object> headers;

	// Assignable fields

	protected PatternLayoutEncoder encoder;

	protected String appName;

	protected String url = "https://localhost:8079";

	protected List<String> mdcFields = new ArrayList<>();

	protected List<String> mdcTypes = new ArrayList<>();

	protected long connectTimeout = 5000;

	protected long readTimeout = 10000;

	/**
	 * Appender initialization.
	 */
	public LogtailAppender() {
		this.headers = new MultivaluedHashMap<>();
		this.headers.add("User-Agent", CUSTOM_USER_AGENT);
		this.headers.add("Accept", MediaType.APPLICATION_JSON);
		this.headers.add("Content-Type", MediaType.APPLICATION_JSON);

		this.dataMapper = new ObjectMapper();
		this.dataMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
		this.dataMapper.setPropertyNamingStrategy(PropertyNamingStrategies.UPPER_CAMEL_CASE);
	}

	// Postpone client initialization to allow timeouts configuration
	protected Client client() {
		if (this.client == null) {
			this.client = ClientBuilder.newBuilder() //
					.connectTimeout(this.connectTimeout, TimeUnit.MILLISECONDS) //
					.readTimeout(this.readTimeout, TimeUnit.MILLISECONDS) //
					.build();
		}

		return this.client;
	}

	/**
	 * @see ch.qos.logback.core.UnsynchronizedAppenderBase#append(java.lang.Object)
	 */
	@Override
	protected void append(ILoggingEvent event) {

		if (disabled) {
			return;
		}

		if (event.getLoggerName().equals(LogtailAppender.class.getName())) {
			return;
		}

		try {
			String jsonData = convertLogEventToJson(event);

			Response response = callIngestApi(jsonData);

			if (response.getStatus() != 202) {
				LogtailResponse logtailResponse = convertResponseToObject(response);
				errorLog.error("Error calling Logtail : {} ({})", logtailResponse.getError(), response.getStatus());
			}

		} catch (JsonProcessingException e) {
			errorLog.error("Error processing JSON data : {}", e.getMessage());

		} catch (Exception e) {
			errorLog.error("Error trying to call Logtail : {}", e.getMessage());
		}

	}

	protected String convertLogEventToJson(ILoggingEvent event) throws JsonProcessingException {
		return this.dataMapper.writeValueAsString(buildPostData(event));
	}

	protected LogtailResponse convertResponseToObject(Response response) throws JsonProcessingException {
		return new LogtailResponse(response.readEntity(String.class), response.getStatus());
	}

	/**
	 * Call Logtail API posting given JSON formated string.
	 * 
	 * @param jsonData a json oriented map
	 * @return the http response
	 */

	protected Response callIngestApi(String jsonData) {
		WebTarget wt = client().target(url);

		return wt.request().headers(headers).post(Entity.json(jsonData));
	}

	/**
	 * Converts a logback logging event to a JSON oriented array.
	 * 
	 * @param event the logging event
	 * @return a json oriented array
	 */
	protected ArrayList<Object> buildPostData(ILoggingEvent event) {
		Map<String, Object> line = new HashMap<>();

		line.put("dt", Long.toString(event.getTimeStamp()));
		line.put("level", event.getLevel().toString());
		line.put("app", this.appName);
		line.put("message",
				this.encoder != null ? new String(this.encoder.encode(event)) : event.getFormattedMessage());

		Map<String, Object> meta = new HashMap<>();
		meta.put("logger", event.getLoggerName());

		if (!mdcFields.isEmpty() && !event.getMDCPropertyMap().isEmpty()) {
			for (Entry<String, String> entry : event.getMDCPropertyMap().entrySet()) {
				if (mdcFields.contains(entry.getKey())) {
					String type = mdcTypes.get(mdcFields.indexOf(entry.getKey()));
					meta.put(entry.getKey(), getMetaValue(type, entry.getValue()));
				}
			}
		}
		line.put("meta", meta);

		Map<String, Object> runtime = new HashMap<>();
		runtime.put("thread", event.getThreadName());

		if (event.hasCallerData()) {
			handleStackElement(event.getCallerData(), runtime);
		}
		line.put("runtime", runtime);
		//
		IThrowableProxy throwableProxy = event.getThrowableProxy();
		if (throwableProxy != null) {
			if (throwableProxy instanceof ThrowableProxy) {
				ThrowableProxy tp = (ThrowableProxy) throwableProxy;
				String className = tp.getClassName();
				Throwable throwable = tp.getThrowable();
				if (event.hasCallerData()) {
					handleStackElement(throwable.getStackTrace(), runtime);
				}
				line.put("message", tp.getMessage());
			}
		}

		ArrayList<Object> lines = new ArrayList<>();
		lines.add(line);

		return lines;
	}

	private void handleStackElement(StackTraceElement[] stackTraceElements, Map<String, Object> runtime) {
		StackTraceElement[] callerData = stackTraceElements;

		if (callerData.length > 0) {
			StackTraceElement callerContext = callerData[0];

			runtime.put("class", callerContext.getClassName());
			runtime.put("method", callerContext.getMethodName());
			runtime.put("file", callerContext.getFileName());
			runtime.put("line", callerContext.getLineNumber());
		}
	}

	private Object getMetaValue(String type, String value) {
		try {
			if ("int".equals(type)) {
				return Integer.valueOf(value);
			}
			if ("long".equals(type)) {
				return Long.valueOf(value);
			}
			if ("boolean".equals(type)) {
				return Boolean.valueOf(value);
			}
		} catch (NumberFormatException e) {
			errorLog.warn("Error getting meta value : {}", e.getMessage());
		}
		return value;

	}

	public void setEncoder(PatternLayoutEncoder encoder) {
		this.encoder = encoder;
	}

	/**
	 * Sets the application name for Logtail indexation.
	 * 
	 * @param appName application name
	 */
	public void setAppName(String appName) {
		this.appName = appName;
	}

	/**
	 * Sets the Logtail ingest API url.
	 * 
	 * @param ingestUrl Logtail url
	 */
	public void setUrl(String ingestUrl) {
		this.url = ingestUrl;
	}

	/**
	 * Sets the MDC fields that needs to be sent inside Logtail metadata, separated
	 * by a comma.
	 * 
	 * @param mdcFields MDC fields to use
	 */
	public void setMdcFields(String mdcFields) {
		this.mdcFields = Arrays.stream(mdcFields.split(",")).map(String::trim).collect(Collectors.toList());
	}

	/**
	 * Sets the MDC fields types that will be sent inside Logtail metadata, in the
	 * same order as <i>mdcFields</i> are set up, separated by a comma. Possible
	 * values are <i>string</i>, <i>boolean</i>, <i>int</i> and <i>long</i>.
	 * 
	 * @param mdcTypes MDC fields types
	 */
	public void setMdcTypes(String mdcTypes) {
		this.mdcTypes = Arrays.stream(mdcTypes.split(",")).map(String::trim).collect(Collectors.toList());
	}

	/**
	 * Sets the connection timeout of the underlying HTTP client, in milliseconds.
	 * 
	 * @param connectTimeout client connection timeout
	 */
	public void setConnectTimeout(Long connectTimeout) {
		this.connectTimeout = connectTimeout;
	}

	/**
	 * Sets the read timeout of the underlying HTTP client, in milliseconds.
	 * 
	 * @param readTimeout client read timeout
	 */
	public void setReadTimeout(Long readTimeout) {
		this.readTimeout = readTimeout;
	}

	public boolean isDisabled() {
		return this.disabled;
	}
}
