package cn.ihoway.provider.security;

import org.junit.jupiter.api.Test;

import java.text.ParseException;

class HowayAccessTokenTest {
    private HowayAccessToken accessToken = new HowayAccessToken();

    @Test
    void getToken() throws ParseException, InterruptedException {

        System.out.println(accessToken.isToekenRule("240guz4c4e62c0f5582a88027bf164355598549b","李四","2f4ce48a2f919d6df49edef749142e87"));

//        for (int i = 0; i < 20; i++){
//            String token = accessToken.getToken(57,"张三","123456","5582","d1497b84");
//            //System.out.println(token);
//            System.out.println(accessToken.isToekenRule(240guz4c4e62c0f5582a88027bf164355598549b,"张三","123456"));
//
//        }
        //System.out.println(accessToken.isToekenRule("e0b6a851a0a8a4cf2b3cae164112189d9512","张三","123456","asdf","mnkl89"));
       // System.out.println(accessToken.isToekenRule("7d62c4e71c89fec4c339a11641125207806n","张三","123456","asdf","mnkl89"));


    }
}