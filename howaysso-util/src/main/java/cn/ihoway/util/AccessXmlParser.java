package cn.ihoway.util;

/**
 * access.xml解析器
 */
public class AccessXmlParser {
    public static XmlParser parser;
    public static void init() throws ConfigException{
        parser = new XmlParser();
        parser.setPathName("/META-INF/access.xml");
        parser.init();
    }
}
