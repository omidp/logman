package com.logman.consumer.log.app.mongo.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.logman.consumer.log.app.mongo.domain.LogEvent;

@Repository
public interface MongoLogEventCrudRepository extends ReactiveCrudRepository<LogEvent, String> {

}