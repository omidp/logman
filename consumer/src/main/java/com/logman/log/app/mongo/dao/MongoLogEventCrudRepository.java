package com.logman.log.app.mongo.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.logman.log.app.postgres.domain.LogEvent;

@Repository
public interface MongoLogEventCrudRepository extends ReactiveCrudRepository<LogEvent, String> {

}