package cn.ihoway.dao;

import cn.ihoway.entity.User;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;

@Service
public interface UserDao {

    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    User selectByName(String name);

    User selectByTel(String tel);

    User selectByNameAndPass(@Param("name")String name, @Param("password")String password);

    User selectByTelAndPass(String tel,String password);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

}