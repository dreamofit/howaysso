package cn.ihoway.web.controller.site;

import cn.ihoway.util.AccessRoute;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping(value = "/site")
public class SiteController {

    //todo site 查询和更新、删除API
    //todo 暂时计划项目：forum(个人空间以及文章汇总，类似it之家)、howay-dubbo-base（基础项目，各项目框架springboot+dubbo）
    //todo 全文搜索引擎（此系统可独立为服务）+无界面爬虫系统

    @CrossOrigin
    @RequestMapping(value = "/", method = { RequestMethod.POST })
    public String add(@RequestBody JSONObject site, HttpServletRequest request){
        HowayResult rs = AccessRoute.handle(request,"siteAdd",site);
        return rs.toString();
    }
}
