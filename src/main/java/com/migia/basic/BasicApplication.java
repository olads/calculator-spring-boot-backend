package com.migia.basic;

import com.migia.basic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;

@SpringBootApplication
public class BasicApplication {

	@Autowired
	UserService service;

	public static void main(String[] args) {
		SpringApplication.run(BasicApplication.class, args);

	}

	@PostConstruct
	public void postConstruct(){
		service.saveUser("stephen","steve@gmail.com","migia");
	}


}
