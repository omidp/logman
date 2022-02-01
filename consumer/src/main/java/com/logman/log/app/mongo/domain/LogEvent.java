package com.logman.log.app.mongo.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.logman.log.app.model.event.Log;

import lombok.Data;

@Document(collection = "logs")
@Data
public class LogEvent implements Log {

	@Id
	private String uid;

	private String message;

	private String id;

	private String app;

	private String level;

	private String fileName;

	private String method;

	private long line;

	private String threadName;

	private String clzName;
	
	private String logger;
	
	private String spanId;
	
	private String traceId;
	
//	private String userId;
//	
//	private String hostname;
//	
//	private String ip;
	
	private Date ts;
	
	private Date createdDate;
	
	Map<String, Object> meta = new HashMap<>();

}
