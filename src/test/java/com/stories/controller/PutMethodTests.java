package com.stories.controller;

import com.stories.domain.StoryDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.service.StoriesServiceImpl;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultHandler;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = StoriesController.class)
public class PutMethodTests {

    @MockBean
    private StoriesServiceImpl storiesServiceImpl;

    @Autowired
    private MockMvc mvcResult;

    @Test
    public void putTestTrue() throws Exception {
        String uri = "/stories/5e7133b6430bf4151ec1e85f";
        mvcResult.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .content(setStoryInJsonFormat("5e7133b6430bf4151ec1e85f"))).andExpect(status().isAccepted());
    }

    @Test(expected = EntityNotFoundException.class)
    public void putTestInvelidId() throws Exception {
        String uri = "/stories/5e6a8441bfc6533811235e1";
        mvcResult.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .content(setStoryInJsonFormat("5e6a8441bfc6533811235e1"))).andDo(new ResultHandler() {
            @Override
            public void handle(MvcResult mvcResult) throws Exception {
                throw new EntityNotFoundException("Story not found", StoryDomain.class);
            }
        }).andExpect(status().isNotFound());
    }

    @Test
    public void putTestInvalidJson() throws Exception {
        String uri = "/stories/5e6a8441bfc6533811235e19";
        mvcResult.perform(MockMvcRequestBuilders.put(uri).contentType(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON).characterEncoding("UTF-8")
                .content(setStoryInJsonBadFormat("5e6a8441bfc6533811235e19"))).andExpect(status().isBadRequest());
    }

    private String setStoryInJsonFormat(String id) {
        return "{\"id\":\"" + id + "\", \"sprint_id\":\"UUID\", \"technology\":\"Java\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":2,\"progress\":885, \"status\":\"Working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
    }

    private String setStoryInJsonBadFormat(String id) {
        return "{\"id\":\"" + id + "\", \"sprint_id\":\"UUID\", \"technology\":\"Java\",\"name\":\"Create Stories POST endpoint\", \"description\":\"\",\"acceptance_criteria\":\"\",\"points\":\"2#\",\"progress\":885, \"status\":\"Working\",\"notes\":\"\",\"comments\":\"Test\", \"start_date\":\"2020-08-25\",\"due_date\":\"2020-08-25\",\"priority\":\"High\", \"assignee_id\":\"UUID\",\"history\":[\"\",\"\"]}";
    }

}