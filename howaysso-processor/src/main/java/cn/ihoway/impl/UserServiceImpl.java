package cn.ihoway.impl;


import cn.ihoway.dao.UserDao;
import cn.ihoway.entity.User;
import cn.ihoway.service.UserService;
import cn.ihoway.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    private final SqlSession sqlSession = MybatisUtils.getSqlSession();
    private final UserDao userDao = sqlSession.getMapper(UserDao.class);

    //添加用户
    @Override
    public int addUser(User user) {
        int rs = userDao.insertSelective(user);
        if(rs > 0){
            sqlSession.commit();
        }else {
            sqlSession.rollback();
        }
        return rs;
    }

    @Override
    public User findById(int id) {
        return userDao.selectByPrimaryKey(id);
    }

    @Override
    public List<User> findAll() {
        return userDao.selectAll();
    }

    @Override
    public User findByName(String name) {
        return userDao.selectByName(name);
    }

    @Override
    public User findByTel(String tel) {
        return userDao.selectByTel(tel);
    }

    @Override
    public User loginByNameAndPass(String name, String password) {
        return userDao.selectByNameAndPass(name,password);
    }

    @Override
    public User loginByTelAndPass(String tel, String password) {
        return userDao.selectByTelAndPass(tel,password);
    }

    @Override
    public int updateUser(User user) {
        int rs = userDao.updateByPrimaryKeySelective(user);
        if(rs > 0){
            sqlSession.commit();
        }else {
            sqlSession.rollback();
        }
        return rs;
    }

    @Override
    public void free(){
        sqlSession.close();
    }
}
