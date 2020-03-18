package com.stories.controller;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.stories.domain.StoryDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.service.StoriesServiceImpl;

import junit.framework.TestCase;

@RunWith(SpringRunner.class)
@WebMvcTest(StoriesController.class)
public class GetMethodTest extends TestCase {
	
	@MockBean
    private StoriesServiceImpl storiesServiceImpl;

    @Autowired
    private MockMvc mvcResult;
    
    @Test
    public void getAllValid() throws Exception {
        String uri = "/stories/";
        mvcResult.perform(MockMvcRequestBuilders.get(uri)).andExpect(status().isOk());
    }
    
    @Test
    public void getByIdValid() throws Exception {
    	String uri = "/stories/5e7134c9099a9a0ab248c90b";
    	mvcResult.perform(MockMvcRequestBuilders.get(uri)
    			.contentType("5e7134c9099a9a0ab248c90b")).andExpect(status().isOk());
    }
    
    @Test(expected = EntityNotFoundException.class)
    public void getByIdInvalid() throws Exception {
    	String uri = "/stories/5e6a8441bf#ERFSasda";
    	mvcResult.perform(MockMvcRequestBuilders.get(uri))
    	.andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult mvcResult) throws Exception {
                throw new EntityNotFoundException("Story not found", StoryDomain.class);
            }
        }).andExpect(status().isNotFound());
    }
}
