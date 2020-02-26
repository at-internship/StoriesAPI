package com.stories.mapper;

import com.stories.domain.StoryDomain;
import com.stories.model.StoryModel;

import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;

public class StoryMapping implements OrikaMapperFactoryConfigurer{
	
	@Override
	public void configure(MapperFactory orikaMapperFactory) {
		orikaMapperFactory.classMap(StoryDomain.class, StoryModel.class).byDefault().register();
	}

}
