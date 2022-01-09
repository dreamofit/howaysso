package cn.ihoway.service;

import cn.ihoway.entity.Site;

import java.util.List;

public interface SiteService {
    int addSite(Site site);
    Site findSiteById(Integer id);
    Site findSiteByName(String name);
    Site findSiteByUrl(String url);
    List<Site> findAll();
    int update(Site site);
    int delete(Integer id);

}
