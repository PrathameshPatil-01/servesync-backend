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
	 *  
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
		//to transfer only properties matching by name 
		mapper.getConfiguration()
		.setMatchingStrategy(MatchingStrategies.STRICT)
		//transfer not null props
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
