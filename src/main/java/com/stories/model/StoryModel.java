package com.stories.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Document(collection = "stories")
public class StoryModel {
	
	@Id
    private String _id;
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
    private LocalDate start_date;
    private LocalDate due_date;
    private String priority;
    private String assignee_id;
    private List<String> history;
    
}
