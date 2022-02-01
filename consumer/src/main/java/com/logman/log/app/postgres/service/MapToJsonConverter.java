package com.logman.log.app.postgres.service;

import java.util.Map;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;

import com.logman.log.util.JsonUtil;

import lombok.extern.slf4j.Slf4j;

@WritingConverter
@Slf4j
public class MapToJsonConverter implements Converter<Map<String, Object>, String> {

	@Override
	public String convert(Map<String, Object> source) {
		return JsonUtil.writeValueAsString(source);
	}
}