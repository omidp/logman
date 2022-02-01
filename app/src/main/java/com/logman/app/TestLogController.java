package com.logman.app;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/log")
@Slf4j
public class TestLogController {

	
	
	@PostMapping
	public ResponseEntity<String> send()
	{
		log.info("This is a test log sending to kafka producer");
		
		return ResponseEntity.ok().build();
	}
	
}
