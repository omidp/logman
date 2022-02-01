package com.logman.log.app.model.event;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class LogModel implements Log {

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