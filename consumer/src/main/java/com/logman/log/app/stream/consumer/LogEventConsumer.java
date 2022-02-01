package com.logman.log.app.stream.consumer;

import java.util.List;

import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;

import com.logman.log.app.exception.ConsumerExcpetion;
import com.logman.log.app.model.event.LogModel;
import com.logman.log.app.postgres.service.LogStoreService;
import com.logman.log.app.postgres.service.MongoLogStoreService;

import lombok.extern.slf4j.Slf4j;

/**
 * @author omidp
 *
 */
@Component
@EnableBinding(LogEventDataInput.class)
@Slf4j
public class LogEventConsumer {

//	private LogStoreService eventService;
	private MongoLogStoreService eventService;
	private List<PayloadReader> readers;

	public LogEventConsumer(MongoLogStoreService eventService, List<PayloadReader> readers) {
		this.eventService = eventService;
		this.readers = readers;
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
		eventService.save(event, headers);
	}

}
