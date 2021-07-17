package com.example.kakao.pay;

import com.example.kakao.pay.model.KakaoPayApprovalVO;
import com.example.kakao.pay.model.KakaoPayReadyVO;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@Service
@Log
public class KakaoPay {

    private static final String HOST = "https://kapi.kakao.com";

    private KakaoPayReadyVO kakaoPayReadyVO;

    private KakaoPayApprovalVO kakaoPayApprovalVO;

    RestTemplate restTemplate = new RestTemplate();

    public static HttpHeaders headers() {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "KakaoAK " + " ");
        headers.add("Accept", "application/x-www-form-urlencoded;charset=utf-8");
        headers.add("Content-Type", MediaType.APPLICATION_FORM_URLENCODED_VALUE + ";charset=UTF-8");
        return headers;
    }

    public String kakaoPayReady() {


        // 서버로 요청할 Header

        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "gorany");
        params.add("item_name", "이준형과 1일 데이트권");
        params.add("quantity", "1");
        params.add("total_amount", "500000");
        params.add("tax_free_amount", "100");
        params.add("approval_url", "http://localhost:8090/kakaoPaySuccess");
        params.add("cancel_url", "http://localhost:8090/kakaoPayCancel");
        params.add("fail_url", "http://localhost:8090/kakaoPaySuccessFail");

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers());

        try {
            kakaoPayReadyVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/ready"), body, KakaoPayReadyVO.class);

            log.info("" + kakaoPayReadyVO);

            return kakaoPayReadyVO.getNext_redirect_pc_url();

        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return "KakaoPayFail"; //실패시
    }

    public KakaoPayApprovalVO kakaoPayInfo(String pg_token) {

        log.info("KakaoPayInfoVO............................................");
        log.info("-----------------------------");



        // 서버로 요청할 Header


        // 서버로 요청할 Body
        MultiValueMap<String, String> params = new LinkedMultiValueMap<String, String>();
        params.add("cid", "TC0ONETIME");
        params.add("tid", kakaoPayReadyVO.getTid());
        params.add("partner_order_id", "1001");
        params.add("partner_user_id", "gorany");
        params.add("pg_token", pg_token);
        params.add("total_amount", "2100");

        HttpEntity<MultiValueMap<String, String>> body = new HttpEntity<MultiValueMap<String, String>>(params, headers());

        try {
            kakaoPayApprovalVO = restTemplate.postForObject(new URI(HOST + "/v1/payment/approve"), body, KakaoPayApprovalVO.class);
            log.info("" + kakaoPayApprovalVO);

            return kakaoPayApprovalVO;

        } catch (RestClientException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return null;
    }

}