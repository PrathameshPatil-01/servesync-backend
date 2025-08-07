package com.servesync;

import org.modelmapper.Condition;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
	    info = @Info(
	        title = "ServeSync API",
	        version = "1.0",
	        description = "API documentation for ServeSync project"
	    )
	)
@SpringBootApplication
public class ServesyncBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(ServesyncBackendApplication.class, args);
	}

	/*
	 * Configure ModelMapper as a spring bean , so that
	 *  - SC manages its life cycle
	 *  - It can be injected as a dependency in any other spring bean
	 *
	 * NOTE: While ModelMapper is configured here, for DTO-Entity mapping,
	 * MapStruct is preferred for performance and compile-time safety.
	 * ModelMapper might still be used for other general object mappings.
	 */
	@Bean
	ModelMapper modelMapper() {
		System.out.println("creating model mapper");
		Condition<?, ?> condition = ctx -> {
		    Object sourceValue = ctx.getSource();
		    if (sourceValue == null) return false;
		    if (sourceValue instanceof Number) return ((Number) sourceValue).doubleValue() != 0.0;
		    if (sourceValue instanceof Boolean) return (Boolean) sourceValue;
		    if (sourceValue instanceof Character) return (Character) sourceValue != '\u0000';
		    return true;
		};
		ModelMapper mapper= new ModelMapper();
		mapper.getConfiguration()
		.setMatchingStrategy(MatchingStrategies.STRICT)
		.setPropertyCondition(Conditions.isNotNull())
		.setPropertyCondition(condition);
		return mapper;
	}
	//configure PasswordEncoder as spring bean
	@Bean
	PasswordEncoder passwordEncoder()
	{
		return new BCryptPasswordEncoder();
	}
}
