package cn.ihoway.controller.site;

import cn.ihoway.processor.site.SiteAddProcessor;
import cn.ihoway.processor.site.io.SiteAddInput;
import cn.ihoway.processor.site.io.SiteAddOutput;
import cn.ihoway.util.HowayResult;
import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/site")
public class SiteController {

    @CrossOrigin
    @RequestMapping(value = "/", method = { RequestMethod.POST })
    public String add(String token,@RequestBody JSONObject site){
        SiteAddProcessor processor = new SiteAddProcessor();
        SiteAddOutput output = new SiteAddOutput();
        SiteAddInput input = new SiteAddInput();
        input.token = token;
        input.inChomm.name = site.getString("name");
        input.inChomm.url = site.getString("url");
        input.inChomm.rank = site.getInteger("rank");
        input.inChomm.enable = site.getInteger("enable");
        HowayResult rs = processor.doExcute(input,output);
        return rs.toString();
    }
}
