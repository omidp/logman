package com.logman.producer.log.app.service;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

import com.logman.producer.log.model.Log;
import com.logman.producer.log.model.LogEvent;
import com.logman.producer.log.model.LogRequest;

@Component
public class LogMapper {

	public Log fromRequest(LogRequest event) {
		LogEvent le = new LogEvent();
		le.setMessage(event.getMessage());
		le.setId(UUID.randomUUID().toString());
		le.setApp(event.getApp());
		le.setLevel(event.getLevel());
		le.setTs(new Date(Long.parseLong(event.getDt())));
		//
		if(event.getRuntime() != null)
		{
			le.setClzName(""+event.getRuntime().get("class"));
			le.setFileName(""+event.getRuntime().get("file"));
			le.setLine(Long.parseLong(""+event.getRuntime().get("line")));
			le.setMethod(""+event.getRuntime().get("method"));
			le.setThreadName(""+event.getRuntime().get("thread"));
		}
		if(event.getMeta() != null)
		{
			le.setMeta(event.getMeta());
		}
		return le;
	}

}
