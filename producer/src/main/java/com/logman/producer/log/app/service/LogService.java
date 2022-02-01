package com.logman.producer.log.app.service;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.logman.producer.log.app.stream.LogProducer;
import com.logman.producer.log.model.Log;
import com.logman.producer.log.model.LogRequest;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.ParallelFlux;
import reactor.core.scheduler.Schedulers;

@Service
public class LogService {

	private static final Logger LOG = LoggerFactory.getLogger(LogService.class);

	private final LogProducer logProducer;
	private final LogMapper logMapper;

	public LogService(LogProducer eventProducer, LogMapper eventMapper) {
		this.logProducer = eventProducer;
		this.logMapper = eventMapper;
	}

	public ParallelFlux<Void> processLogs(List<LogRequest> events) {
		return Flux.fromIterable(events)
		.doFirst(() -> LOG.debug("=== Starting event generator. -> {} ", LocalDateTime.now()))
        .parallel(events.size())
        .runOn(Schedulers.boundedElastic())
        .flatMap(event -> sendLog(logMapper.fromRequest(event)))
        .doOnComplete(() -> LOG.info("==== Event generation ended {}", LocalDateTime.now()));
		
	}

	protected Mono<Void> sendLog(Log event) {
		return logProducer.sendEvent(event).doFirst(() -> LOG.debug("==== Sending event {}", event));
	}
}
