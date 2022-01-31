package com.logman.log.producer;

import java.util.UUID;

import org.springframework.stereotype.Component;

@Component
public class LogMapper {

	public Log fromRequest(LogRequest event) {
		LogEvent le = new LogEvent();
		le.setMessage(event.getMessage());
		le.setId(UUID.randomUUID().toString());
		return le;
	}

  

}
