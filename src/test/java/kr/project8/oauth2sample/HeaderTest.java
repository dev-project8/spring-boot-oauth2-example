package kr.project8.oauth2sample;

import org.junit.Test;

public class HeaderTest {

    @Test
    public void tesT(){
        String a = "JSESSIONID=69C3DBD996B205B6EAC418B21A72CD51; Path=/; HttpOnly";
        String s = a.replaceAll("JSESSIONID=", "").replaceAll("; Path=/; HttpOnly", "");
        System.out.println(s);
    }
}
