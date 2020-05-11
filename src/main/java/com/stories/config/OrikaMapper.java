package com.stories.config;

import org.springframework.context.annotation.Configuration;

import com.stories.domain.StoryDomain;
import com.stories.model.StoryModel;

import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;

@Configuration
public class OrikaMapper extends ConfigurableMapper {

	public MapperFactory mapper(MapperFactory factory) {
		factory.classMap(StoryModel.class, StoryDomain.class).byDefault().register();
		return factory;
	}

}