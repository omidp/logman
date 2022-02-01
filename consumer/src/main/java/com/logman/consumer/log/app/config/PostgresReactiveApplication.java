package com.logman.consumer.log.app.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.convert.CustomConversions.StoreConversions;
import org.springframework.data.r2dbc.convert.R2dbcCustomConversions;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.r2dbc.connection.init.ConnectionFactoryInitializer;
import org.springframework.r2dbc.connection.init.ResourceDatabasePopulator;

import com.logman.consumer.log.app.postgres.dao.PostgresLogEventCrudRepository;
import com.logman.consumer.log.app.postgres.domain.LogEvent;
import com.logman.consumer.log.app.postgres.service.JsonToMapConverter;
import com.logman.consumer.log.app.postgres.service.MapToJsonConverter;

import io.r2dbc.spi.ConnectionFactory;

@Configuration
@EntityScan(basePackageClasses = LogEvent.class)
@EnableR2dbcRepositories(basePackageClasses = PostgresLogEventCrudRepository.class)
@ConditionalOnProperty(prefix = LogmanConsumerConfiguration.PREFIX + ".postgres", name = "enabled", havingValue = "true")
public class PostgresReactiveApplication {

	@Bean
	ConnectionFactoryInitializer initializer(ConnectionFactory connectionFactory) {

		var initializer = new ConnectionFactoryInitializer();
		initializer.setConnectionFactory(connectionFactory);
		initializer.setDatabasePopulator(new ResourceDatabasePopulator(new ClassPathResource("schema.sql")));

		return initializer;
	}

	@Bean
	public R2dbcCustomConversions r2dbcCustomConversions() {
		List<Converter<?, ?>> converters = new ArrayList<>();
		converters.add(new JsonToMapConverter());
		converters.add(new MapToJsonConverter());
		return new R2dbcCustomConversions(converters);
	}

}