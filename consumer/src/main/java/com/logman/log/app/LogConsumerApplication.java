package com.logman.log.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;

import com.logman.log.app.config.LogmanConsumerConfiguration;
import com.logman.log.app.stream.consumer.LogEventDataInput;

/**
 * @author omidp
 *
 */
@SpringBootApplication
@EnableConfigurationProperties(LogmanConsumerConfiguration.class)
public class LogConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(LogConsumerApplication.class, args);
	}

}
