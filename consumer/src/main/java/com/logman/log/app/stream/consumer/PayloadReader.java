package com.logman.log.app.stream.consumer;

import com.logman.log.app.model.event.LogModel;

/**
 * @author omidp
 *
 */
public interface PayloadReader {

	public boolean support(String contentType, Object payload);
	
	public LogModel read(Object payload);
	
}
