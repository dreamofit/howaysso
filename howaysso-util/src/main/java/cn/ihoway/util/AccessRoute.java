package cn.ihoway.util;

import cn.ihoway.type.StatusCode;
import org.apache.commons.lang.StringUtils;
import org.dom4j.Element;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 * 所有接口进入入口
 * @author howay
 */
public class AccessRoute {


    /**
     *
     * @param request 请求
     * @param serviceId 服务id
     * @param map 输入
     * @return HowayResult
     */
    public static HowayResult handle(HttpServletRequest request,String serviceId, Map<String,Object> map){
        XmlParser parser = AccessXmlParser.parser;
        Element element = parser.getElement(serviceId);
        String processor = String.valueOf(parser.getValueFromElement(element,"processor"));
        String input = String.valueOf(parser.getValueFromElement(element,"input"));
        String output = String.valueOf(parser.getValueFromElement(element,"output"));
        String dir = String.valueOf(parser.getValueFromElement(element,"dir"));
        String prefix = String.valueOf(parser.getValueFromElement(element,"prefix"));
        String commonPrefix = HowayConfigReader.getConfig("access.properties","package.name") + dir;
        String processorName = commonPrefix + "." + prefix + HowayConfigReader.getConfig("access.properties","processor.name");
        String inputName = commonPrefix + ".io." + prefix + HowayConfigReader.getConfig("access.properties","input.name");
        String outputName = commonPrefix + ".io." + prefix + HowayConfigReader.getConfig("access.properties","output.name");
        if(StringUtils.isNotBlank(processor)){
            processorName = processor;
        }
        if(StringUtils.isNotBlank(input)){
            inputName = input;
        }
        if(StringUtils.isNotBlank(output)){
            outputName = output;
        }
        return handle(request,processorName,inputName,outputName,map);
    }


    /**
     *
     * @param request 请求
     * @param processorName 处理类类名
     * @param inputName 输入类类名
     * @param outputName 输出类类名
     * @param map 输入
     * @return HowayResult
     */
    private static HowayResult handle(HttpServletRequest request,String processorName, String inputName,String outputName, Map<String,Object> map) {
        HowayLog logger = new HowayLog(AccessRoute.class);
        Convert convert = new Convert();
        Object input = convert.mapToInput(map,inputName);
        Class<?> inputClz;
        try {
            inputClz = Class.forName(inputName);
            Field[] fields = inputClz.getFields();
            for (Field field : fields){
                if("ip".equals(field.getName())){
                    field.set(input,HowayRequest.getIpAddr(request)); //input.ip赋值
                }
            }
            return execute(processorName,outputName,input);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return HowayResult.createFailResult(StatusCode.JAVAEXCEPTION,null);
    }

    /**
     *
     * @param processorName 逻辑处理类位置
     * @param outputName 输出类位置
     * @param input 输入
     * @return 结果
     */
    private static HowayResult execute(String processorName, String outputName, Object input) throws Exception {
        Class<?> processorClz = Class.forName(processorName);
        Class<?> outputClz = Class.forName(outputName);
        Object processor = processorClz.getDeclaredConstructor().newInstance();
        Object output = outputClz.getDeclaredConstructor().newInstance();
        HowayResult res = null;
        Method[] methods = processorClz.getMethods();
        for (Method method : methods){
            if("doExecute".equals(method.getName())){
                res = (HowayResult) method.invoke(processor,input,output);
            }
        }
        return res;
    }
}
