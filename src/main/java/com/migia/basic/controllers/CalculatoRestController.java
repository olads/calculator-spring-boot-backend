package com.migia.basic.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.migia.basic.models.Authority;
import com.migia.basic.models.History;
import com.migia.basic.models.User;
import com.migia.basic.repository.UserRepository;
import com.migia.basic.security.jwt.JwtUtils;
import com.migia.basic.service.EvaluateMathService;
import com.migia.basic.service.HistoryService;
import com.migia.basic.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins="http://localhost:3000",allowedHeaders = "*",allowCredentials = "true")
@RestController
@RequestMapping("/calculator")
public class CalculatoRestController {

    @Autowired
    EvaluateMathService calculator;

    @Autowired
    HistoryService historyService;

    @PostMapping("/calculate")
    public String calculate(@RequestBody MessageResponse expr){
        double value = calculator.getResult(expr.getMessage());
         User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
         History history = new History(expr.getMessage(),value);
        // System.out.println(user.getHistories().toString());
        historyService.saveHistory(history,user);
        return String.valueOf(value);
    }

  @PostMapping("/addhistory")
    public String addHistory(@RequestBody History history){
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        historyService.saveHistory(history,user);
       return "Works";
  }

  @GetMapping("/history")
    public List<History> getHistory(){
      User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
      return historyService.retrieveHistories(user.getId());

  }
}


