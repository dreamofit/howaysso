package cn.ihoway.service;

import cn.ihoway.entity.User;

import java.util.List;

public interface UserService {
    int addUser(User user);
    User findById(int id);
    List<User> findAll();
    User findByName(String name);
    User findByTel(String tel);
    User loginByNameAndPass(String name, String password);
    User loginByTelAndPass(String tel, String password);
    int updateUser(User user);
    void free();
}
