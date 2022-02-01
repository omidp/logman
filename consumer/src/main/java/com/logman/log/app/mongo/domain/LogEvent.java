package com.logman.log.app.mongo.domain;

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
	
	Map<String, Object> meta = new HashMap<>();

}