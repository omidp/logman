package com.logman.log.app.config;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import com.logman.log.app.mongo.dao.MongoLogEventCrudRepository;
import com.logman.log.app.mongo.domain.LogEvent;
import com.mongodb.reactivestreams.client.MongoClient;

@Configuration
@EnableReactiveMongoRepositories(basePackageClasses = MongoLogEventCrudRepository.class)
@EntityScan(basePackageClasses = LogEvent.class)
@ConditionalOnProperty(prefix = LogmanConsumerConfiguration.PREFIX + ".mongo", name = "enabled", havingValue = "true")
public class MongoReactiveApplication {

	@Bean
	public ReactiveMongoTemplate reactiveMongoTemplate(MongoClient mongoClient) {
		return new ReactiveMongoTemplate(mongoClient, "logdb");
	}
	
	@Bean
	ApplicationRunner ar(ReactiveMongoTemplate rt)
	{
		return args -> {
			
		};
	}

}