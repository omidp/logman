package com.logman.consumer.log.app.stream;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

/**
 * @author omidp
 *
 */
public interface LogEventDataInput {

	/**
	 * Input channel name.
	 */
	String INPUT = "log-data";

	/**
	 * @return input channel.
	 */
	@Input(LogEventDataInput.INPUT)
	SubscribableChannel input();

}