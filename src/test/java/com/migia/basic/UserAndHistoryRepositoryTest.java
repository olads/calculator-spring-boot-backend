package com.migia.basic;


import com.migia.basic.models.Authority;
import com.migia.basic.models.History;
import com.migia.basic.models.User;
import com.migia.basic.repository.HistoryRepository;
import com.migia.basic.repository.UserRepository;
import com.migia.basic.service.HistoryService;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserAndHistoryRepositoryTest {
    private final static Logger logger = LoggerFactory.getLogger(UserAndHistoryRepositoryTest.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    HistoryRepository historyRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    HistoryService historyService;

  /*  public UserAndHistoryRepositoryTest(){
        User user  = new User("migia","migia@gmail.com",passwordEncoder.encode("stephen"), Authority.USER);
    }*/


    @Test
    public void testUserRepository(){
       User user  = new User("migia",
                "migia@gmail.com",
                passwordEncoder.encode("stephen"),
                Authority.USER);
        userRepository.save(user);
        var result = userRepository.findByName("migia");
        logger.info(result.toString());
        assertTrue(result != null);
    }
    @Test
    public void testHistoryRepository(){
       //assertNotEquals(userRepository,null);
       User user = userRepository.findByName("migia");
        History history = new History("2*5",10.0);
        history.setUser(user);
        historyRepository.save(history);
        var historyFound = historyRepository.findAll();
        assertTrue(historyFound.size() > 0);
    }

    @Test
    public void testGetHistoriesById(){
       List<History> histories=  historyRepository.findByUserId((long) 2);
       for(History hist : histories)
           logger.info(hist.toString());
       assertTrue(histories.size() > 0);
    }
    @Test
    public void testGetHistoriesByName(){
        List<History> histories=  historyRepository.findByUserName("migia");
        for(History hist : histories)
            logger.info(hist.toString());
        assertTrue(histories.size() > 0);
    }

    @Test
    public void createHistory(){
      // userRepository.deleteById((long) 4);
       logger.warn(userRepository.findByName("migia").toString());
    }

    @Test
    public void testHistoryService(){
        User user = userRepository.findByName("migia");
        historyService.saveHistory(new History("2*3",6.0),user);
        historyService.saveHistory(new History("2*4",8.0),user);
        /*historyService.saveHistory(new History("2*5",10.0),user);
        historyService.saveHistory(new History("2*6",12.0),user);
        historyService.saveHistory(new History("2*7",14.0),user);
        historyService.saveHistory(new History("2*8",16.0),user);
        historyService.saveHistory(new History("2*9",18.0),user);*/
        logger.info("============================== Histories ==============================");
        List<History> histories=  historyRepository.findByUserName("migia");
        for(History hist : histories)
            logger.info(hist.toString());
        assertTrue(histories.size() == 5);

    }

    @Test
    public void deleteRepository(){
        historyRepository.deleteAllByUserId((long) 2);
    }
}
