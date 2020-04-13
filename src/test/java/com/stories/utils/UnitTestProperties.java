package com.stories.utils;

import java.time.LocalDate;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import lombok.Data;

@PropertySource("classpath:unittest.properties")
@ConfigurationProperties(prefix = "unittest")
@Data
@Component
public class UnitTestProperties {

	protected String urlId;
	protected String sprintClientUri;
	
	protected String domainId;
	protected String domainSprintId;
	protected String domainTechnology;
	protected String domainName;
	protected String domainDescription;
	protected String domainAcceptanceCriteria;
	protected int domainPoints;
	protected int domainProgress;
	protected String domainStatus;
	protected String domainNotes;
	protected String domainComment;
	protected LocalDate domainStartDate = LocalDate.now();
	protected LocalDate domainDueDate = LocalDate.now();
	protected String domainPriority;
	protected String domainAssigneeId;
	protected String domainHistory1;
	protected String domainHistory2;
	
	protected String TasksdomainName;
	protected String TasksdomainDescription;
	protected String TasksdomainStatus;
	protected String TasksdomainComments;
	protected String TasksdomainAssignee;
	
	protected String TasksModelId;
	protected String TasksModelName;
	protected String TasksModelDescription;
	protected String TasksModelStatus;
	protected String TasksModelComments;
	protected String TasksModelAssignee;

	protected String modelId;
	protected String modelSprintid;
	protected String modelTechnology;
	protected String modelName;
	protected String modelDescription;
	protected String modelAcceptanceCriteria;
	protected int modelPoints;
	protected int modelProgress;
	protected String modelStatus;
	protected String modelNotes;
	protected String modelComments;
	protected LocalDate modelStartDate = LocalDate.now();
	protected LocalDate modelDueDate = LocalDate.now();
	protected String modelPriority;
	protected String modelAssigneeId;
	protected String modelHistory1;
	protected String modelHistory2;
	
	protected String sprintClientId;
	protected String sprintClientName;
	protected String sprintClientTechnology;
	protected boolean sprintClientActive;
	protected boolean sprintClientIsBacklog;
	protected LocalDate sprintClientStartDate = LocalDate.now(); 
	protected LocalDate sprintClientEndDate = LocalDate.now(); 

}
