package com.stories.domain;

import java.util.ArrayList;
import java.util.Date;

import lombok.Data;

@Data
public class StoryDomain {

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