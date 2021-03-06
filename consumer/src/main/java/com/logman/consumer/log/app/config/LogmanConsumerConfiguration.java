package com.logman.consumer.log.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.logman.consumer.log.app.postgres.service.LogStoreType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = LogmanConsumerConfiguration.PREFIX)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LogmanConsumerConfiguration
{

    public static final String PREFIX = "logman";
    
    
    public LogStoreType type;
 
    
}
