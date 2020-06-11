package com.springcloud.management.repository;

import com.springcloud.management.entity.LoginManager;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface LoginManagerRepository extends JpaRepository<LoginManager,Integer> {

    List<LoginManager> findAll();

    LoginManager findByUsernameAndPassword(String username, String password);
}
