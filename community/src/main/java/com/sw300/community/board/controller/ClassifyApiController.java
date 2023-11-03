package com.sw300.community.board.controller;

import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
public class ClassifyApiController {

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    @Value("${openai-api-key}")
    private String OPENAI_API_KEY;

    private static final Logger logger = LoggerFactory.getLogger(ClassifyApiController.class);

    @PostMapping("/api/externalClassify")
    public ResponseEntity<String> classifyContent(@RequestBody Map<String, String> requestData) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(OPENAI_URL);

            JSONObject json = new JSONObject();
            json.put("model", "gpt-3.5-turbo");

            JSONArray messages = new JSONArray();

            JSONObject contentMessage = new JSONObject();
            contentMessage.put("role", "user");
            contentMessage.put("content", "제목은 " + requestData.get("title") +
                    "이고, 내용은 " + requestData.get("content") +
                    "이야. 이 글을 연애게시판/학업게시판 중에서 카테고리 분류해줘. 그리고 행복/분노/두려움/슬픔/기쁨 중에서 글쓴이의 감정을 분석해줘.");
            messages.put(contentMessage);

            System.out.println("Content Message: " + contentMessage.toString());

            json.put("messages", messages);

            StringEntity entity = new StringEntity(json.toString(), "UTF-8"); // UTF-8 인코딩 지정
            httpPost.setEntity(entity);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpPost.setHeader("Authorization", "Bearer " + OPENAI_API_KEY);

            String responseString = EntityUtils.toString(httpClient.execute(httpPost).getEntity(), "UTF-8");
            logger.info("OpenAI Response: " + responseString);

            JSONObject responseJson = new JSONObject(responseString);

            JSONArray choices = responseJson.getJSONArray("choices");
            String content = choices.getJSONObject(0).getJSONObject("message").getString("content").trim();

            // 분석된 결과를 JSON 객체로 변환
            String[] results = content.split(",");
            JSONObject resultJson = new JSONObject();
            resultJson.put("category", results[0].trim());
            resultJson.put("feeling", results[1].trim());

            return ResponseEntity.ok(resultJson.toString());
        } catch (Exception e) {
            logger.error("Error while classifying content:", e);
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }



//    @PostMapping("/api/externalClassify")
//    public ResponseEntity<String> classifyContent(@RequestBody Map<String, String> requestData) {
//        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
//            HttpPost httpPost = new HttpPost(OPENAI_URL);
//
//            JSONObject json = new JSONObject();
//            json.put("model", "gpt-3.5-turbo");
//
//            JSONArray messages = new JSONArray();
//
//            JSONObject contentMessage = new JSONObject();
//            contentMessage.put("role", "user");
//            contentMessage.put("content", "제목은 " + requestData.get("title") +
//                    "이고, 내용은 " + requestData.get("content") +
//                    "이야. 이 글을 연애게시판/학업게시판/진로게시판 중에서 카테고리 분류해줘. 대답은 '무슨게시판' 이렇게 단어만 말해줘. 다른 말은 하지 말고.");
//            messages.put(contentMessage);
//
//            System.out.println("Content Message: " + contentMessage.toString());
//
//            json.put("messages", messages);
//
//            StringEntity entity = new StringEntity(json.toString(), "UTF-8"); // UTF-8 인코딩 지정
//            httpPost.setEntity(entity);
//            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
//            httpPost.setHeader("Authorization", "Bearer " + OPENAI_API_KEY);
//
//            String responseString = EntityUtils.toString(httpClient.execute(httpPost).getEntity(), "UTF-8");
//            logger.info("OpenAI Response: " + responseString);
//
//            JSONObject responseJson = new JSONObject(responseString);
//
//            JSONArray choices = responseJson.getJSONArray("choices");
//            String content = choices.getJSONObject(0).getJSONObject("message").getString("content").trim();
//
//            String firstWord = content.split(" ")[0];
//            return ResponseEntity.ok(firstWord);
//        } catch (Exception e) {
//            logger.error("Error while classifying content:", e);
//            return ResponseEntity.status(500).body("Error: " + e.getMessage());
//        }
//    }
}
