package com.migia.basic.repository;

import com.migia.basic.models.History;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History,Integer> {
    List<History> findByUserId(long id);
    List<History> findByUserName(String name);

    @Transactional
    void deleteAllByUserId(long id);

}
