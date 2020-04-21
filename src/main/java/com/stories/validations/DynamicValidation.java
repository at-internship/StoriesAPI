package com.stories.validations;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.stories.domain.StoryDomain;
import com.stories.domain.TasksDomain;
import com.stories.exception.EntityNotFoundException;
import com.stories.model.StoryModel;
import com.stories.repository.StoriesRepository;
import com.stories.repository.UsersRepository;
import com.stories.sprintsclient.SprintsClient;

import io.micrometer.core.instrument.util.StringUtils;

@Component
public class DynamicValidation { 
	
	@Autowired
	SprintsClient sprintClient;
	
	@Autowired
	UsersRepository usersRepository;
	
	@Autowired
	StoriesRepository storiesRepository;
	
	String[] statusArray = { "Refining", "Ready to Work", "Working", "Testing", "Ready to Accept", "Accepted" };
	String[] storyDomainValidation = { "Story_id", "Sprint_id", "Status", "Priority", "Assignee_id" };
	String[] taskDomainValidation = { "Story_id", "Task_id", "Status", "assignee" };
	int[] pointsArray = { 0, 1, 2, 3, 5 };
	String[] specialCharacters = {"?","!","(",")","$","&","/","@","^",">","<","|","#",";",":","[","]","{","}","+","*","¨","¿","-","=","%",",","'",".","_","°","~"};
	
	public DynamicValidationArray storyValidation(StoryDomain storyDomain, String storyId) {
		DynamicValidationArray messageDynamicValidation = new DynamicValidationArray();
        String validationRespons = "";
        String[] validationPath = { "/Sprints/", "/StoryDomain/", "/Users/", "/stories/" };
        int countValidationPositive = 0;

        List<String> domainList = new ArrayList<>();
        domainList.add(storyId);
        domainList.add(storyDomain.getSprint_id());
        domainList.add(storyDomain.getStatus());
        domainList.add(storyDomain.getPriority());
        domainList.add(storyDomain.getAssignee_id());
       
        validationRespons = nameStatusNullValidation(storyDomain.getName(), storyDomain.getStatus());
        if (!StringUtils.isEmpty(validationRespons)) {
        	messageDynamicValidation.setMessage(messageDynamicValidation.getMessage() + validationRespons);
        	messageDynamicValidation.setPath("/stories/");
        	messageDynamicValidation.setStatus("BAD_REQUEST");
            return messageDynamicValidation;
        }
       
        validationRespons = "";
        for(int i= 0; i < domainList.size(); i++) {
	      	if(specialCharacterValidation(domainList.get(i))) {
	      		countValidationPositive++;
	      		if (StringUtils.isEmpty(validationRespons)) {
	      			validationRespons = storyDomainValidation[i];
	            }
	      		else {
	      			validationRespons = validationRespons + ", " + storyDomainValidation[i];
	      		}
	      	}
        }
        if(countValidationPositive > 1) {
  			messageDynamicValidation.setMessage("the following fields have special characters: " + validationRespons);
  		}
  		else if(countValidationPositive == 1){
  			messageDynamicValidation.setMessage("the next field have special characters: " + validationRespons);
  		}

        if(!StringUtils.isEmpty(messageDynamicValidation.getMessage())) {
        	messageDynamicValidation.setPath("/stories/");
        	messageDynamicValidation.setStatus("BAD_REQUEST");
        	return messageDynamicValidation;
        }
        
        if((!StringUtils.isEmpty(storyDomain.getSprint_id())) || (!StringUtils.isEmpty(storyDomain.getAssignee_id()))) {
            validationRespons = sprintNullValidation(storyDomain.getSprint_id(), sprintClient);
            if (!StringUtils.isEmpty(validationRespons)) {
            	messageDynamicValidation.setMessage(messageDynamicValidation.getMessage() + validationRespons);
            	messageDynamicValidation.setPath("/stories/");
                
            }
            
            validationRespons = userNullValidation(storyDomain.getAssignee_id(), usersRepository);
            if (!StringUtils.isEmpty(validationRespons)) {
            	messageDynamicValidation.setMessage(messageDynamicValidation.getMessage() + validationRespons);
            	messageDynamicValidation.setPath("/stories/");
            }
            
            messageDynamicValidation.setPath(filtervalidation(validationPath, messageDynamicValidation.getPath()));
            messageDynamicValidation.setStatus("CONFLICT");
            if(!StringUtils.isEmpty(messageDynamicValidation.getMessage())) {
                return messageDynamicValidation;    
            }
        }

        if((!StringUtils.isEmpty(storyDomain.getStatus())) || !(StringUtils.isEmpty(storyDomain.getProgress()+"")) || !(StringUtils.isEmpty(storyDomain.getPoints()+""))) {
            validationRespons = pointsValidation(storyDomain.getPoints(), pointsArray);
            if (!StringUtils.isEmpty(validationRespons)) {
            	messageDynamicValidation.setMessage(messageDynamicValidation.getMessage() + validationRespons);
            	messageDynamicValidation.setPath("/stories/");
            }

            validationRespons = proggressValidation(storyDomain.getProgress());
            if (!StringUtils.isEmpty(validationRespons)) {
            	messageDynamicValidation.setMessage(messageDynamicValidation.getMessage() + validationRespons);
            	messageDynamicValidation.setPath("/stories/");
            }

            validationRespons = statusValidation(statusArray, storyDomain.getStatus());
            if (!StringUtils.isEmpty(validationRespons)) {
            	messageDynamicValidation.setMessage(messageDynamicValidation.getMessage() + validationRespons);
            	messageDynamicValidation.setPath("/stories/");
            }
            
            messageDynamicValidation.setPath(filtervalidation(validationPath, messageDynamicValidation.getPath()));
            messageDynamicValidation.setStatus("BAD_REQUEST");
            if(!StringUtils.isEmpty(messageDynamicValidation.getMessage())) {
                return messageDynamicValidation;
            }
        }
        return messageDynamicValidation;
	}
	
