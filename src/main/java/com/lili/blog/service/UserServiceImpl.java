package com.lili.blog.service;

import com.lili.blog.bean.User;
import com.lili.blog.dao.UserRepository;
import com.lili.blog.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public User loginIn(String username, String password) {

        User user = userRepository.findByUsernameAndPassword(username, MD5Util.code(password));

        return user;
    }
}




