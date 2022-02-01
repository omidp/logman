package com.logman.log.app.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@ConfigurationProperties(prefix = LogmanConsumerConfiguration.PREFIX)
@Data
//@AllArgsConstructor
@NoArgsConstructor
public class LogmanConsumerConfiguration
{

    public static final String PREFIX = "logman";
    
    
    
 
    
}
