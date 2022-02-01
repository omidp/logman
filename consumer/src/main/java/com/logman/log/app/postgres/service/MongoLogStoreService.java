package com.logman.log.app.postgres.service;


import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import com.logman.log.app.model.event.Log;
import com.logman.log.app.mongo.dao.MongoLogEventCrudRepository;
import com.logman.log.app.mongo.domain.LogEvent;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author omidp
 *
 */
@Service
@Slf4j
public class MongoLogStoreService {

	MongoLogEventCrudRepository eventDao;

	

	public MongoLogStoreService(MongoLogEventCrudRepository eventDao) {
		this.eventDao = eventDao;
	}



	public Mono<Void> save(Log event, MessageHeaders headers) {
		LogEvent ee = new LogEvent();
		ee.setId(event.getId());
		ee.setMessage(event.getMessage());
		ee.setApp(event.getApp());
		ee.setClzName(event.getClzName());
		ee.setFileName(event.getFileName());
		ee.setLevel(event.getLevel());
		ee.setLine(event.getLine());
		ee.setMethod(event.getMethod());
		ee.setThreadName(event.getThreadName());
		//
		if(event.getMeta() != null)
		{
			ee.setTraceId(""+event.getMeta().get("traceId"));
			ee.setSpanId(""+event.getMeta().get("spanId"));
			ee.setLogger(""+event.getMeta().get("logger"));
			ee.setUserId(""+event.getMeta().get("userId"));
			ee.setMeta(event.getMeta());
		}
		eventDao.save(ee).doOnSuccess((it) -> log.info("event saved {}", it.getId())).subscribe();
		return Mono.empty();
	}

}
