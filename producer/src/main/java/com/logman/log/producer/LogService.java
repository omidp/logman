package com.logman.log.producer;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

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

    public ParallelFlux<Void> processAllEvents(List<LogRequest> events) {
    	
        return Flux.fromIterable(events)
                .doFirst(() -> LOG.debug("=== Starting event generator. -> {} ", LocalDateTime.now()))
                .parallel(events.size())
                .runOn(Schedulers.boundedElastic())
                .flatMap(e->processEvent(logMapper.fromRequest(e), e.getTotal(), e.getHeartBeat()))
                .doOnComplete(() -> LOG.info("==== Event generation ended {}", LocalDateTime.now()));
    }

    public Flux<Void> processEvent(Log event, Integer total, Integer heartBeat) {
        return Flux.range(0, total)
                .doFirst(() -> LOG.debug("==== Going to process event single event -> {}", event))
                .delayElements(Duration.ofSeconds(heartBeat))
                .flatMap(t -> sendLog(event))
                .doOnComplete(() -> LOG.debug("==== Going to process event single event -> {}", event));
    }

    protected Mono<Void> sendLog(Log event) {
        return logProducer.sendEvent(event)
                .doFirst(() -> LOG.debug("==== Sending event {}", event));
    }
}
