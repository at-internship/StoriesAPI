package com.stories.model;

import java.io.Serializable;
import java.util.UUID;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.stereotype.Service;

@Document(collation = "stories")
@Service
public class Story_task implements Serializable {
	
	
	@Id
	private int sprint_id;
//	" : UUID,
	private String technology;
	private String name;
	private String description;
	private String acceptance_criteria;
	private int points;
	private int progress;
	private String status;
	private String notes;
	private String comments;
	private String start_date;
//	" : Date,
	private String due_date;
//	: Date,
	private String priority;
	private int assignee_id;
//	: UUID,
	private String history;
	
	
	
	public Story_task() {
		
		}
	
	public Story_task(UUID id, int sprint_id, String technology, String name, String description, String acceptance_criteria,
		int points, int progress, String status, String notes, String comments, String start_date, String due_date,
		String priority, int assignee_id, String history) {
			super();
			this.sprint_id = sprint_id;
			this.technology = technology;
			this.name = name;
			this.description = description;
			this.acceptance_criteria = acceptance_criteria;
			this.points = points;
			this.progress = progress;
			this.status = status;
			this.notes = notes;
			this.comments = comments;
			this.start_date = start_date;
			this.due_date = due_date;
			this.priority = priority;
			this.assignee_id = assignee_id;
			this.history = history;
	}
	public UUID getId(UUID id) {
		return id;
	}
	public void setId(UUID id) {
	}
	public int getSprint_id() {
		return sprint_id;
	}
	public void setSprint_id(int sprint_id) {
		this.sprint_id = sprint_id;
	}
	public String getTechnology() {
		return technology;
	}
	public void setTechnology(String technology) {
		this.technology = technology;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getAcceptance_criteria() {
		return acceptance_criteria;
	}
	public void setAcceptance_criteria(String acceptance_criteria) {
		this.acceptance_criteria = acceptance_criteria;
	}
	public int getPoints() {
		return points;
	}
	public void setPoints(int points) {
		this.points = points;
	}
	public int getProgress() {
		return progress;
	}
	public void setProgress(int progress) {
		this.progress = progress;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getComments() {
		return comments;
	}
	public void setComments(String comments) {
		this.comments = comments;
	}
	public String getStart_date() {
		return start_date;
	}
	public void setStart_date(String start_date) {
		this.start_date = start_date;
	}
	public String getDue_date() {
		return due_date;
	}
	public void setDue_date(String due_date) {
		this.due_date = due_date;
	}
	public String getPriority() {
		return priority;
	}
	public void setPriority(String priority) {
		this.priority = priority;
	}
	public int getAssignee_id() {
		return assignee_id;
	}
	public void setAssignee_id(int assignee_id) {
		this.assignee_id = assignee_id;
	}
	public String getHistory() {
		return history;
	}
	public void setHistory(String history) {
		this.history = history;
	}

//	public Object FindById(long id2) {
//		// TODO Auto-generated method stub
//		return null;
//	}
	
	
	
	
	
	
//	"id" : UUID,
//	"sprint_id" : UUID,
//	"technology" : String,
//	"name" : String,
//	"description" : String,
//	"acceptance_criteria" : String,
//	"points" : Numeric,
//	"progress" : Numeric,
//	"status" : String,
//	"notes" : String,
//	"comments" : String,
//	"start_date" : Date,
//	"due_date" : Date,
//	"priority" : String,
//	"assignee_id" : UUID,
//	"history" : [String]
	
//	_id:5e6290031c9d44000033734d
//	sprint_id:"UUID"
//	technology:"Java"
//	name:"Create Stories POST end point"
//	description:""
//	acceptance_criteria:""
//	points:2
//	progress:1
//	status:"Working"
//	notes:""
//	comments:"Test"
//	start_date:2020-03-06T06:00:00.000+00:00
//	due_date:2020-04-06T06:00:00.000+00:00
//	priority:"High"
//	assignee_id:"UUID"
//	history array
	
}
