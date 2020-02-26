package com.stories.API;	

import org.bson.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
		
		 MongoClientURI connectionString = new MongoClientURI("mongodb+srv://db_user:vloUr7AakeTEdXDj@cluster0-nomfh.mongodb.net/test?retryWrites=true&w=majority");
		 @SuppressWarnings("resource")
		 MongoClient mongoClient = new MongoClient(connectionString);
		 
	}

}
