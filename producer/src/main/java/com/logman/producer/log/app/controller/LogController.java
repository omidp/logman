package com.logman.producer.log.app.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.logman.producer.log.app.service.LogService;

import reactor.core.publisher.Mono;

@Validated
@RestController
@RequestMapping("/logs")
public class LogController {

	private static final Logger LOG = LoggerFactory.getLogger(LogController.class);

	private final LogService logService;

	public LogController(LogService logService) {
		this.logService = logService;
	}

	@ResponseStatus(HttpStatus.ACCEPTED)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public Mono<Void> produceEvents(
			@RequestBody  List<com.logman.producer.log.model.LogRequest> request) {
		logService.processLogs(request)
				.doOnSubscribe(subscription -> LOG.info("==== Received request -> {}", request)).subscribe(null,
						throwable -> LOG.error("=== Failed to process request " + request, throwable),
						() -> LOG.info("===== Process Ended for request -> {}", request));

		return Mono.empty();
	}
}