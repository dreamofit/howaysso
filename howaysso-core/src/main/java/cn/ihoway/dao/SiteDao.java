package cn.ihoway.dao;

import cn.ihoway.entity.Site;
import org.springframework.stereotype.Service;

@Service
public interface SiteDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Site record);

    int insertSelective(Site record);

    Site selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Site record);

    int updateByPrimaryKey(Site record);
}