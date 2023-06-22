package cn.ihoway.util;

public class AccessXmlParser {
    public static XmlParser parser;
    public static void init(){
        parser = new XmlParser();
        parser.setPathName("/META-INF/access.xml");
        parser.init();
    }
}
