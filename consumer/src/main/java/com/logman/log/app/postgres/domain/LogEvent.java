package com.logman.log.app.postgres.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import com.logman.log.app.model.event.Log;

import lombok.Data;

@Data
@Table
public class LogEvent implements Log {

	@Id
	private Long logEventId;

	private String message;

	private String id;

	private String app;

	private String level;

	private String fileName;

	private String method;

	private long line;

	private String threadName;

	private String clzName;
	
	private String traceId;
	
	private String spanId;
	
	private String userId;
	
	private String logger;
	
	private Date ts;

	private Map<String, Object> meta = new HashMap<>();

}