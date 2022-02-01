package com.logman.consumer.log.app.stream;

import java.util.List;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import com.logman.consumer.log.app.config.LogmanConsumerConfiguration;
import com.logman.consumer.log.app.postgres.service.LogStoreServiceFactory;
import com.logman.consumer.log.exception.ConsumerExcpetion;
import com.logman.consumer.log.model.event.LogModel;

import lombok.extern.slf4j.Slf4j;

/**
 * @author omidp
 *
 */
@Component
@EnableBinding(LogEventDataInput.class)
@Slf4j
public class LogEventConsumer {

	private LogStoreServiceFactory logStoreServiceFactory;
	private List<PayloadReader> readers;
	private LogmanConsumerConfiguration config;

	public LogEventConsumer(LogStoreServiceFactory logStoreServiceFactory, List<PayloadReader> readers,
			LogmanConsumerConfiguration config) {
		this.logStoreServiceFactory = logStoreServiceFactory;
		this.readers = readers;
		this.config = config;
	}

	@StreamListener(LogEventDataInput.INPUT)
	public void process(Message<?> message) {
		MessageHeaders headers = message.getHeaders();
		String contentType = "" + headers.getOrDefault("contentType", "application/json");
		Object payload = message.getPayload();
		log.info("Payload {}", payload);
		PayloadReader c = readers.stream().filter(f -> f.support(contentType, payload)).findFirst()
				.orElseThrow(() -> new ConsumerExcpetion("no reader found for " + contentType));
		LogModel event = c.read(payload);
		log.info("eventmodel {}", event.toString());
		logStoreServiceFactory.getInstance(config.getType()).save(event, headers);
	}

}
