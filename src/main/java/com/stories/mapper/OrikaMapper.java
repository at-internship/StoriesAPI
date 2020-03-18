package com.stories.mapper;

import com.stories.domain.StoryDomain;
import com.stories.model.StoryModel;
import ma.glasnost.orika.MapperFactory;
import ma.glasnost.orika.impl.ConfigurableMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrikaMapper extends ConfigurableMapper {

	public MapperFactory mapper(MapperFactory factory) {
		factory.classMap(StoryModel.class, StoryDomain.class).byDefault().register();
		return factory;
	}

}