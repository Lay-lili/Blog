package com.lili.blog.service;

import com.lili.blog.bean.User;

public interface UserService {

    User loginIn(String username, String password);
}
