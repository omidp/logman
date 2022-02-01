package com.logman.consumer.log.app.postgres.service;


import java.util.Date;
import java.util.Map;

import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import com.logman.consumer.log.app.mongo.dao.MongoLogEventCrudRepository;
import com.logman.consumer.log.app.mongo.domain.LogEvent;
import com.logman.consumer.log.model.event.Log;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * @author omidp
 *
 */
@Service
@Slf4j
public class MongoLogStoreService implements LogStoreService{

	MongoLogEventCrudRepository eventDao;

	

	public MongoLogStoreService(MongoLogEventCrudRepository eventDao) {
		this.eventDao = eventDao;
	}



	public Mono<Void> save(Log event, Map<String, Object> headers) {
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
		ee.setTs(event.getTs());
		ee.setCreatedDate(new Date());
		//
		if(event.getMeta() != null)
		{
			ee.setTraceId(""+event.getMeta().get("traceId"));
			ee.setSpanId(""+event.getMeta().get("spanId"));
			ee.setLogger(""+event.getMeta().get("logger"));
//			ee.setUserId(""+event.getMeta().get("userId"));
			ee.setMeta(event.getMeta());
		}
		eventDao.save(ee).doOnSuccess((it) -> log.info("event saved {}", it.getId())).subscribe();
		return Mono.empty();
	}



	@Override
	public LogStoreType type() {
		return LogStoreType.MONGO;
	}

}
