package com.sw300.community.board.service;

public interface ExternalService {

    /**
     * 카테고리 분류 gpt
     */
    String classifyContent(String title, String contents);


    /**
     * 이미지 생성 dalle
     */
    String generateImage(String prompt);
}

