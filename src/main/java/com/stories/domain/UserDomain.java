package com.stories.domain;

import lombok.Data;

@Data
public class UserDomain {
	
	private String userId;
	private String firstName;
	private String lastName;
	private String email;
	private String phone;
	private String startDate;
	private String endDate;
	private String activeTechnology;
	private String status;
	private String role;
	
}
