package com.logman.producer.log.model;

import java.io.Serializable;
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
public class LogRequest implements Serializable {

	private String message;
	
	/**
	 * Date time
	 */
	private String dt;
	
	private String app;
	
	private String level;
	
	Map<String, Object> meta = new HashMap<>();
	
	Map<String, Object> runtime = new HashMap<>();
	
}
