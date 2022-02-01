package com.logman.log.app.postgres.service;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.stereotype.Component;

import com.logman.log.app.exception.ConsumerExcpetion;

@Component
public class LogStoreServiceFactory {

	private Map<LogStoreType, LogStoreService> map = new EnumMap<>(LogStoreType.class);
	
	private List<LogStoreService> logStores;

	public LogStoreServiceFactory(List<LogStoreService> logStores) {
		this.logStores = logStores;
	}
	
	@PostConstruct
	public void init()
	{
		logStores.forEach(i->map.put(i.type(), i));
	}
	
	
	public LogStoreService getInstance(LogStoreType type)
	{
		LogStoreService logStoreService = map.get(type);
		if(logStoreService == null)
			throw new ConsumerExcpetion("logstore not found for type " + type.name());
		return logStoreService;
	}
	
	
	
}