	public DynamicValidationArray taskValidation(TasksDomain task, String storyId, String taskId) {
		DynamicValidationArray messageDynamicValidation = new DynamicValidationArray();
		String validationRespons = "";
        String[] validationPath = { "/Sprints/", "/StoryDomain/", "/Users/", "/stories/" };
        int countValidationPositive = 0;

        List<String> domainList = new ArrayList<>();
        domainList.add(storyId);
        domainList.add(taskId);
        domainList.add(task.getStatus());
        domainList.add(task.getAssignee());
        
        validationRespons = nameStatusNullValidation(task.getName(),".");
        if (!StringUtils.isEmpty(validationRespons)) {
         	messageDynamicValidation.setMessage(messageDynamicValidation.getMessage() + validationRespons);
         	messageDynamicValidation.setPath("/stories/");
         	messageDynamicValidation.setStatus("BAD_REQUEST");
            return messageDynamicValidation;
        }
        
        validationRespons = "";
        for(int i= 0; i < domainList.size(); i++) {
	      	if(specialCharacterValidation(domainList.get(i))) {
	      		countValidationPositive++;
	      		if (StringUtils.isEmpty(validationRespons)) {
	      			validationRespons = taskDomainValidation[i];
	            }
	      		else {
	      			validationRespons = validationRespons + ", " + taskDomainValidation[i];
	      		}
	      	}
        }
        if(countValidationPositive > 1) {
  			messageDynamicValidation.setMessage("The following fields have special characters: " + validationRespons);
  		}
  		else if(countValidationPositive == 1){
  			messageDynamicValidation.setMessage("The next field have special characters: " + validationRespons);
  		}

        if(!ObjectUtils.isEmpty(messageDynamicValidation.getMessage())) {
        	messageDynamicValidation.setPath("/stories/");
         	messageDynamicValidation.setStatus("BAD_REQUEST");
        	return messageDynamicValidation;
        }
        
        if((!StringUtils.isEmpty(task.getAssignee()))) {    
            validationRespons = userNullValidation(task.getAssignee(), usersRepository);
            if (!StringUtils.isEmpty(validationRespons)) {
              	messageDynamicValidation.setMessage(messageDynamicValidation.getMessage() + validationRespons);
             	messageDynamicValidation.setPath("/stories/");
            }
            
            messageDynamicValidation.setPath(filtervalidation(validationPath, messageDynamicValidation.getPath()));
            messageDynamicValidation.setStatus("CONFLICT");
            if(!ObjectUtils.isEmpty(messageDynamicValidation.getMessage())) {
                return messageDynamicValidation;    
            }
        }

        if(!StringUtils.isEmpty(task.getStatus())) {
            validationRespons = statusValidation(statusArray, task.getStatus());
            if (!StringUtils.isEmpty(validationRespons)) {
              	messageDynamicValidation.setMessage(messageDynamicValidation.getMessage() + validationRespons);
              	messageDynamicValidation.setPath("/stories/");
            }
            
            messageDynamicValidation.setPath(filtervalidation(validationPath, messageDynamicValidation.getPath()));
            messageDynamicValidation.setStatus("BAD_REQUEST");
            if(!ObjectUtils.isEmpty(messageDynamicValidation.getMessage())) {
                return messageDynamicValidation;
            }
        }
        return messageDynamicValidation;
	}
	
	public DynamicValidationArray idValidation(String storyId, String taskId) {

		DynamicValidationArray messageDynamicValidation = new DynamicValidationArray();
        String validationRespons = "";
        int countValidationPositive = 0;

        List<String> domainList = new ArrayList<>();
        domainList.add(storyId);
        domainList.add(taskId);
        
        validationRespons = "";
        for(int i= 0; i < domainList.size(); i++) {
	      	if(specialCharacterValidation(domainList.get(i))) {
	      		countValidationPositive++;
	      		if (StringUtils.isEmpty(validationRespons)) {
	      			validationRespons = storyDomainValidation[i];
	            }
	      		else {
	      			validationRespons = validationRespons + ", " + storyDomainValidation[i];
	      		}
	      	}
        }
        if(countValidationPositive > 1) {
          	messageDynamicValidation.setMessage("The following fields have special characters: " + validationRespons);
  		}
  		else if(countValidationPositive == 1){
  			messageDynamicValidation.setMessage("the next field have special characters: " + validationRespons);
  		}

        if(!ObjectUtils.isEmpty(messageDynamicValidation.getMessage())) {
            messageDynamicValidation.setPath("/stories/");
            messageDynamicValidation.setStatus("BAD_REQUEST");
        	return messageDynamicValidation;
        }
        return messageDynamicValidation;
	}
	
