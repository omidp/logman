package com.logman.log.app.postgres.service;

import java.util.Map;

import com.logman.log.app.model.event.Log;

import reactor.core.publisher.Mono;

public interface LogStoreService {

	public Mono<Void> save(Log event, Map<String, Object> headers);
	
	
	public LogStoreType type();

}
