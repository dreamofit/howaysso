package cn.ihoway.processor.site;

import cn.ihoway.processor.site.io.SiteAddInput;
import cn.ihoway.processor.site.io.SiteAddOutput;
import cn.ihoway.util.HowayEncrypt;
import cn.ihoway.util.HowayResult;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SiteAddProcessorTest {
    private final SiteAddProcessor processor = new SiteAddProcessor();

    @Test
    void process() {
        SiteAddInput input = new SiteAddInput();
        input.token = "c4bbhrj4a87a7567bcc8cc6098164164940813b";
        input.inChomm.name = "sso";
        input.inChomm.url = "/sso";
        SiteAddOutput output = new SiteAddOutput();
        HowayResult res = processor.doExcute(input,output);
        System.out.println(res.toString());
    }
}