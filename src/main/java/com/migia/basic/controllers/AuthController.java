package com.migia.basic.controllers;

import com.migia.basic.models.Authority;
import com.migia.basic.models.User;
import com.migia.basic.repository.UserRepository;
import com.migia.basic.security.jwt.JwtUtils;
import com.migia.basic.service.EvaluateMathService;
import com.migia.basic.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin(origins = "http://localhost:3000",allowedHeaders = "*",allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    final static Logger logger = LoggerFactory.getLogger(AuthController.class);

    AuthenticationManager authenticationManager;

    UserRepository userRepository;

    PasswordEncoder encoder;

    JwtUtils jwtUtils;


    UserService userService;
    @Autowired
    EvaluateMathService calculator;



    public AuthController(AuthenticationManager authenticationManager,
                                   UserRepository userRepository,
                                   PasswordEncoder encoder,
                                   JwtUtils jwtUtils,
                                   UserService userService)
    {
        logger.info("Hello instantiating Auth controller now...");
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.userService = userService;
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getName(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        User userDetails = (User) authentication.getPrincipal();
        String jwt = jwtUtils.generateJwtCookie(userDetails);
        List<String> roles = userDetails.getAuthorities().stream()
                .map(item -> item.getAuthority())
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .body(new MessageResponse(jwt));
    }
    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        System.out.println(signUpRequest.getUsername() +" " + signUpRequest.getPassword());
    /*    if (userRepository.existsByName(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }
        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }*/
        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                Authority.USER);

        /*userRepository.save(user);*/
        userService.saveUser(user);
        System.out.println("User registered successfully!");
        Authentication authentication = new UsernamePasswordAuthenticationToken(user,null,user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));

    }
    @GetMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        String cleanJwt = jwtUtils.getCleanJwt();
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, cleanJwt)
                .body(new MessageResponse("You've been signed out!"));
    }
}
