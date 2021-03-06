package com.logman.consumer.log.model.event;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public interface Log extends Serializable {

	String getMessage();

	String getId();

	String getApp();

	String getLevel();
	
	String getLogger();

	Map<String, Object> getMeta();

	String getFileName();

	String getMethod();

	long getLine();

	String getThreadName();

	String getClzName();
	
	Date getTs();
}