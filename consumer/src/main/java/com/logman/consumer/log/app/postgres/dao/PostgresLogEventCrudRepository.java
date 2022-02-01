package com.logman.consumer.log.app.postgres.dao;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.logman.consumer.log.app.postgres.domain.LogEvent;

public interface PostgresLogEventCrudRepository extends ReactiveCrudRepository<LogEvent, String> {

}