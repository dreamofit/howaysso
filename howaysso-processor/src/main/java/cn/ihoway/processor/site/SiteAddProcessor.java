package cn.ihoway.processor.site;

import cn.ihoway.annotation.Processor;
import cn.ihoway.common.CommonProcessor;
import cn.ihoway.entity.Site;
import cn.ihoway.impl.SiteServiceImpl;
import cn.ihoway.processor.site.io.SiteAddInput;
import cn.ihoway.processor.site.io.SiteAddOutput;
import cn.ihoway.service.SiteService;
import cn.ihoway.type.AlgorithmType;
import cn.ihoway.type.AuthorityLevel;
import cn.ihoway.type.StatusCode;
import cn.ihoway.util.Convert;
import cn.ihoway.util.HowayContainer;
import cn.ihoway.util.HowayEncrypt;
import cn.ihoway.util.HowayLog;
import cn.ihoway.util.HowayResult;
import org.apache.commons.lang3.StringUtils;

import java.util.UUID;

@Processor(name = "SiteAddProcessor",certification = true,limitAuthority = AuthorityLevel.ADMINISTRATOR)
public class SiteAddProcessor extends CommonProcessor<SiteAddInput, SiteAddOutput> {

    private final SiteService siteService = (SiteServiceImpl) HowayContainer.getBean("siteServiceImpl");;
    private final HowayLog logger = new HowayLog(SiteAddProcessor.class);

    @Override
    protected StatusCode dataCheck(SiteAddInput input){
        if(StringUtils.isEmpty(input.inChomm.name) || StringUtils.isEmpty(input.inChomm.url)){
            return StatusCode.FIELD_MISSING;
        }
        return StatusCode.SUCCESS;
    }

    @Override
    protected HowayResult beforeProcess(SiteAddInput input,SiteAddOutput output){
        if(input.inChomm.enable == null || input.inChomm.enable < 0 || input.inChomm.enable > 1){
            input.inChomm.enable = 1;
        }
        if(input.inChomm.rank == null || input.inChomm.rank < 0){
            input.inChomm.rank = AuthorityLevel.COMMON_MEMBER.getLevel();
        }
        return HowayResult.createSuccessResult(output);
    }

    @Override
    protected HowayResult process(SiteAddInput input, SiteAddOutput output) {
        Site site = packToSite(input);
        UUID uuid = UUID.randomUUID();
        site.setAppkey(HowayEncrypt.encrypt(uuid.toString(), AlgorithmType.MD5.getAlgorithm(), 4));
        uuid = UUID.randomUUID();
        site.setAppsecret(HowayEncrypt.encrypt(uuid.toString(), AlgorithmType.MD5.getAlgorithm(), 8));
        if(siteService.addSite(site) != 1){
            logger.info("插入失败");
            return HowayResult.createFailResult(StatusCode.INSERT_ERROR,output);
        }
        logger.info("插入成功");
        return HowayResult.createSuccessResult(output);
    }


    private Site packToSite(SiteAddInput input){
        Convert convert = new Convert();
        return (Site) convert.inputToBean(Site.class.getName(),SiteAddInput.class.getName() + "$InChomm",input.inChomm);
    }
}
