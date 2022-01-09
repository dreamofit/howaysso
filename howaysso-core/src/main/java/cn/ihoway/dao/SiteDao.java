package cn.ihoway.dao;

import cn.ihoway.entity.Site;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface SiteDao {
    int deleteByPrimaryKey(Integer id);

    int insert(Site record);

    int insertSelective(Site record);

    Site selectByPrimaryKey(Integer id);

    Site selectByName(String name);

    Site selectByUrl(String url);

    List<Site> selectAll();

    int updateByPrimaryKeySelective(Site record);

    int updateByPrimaryKey(Site record);
}