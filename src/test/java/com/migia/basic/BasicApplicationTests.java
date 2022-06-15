package com.migia.basic;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.migia.basic.controllers.CalculatoRestController;
import com.migia.basic.controllers.LoginRequest;
import com.migia.basic.controllers.MessageResponse;
import com.migia.basic.controllers.SignupRequest;
import com.migia.basic.models.History;
import com.migia.basic.service.EvaluateMathService;
import org.junit.FixMethodOrder;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import javax.servlet.http.Cookie;
import java.io.IOException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
//@FixMethodOrder(MethodSorters.NAME_ASCENDING)
class BasicApplicationTests {
	final static Logger logger = LoggerFactory.getLogger(BasicApplicationTests.class);
	@Autowired
	EvaluateMathService math;

	@Autowired
	MockMvc mockMvc;

	String token = "calculatoreyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJtaWdpYSIsImlhdCI6MTY1NDQ1NjI5NCwiZXhwIjoxNjU0NTQyNjk0fQ.WmjSCJPTdCk3eGl19jxHu0wXf3PkazJWBaL0sP7z3TPRhINtRL669Yl_SigFWkzQRhAdxtkECdK8MX4e-29Wmw";

		@Test
	void signinTest() throws Exception{
		LoginRequest signIn = new LoginRequest("migia","stephen");
		ObjectMapper objectMapper = new ObjectMapper();
		String json = objectMapper.writeValueAsString(signIn);
		logger.info("In the signn test function");
		MvcResult returnValue = null;
		if(true) {
			returnValue = mockMvc.perform(post("/api/auth/signin").content(json)
					.contentType(MediaType.APPLICATION_JSON))
					.andExpect(status().isOk())
					.andReturn();
		}

		logger.info(String.valueOf(returnValue));
		logger.info(returnValue.getResponse().getContentAsString());
	}
	@Test
	void zcalculateTest() throws Exception {
	
		MessageResponse expr = new MessageResponse("3*5");
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(expr);
			logger.info("In the calculateTest function now ....");
			var result = mockMvc.perform(post("/calculator/calculate")
					.contentType(MediaType.APPLICATION_JSON)
					.content(json)
					.header("Authorization",token))
					.andExpect(status().isOk())
					.andExpect(content().string("15.0"))
			.andReturn();
			logger.info(result.getResponse().getContentAsString());

	}


    @Test
	public void saveHistoryTest() throws Exception {
		History hist = new History("20*3",60.0);
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(hist);
		logger.warn(json);
		mockMvc.perform(post("/calculator/addhistory")
		.contentType(MediaType.APPLICATION_JSON)
		.content(json).header("Authorization",token)
		)
				.andExpect(status().isOk())
				;
	}

	@Test
	public void getHistoryTest() throws Exception{
		MvcResult result = mockMvc.perform(get("/calculator/history")
				.header("Authorization",token))
				.andExpect(status().isOk())
				.andReturn();
		logger.info("Content type is " + result.getResponse().getContentType());
		logger.info(result.getResponse().getContentAsString());


	}


}
