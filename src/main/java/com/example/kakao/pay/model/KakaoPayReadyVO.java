package com.example.kakao.pay.model;

import lombok.Data;

import java.util.Date;

@Data
public class KakaoPayReadyVO {

    //response
    private String tid, next_redirect_pc_url;//결제 고유번호(20자), 요청한 클라이언트가 pc 웹일 경우 redirect. 카카오톡으로 TMS를 보내기 위한 사용자입력화면이으로 redirect
    private Date created_at;//결제 준비 요청시간

}
