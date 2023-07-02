package cn.ihoway.util;

public class AccessXmlParser {
    public static XmlParser parser;
    public static void init() throws ConfigException{
        parser = new XmlParser();
        parser.setPathName("/META-INF/access.xml");
        parser.init();
    }
}
