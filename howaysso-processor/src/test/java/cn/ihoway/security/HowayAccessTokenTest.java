package cn.ihoway.security;

import org.junit.jupiter.api.Test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class HowayAccessTokenTest {
    private HowayAccessToken accessToken = new HowayAccessToken();

    @Test
    void getToken() throws ParseException, InterruptedException {

        for (int i = 0; i < 20; i++){
            String token = accessToken.getToken(57,"张三","123456","5582","d1497b84");
            //System.out.println(token);
            System.out.println(accessToken.isToekenRule(token,"张三","123456"));

        }
        //System.out.println(accessToken.isToekenRule("e0b6a851a0a8a4cf2b3cae164112189d9512","张三","123456","asdf","mnkl89"));
       // System.out.println(accessToken.isToekenRule("7d62c4e71c89fec4c339a11641125207806n","张三","123456","asdf","mnkl89"));


    }
}