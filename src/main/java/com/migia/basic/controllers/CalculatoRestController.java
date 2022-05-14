package com.migia.basic.controllers;

import java.util.*;
import java.util.stream.Collectors;

import com.migia.basic.models.Authority;
import com.migia.basic.models.User;
import com.migia.basic.repository.UserRepository;
import com.migia.basic.security.jwt.JwtUtils;
import com.migia.basic.service.EvaluateMathService;
import org.springframework.beans.factory.annotation.Autowired;
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

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class CalculatoRestController {

    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;
    @Autowired
    JwtUtils jwtUtils;

    @Autowired
    EvaluateMathService calculator;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getName(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User userDetails = (User) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getId(),
                        userDetails.getUsername(),
                        userDetails.getEmail()
                        ));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        System.out.println(signUpRequest.getUsername() +" " + signUpRequest.getPassword());
        if (userRepository.existsByName(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                Authority.USER);

        userRepository.save(user);
        System.out.println("User registered successfully!");
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @PostMapping("/calculate")
    public String calculate(@RequestBody MessageResponse expr){
        return String.valueOf(calculator.getResult(expr.getMessage()));
    }
}


