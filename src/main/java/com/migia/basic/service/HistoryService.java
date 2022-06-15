package com.migia.basic.service;

import com.migia.basic.models.History;
import com.migia.basic.models.User;
import com.migia.basic.repository.HistoryRepository;
import com.migia.basic.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class HistoryService {


    private final static Logger logger = LoggerFactory.getLogger(HistoryService.class);
    HistoryRepository historyRepository;
    UserRepository userRepository;

    public HistoryService(HistoryRepository historyRepository, UserRepository userRepository){
        this.historyRepository = historyRepository;
        this.userRepository = userRepository;
    }

    public List<History> retrieveHistories(long userId){
       return historyRepository.findByUserId(userId);
    }

    public void saveHistory(History history){
        historyRepository.save(history);
    }

    public void saveHistory(History history, long userId){
        Optional<User> user = userRepository.findById(userId);
        limitHistories(userId);
        if(user.isPresent()){
            history.setUser(user.get());
            saveHistory(history);
        }
        else{
            logger.error("User with id " + String.valueOf(userId) + " Not found");
        }
    }

    public void saveHistory(History history, User user){
        saveHistory(history,user.getId());
    }

    public boolean limitHistories(long userId){
        logger.info("User id is " + userId);
        List<History> histories = historyRepository.findByUserId(userId);
        if(histories.size() >= 5){
            logger.info(histories.get(0).toString());
            historyRepository.delete(histories.remove(0));

            return true;
        }
        return false;
    }

public void deleteHistories(long userId){
        historyRepository.deleteAllByUserId(userId);
    }
    public void deleteHistories(User user){
        deleteHistories(user.getId());
    }
}
