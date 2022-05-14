package com.migia.basic.repository;

import com.migia.basic.models.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
    public User findByName(String name);
    public boolean existsByName(String name);
    public boolean existsByEmail(String email);
}
