package cn.ihoway.processor.site.io;

import cn.ihoway.common.CommonSeria;
import cn.ihoway.common.io.CommonInput;

public class SiteAddInput  extends CommonInput {
    public InChomm inChomm = new InChomm();
    public static class InChomm extends CommonSeria {
        public String name;
        public String url;
        public Integer rank;
        public Integer enable; //网站是否启用，0未启用，1启用
    }
}
