package com.logman.consumer.log.app.postgres.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;

import com.logman.log.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@ReadingConverter
@Slf4j
public class JsonToMapConverter implements Converter<String, Map<String, Object>> {
	@Override
	public Map<String, Object> convert(String json) {
		return JsonUtil.toObject(json, HashMap.class);
	}

}
