package com.sw300.community.board.service;

public interface ExternalService {

    /**
     * 카테고리 분류 gpt
     */
    String classifyContent(String title, String contents);


    /**
     * 이미지 생성 dalle
     */
    String generateImage(String title, String contents);

    /**
     * 폭력성 여부 gpt
     */
    String hasViolence(String title, String contents);

    /**
     * 위로의 메시지 gpt
     */
    String giveEncouragement(String title, String contents);

}

