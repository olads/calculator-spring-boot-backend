package com.migia.basic.service;

import com.migia.basic.models.Authority;
import com.migia.basic.models.History;
import com.migia.basic.models.User;
import com.migia.basic.repository.HistoryRepository;
import com.migia.basic.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Qualifier("userService")
public class UserService implements UserDetailsService {
    UserRepository repository;
    PasswordEncoder passwordEncoder;
    HistoryRepository historyRepository;

    public UserService(UserRepository repository, PasswordEncoder encoder, HistoryRepository historyRepository){
        this.repository = repository;
        this.passwordEncoder = encoder;
        this.historyRepository = historyRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = repository.findByName(username);
       if(user != null){
           return user;
       }
       else{
           System.out.println("User with username " + username + " Is not found" );
           throw new UsernameNotFoundException("Username not found ");
       }
    }

    public void saveUser(String name,String email,String password){
        User user = new User(name,email,passwordEncoder.encode(password), Authority.USER);
        if(repository.findByName(name) == null)
        repository.save(user);
    }

    public void saveUser(User user){
        if(repository.findByName(user.getName()) == null){
            repository.save(user);
        }
    }

    public String addHistory(History history){
        historyRepository.save(history);
        return "Saved";
    }
}
