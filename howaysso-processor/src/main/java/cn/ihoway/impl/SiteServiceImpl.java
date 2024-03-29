package cn.ihoway.impl;

import cn.ihoway.dao.SiteDao;
import cn.ihoway.entity.Site;
import cn.ihoway.service.SiteService;
import cn.ihoway.util.MybatisUtils;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SiteServiceImpl implements SiteService {

    private final SqlSession sqlSession = MybatisUtils.getSqlSession();
    private final SiteDao siteDao = sqlSession.getMapper(SiteDao.class);

    @Override
    public int addSite(Site site) {
        int rs = siteDao.insertSelective(site);
        if(rs > 0){
            sqlSession.commit();
        }else {
            sqlSession.rollback();
        }
        return rs;
    }

    @Override
    public Site findSiteById(Integer id) {
        return siteDao.selectByPrimaryKey(id);
    }

    @Override
    public Site findSiteByName(String name) {
        return siteDao.selectByName(name);
    }

    @Override
    public Site findSiteByUrl(String url) {
        return siteDao.selectByUrl(url);
    }

    @Override
    public Site findSiteByAppKey(String appKey) {
        return siteDao.selectByAppKey(appKey);
    }

    @Override
    public List<Site> findAll() {
        return siteDao.selectAll();
    }

    @Override
    public int update(Site site) {
        int rs = siteDao.updateByPrimaryKeySelective(site);
        if(rs > 0){
            sqlSession.commit();
        }else {
            sqlSession.rollback();
        }
        return rs;
    }

    @Override
    public int delete(Integer id) {
        return siteDao.deleteByPrimaryKey(id);
    }

    @Override
    public void free(){
        sqlSession.close();
    }
}
