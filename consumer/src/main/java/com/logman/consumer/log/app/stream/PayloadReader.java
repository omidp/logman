package com.logman.consumer.log.app.stream;

import com.logman.consumer.log.model.event.LogModel;

/**
 * @author omidp
 *
 */
public interface PayloadReader {

	public boolean support(String contentType, Object payload);
	
	public LogModel read(Object payload);
	
}
