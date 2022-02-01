package com.logman.log.producer;

import java.util.Date;
import java.util.UUID;

import org.springframework.stereotype.Component;

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
