package com.logman.log.producer;

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
	
	private String ts;
	
	private String app;
	
	private String level;
	
	Map<String, Object> meta = new HashMap<>();
	
	Map<String, Object> runtime = new HashMap<>();
	
}
