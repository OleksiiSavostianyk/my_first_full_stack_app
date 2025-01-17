package com.alex.spring_myfirstpetproject.DAO;

import com.alex.spring_myfirstpetproject.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {
    public Optional<User> findByUsername(String username);
    public Optional<User> findByEmail(String email);

}
