package com.sts.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;


@Configuration
@Slf4j
public class ClassConfig {
	
	@Bean 
	ModelMapper modelMapper() {
		return new ModelMapper();
	}
	
}
