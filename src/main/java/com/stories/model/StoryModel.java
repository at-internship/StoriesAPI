package com.stories.model;

import java.util.ArrayList;
import java.util.Date;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;


@Data
@Document(collection = "stories")
public class StoryModel {
	@Id
    private ObjectId _id;
	private String sprint_id;
    private String technology;
    private String name;
    private String description;
    private String acceptance_criteria;
    private int points;
    private int progress;
    private String status;
    private String notes;
    private String comments;
    private Date start_date;
    private Date due_date;
    private String priority;
    private String assignee_id;
    private ArrayList history;
     
    
}
