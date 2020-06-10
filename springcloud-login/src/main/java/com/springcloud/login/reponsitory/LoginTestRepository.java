package com.springcloud.login.reponsitory;

import com.springcloud.login.entity.LoginTest;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;
import java.util.List;

@Transactional
public interface LoginTestRepository extends JpaRepository<LoginTest,Integer> {

    List<LoginTest> findAll();

    LoginTest findByUsernameAndPassword(String username, String password);
}