	private boolean specialCharacterValidation(String string) {
		for (int i = 0; i < specialCharacters.length; i++) {
           if(!StringUtils.isEmpty(string)) {
        	   if (string.toString().indexOf(specialCharacters[i]) == -1) {
               	
               } else {
               	return true;
               }
           }
        }
		return false;
	}

	private String filtervalidation(String[] validationPath, String string) {
        String validationRespons = "";
        for (int i = 0; i < validationPath.length; i++) {
            if (string.toString().indexOf(validationPath[i]) == -1) {

            } else {
                if (validationPath[i].equals(string.toString().substring(
                        string.toString().indexOf(validationPath[i]),
                        string.toString().indexOf(validationPath[i])
                                + validationPath[i].length()))) {
                    if (!(string.toString().indexOf(validationPath[i]) == -1)) {
                        if (StringUtils.isEmpty(validationRespons)) {
                            validationRespons = validationPath[i];
                        }
                    }
                }
            }
        }
        return validationRespons;
    }
	
	private String nameStatusNullValidation(String name, String status) {
		String validationNameStatus = "";

		if (StringUtils.isEmpty(name) && StringUtils.isEmpty(status)) {
			validationNameStatus = "The JSON format provided is invalid, please provide the required fields ('Name','Status').";
		} else if (StringUtils.isEmpty(name)) {
			validationNameStatus = "The JSON format provided is invalid, please provide the required field ('Name').";
		} else if (StringUtils.isEmpty(status)) {
			validationNameStatus = "The JSON format provided is invalid, please provide the required field ('Status').";
		}

		if (!StringUtils.isEmpty(validationNameStatus)) {
			return validationNameStatus;
		} else {
			return validationNameStatus;
		}
	}
	
	private String sprintNullValidation(String sprintId, SprintsClient sprintClient) {
		String validation = "";
		if (StringUtils.isEmpty(sprintId)) {

		} else {
			if (!sprintClient.existsSprintById(sprintId))
				validation = "The id entered in the sprint_id field does not exist";
		}
		if (StringUtils.isEmpty(validation)) {
			return validation;
		} else {
			return validation + ", ";
		}
	}
	
	private String userNullValidation(String assigneeId, UsersRepository usersRepository) {
		String validation = "";
		if (StringUtils.isEmpty(assigneeId)) {

		} else {
			if (!usersRepository.existsById(assigneeId))
				validation = "User assignee_id does not exist";
			return validation;
		}
		return validation;
	}
	
	private String pointsValidation(int points, int[] pointsArray) {
		String pointsValidation = "";
		if (points < 0) {
			pointsValidation = "The number entered in the points field is a negative number";
		} else {
			for (int i = 0; i < pointsArray.length; i++) {
				if (points == pointsArray[i]) {
					break;
				} else if (i == pointsArray.length - 1) {
					pointsValidation = "The number entered in the points field does not match a valid story point";
				}
			}
		}
		if (StringUtils.isEmpty(pointsValidation)) {
			return pointsValidation;
		} else {
			return pointsValidation +  ", ";
		}
	}
	
	private String proggressValidation(int progress) {
		String progressValidation = "";
		if (progress < 0) {
			progressValidation = "The number entered in the progress field is a negative number";
		}

		if (progress > 100) {
			progressValidation = "The number entered in the progress field exceeds 100%";
		}

		if (StringUtils.isEmpty(progressValidation)) {
			return progressValidation;
		} else {
			return progressValidation+  ", ";
		}
	}

	private String statusValidation(String[] statusArray, String storyStatus) {
		String validationStatus = "";
		if (!(Arrays.asList(statusArray).contains(storyStatus))) {
			validationStatus = "The Status field should be one of the following options: 'Refining' ,'Ready to Work', 'Working', 'Testing', 'Ready to Accept' or 'Accepted'.";
		}
		if (StringUtils.isEmpty(validationStatus)) {
			return validationStatus;
		} else {
			return validationStatus + ", ";
		}
	}
	
	public StoryModel nameValidation(StoryModel storyModel) throws EntityNotFoundException {
		try {
			storiesRepository.save(storyModel);
			return storyModel;
		} catch (Exception e) {
			throw new EntityNotFoundException("There is a story with this name already.", "", "/stories/");
		}
	}
	
	public LocalDate startDate(LocalDate start_date) {
		if ((!(start_date == null || (StringUtils.isEmpty(start_date.toString()))))) {
			return start_date;
		} else {
			return LocalDate.now();
		}
	}
	
	public LocalDate dueDate(LocalDate due_date) {
		if ((!(due_date == null || (StringUtils.isEmpty(due_date.toString()))))) {
			return due_date;
		} else {
			return LocalDate.now();
		}
	}
}
