package com.logman.producer.log.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LogEvent implements Log {

	private String message;
	
	private String id;
	
	private String app;
	
	private String level;
	
	Map<String, Object> meta = new HashMap<>();
	
	private String fileName;
	
	private String method;
	
	private long line;
	
	private String threadName;
	
	private String clzName;
	
	private Date ts;

}