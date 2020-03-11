package com.stories.mapper;

import com.stories.domain.StoryDomain;

import ma.glasnost.orika.MapperFactory;
import net.rakugakibox.spring.boot.orika.OrikaMapperFactoryConfigurer;

public class StoriesMapping implements OrikaMapperFactoryConfigurer {
	@Override
	public void configure(MapperFactory orikaMapperFactory) {
		orikaMapperFactory.classMap(StoryDomain.class, StoryDomain.class).byDefault().register();
	}
}
