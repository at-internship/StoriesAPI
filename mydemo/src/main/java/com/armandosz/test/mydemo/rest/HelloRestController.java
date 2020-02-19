package com.armandosz.test.mydemo.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloRestController {

	@GetMapping("/")
	public String sayHello() {
		return "Hello World, this is my first springboot proyect.";
	}
}
