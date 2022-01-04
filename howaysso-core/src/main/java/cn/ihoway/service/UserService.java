package cn.ihoway.service;

import cn.ihoway.entity.User;

import java.util.List;

public interface UserService {
    public int addUser(User user);
    public User findById(int id);
    public List<User> findAll();
    public User findByName(String name);
    public User findByTel(String tel);
    public User loginByNameAndPass(String name,String password);
    public User loginByTelAndPass(String tel,String password);
    public int updateUser(User user);
}
