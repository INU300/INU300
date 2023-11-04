package com.sw300.community.board.service;


import com.sw300.community.board.repository.BoardRepository;
import com.sw300.community.category.repository.CategoryRepository;
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
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ExternalServiceImpl implements ExternalService {

    private static final String GPT_URL = "https://api.openai.com/v1/chat/completions";
    private static final String DALLE_URL = "https://api.openai.com/v1/images/generations";
    @Value("${openai-api-key}")
    private String OPENAI_API_KEY;

    private static final Logger logger = LoggerFactory.getLogger(ExternalServiceImpl.class);

    private final CategoryRepository categoryRepository;
    private final BoardRepository boardRepository;

    @Override
    public String classifyContent(String title, String contents) {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(GPT_URL);

            JSONObject json = new JSONObject();
            json.put("model", "gpt-4-0613");

            JSONArray messages = new JSONArray();

            // 시스템 메시지
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "대답은 '무엇' 이렇게 단어만 말해줘. 다른 말은 하지 말고.");
            messages.put(systemMessage);

            // 사용자 메시지
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", "제목은 " + title +
                    "이고, 내용은 " + contents +
                    "이야. 이 글을 연애/건강/취업 중에서 카테고리 분류해줘.");
            messages.put(userMessage);

            System.out.println("Messages: " + messages.toString());

            json.put("messages", messages);

            StringEntity entity = new StringEntity(json.toString(), "UTF-8");
            httpPost.setEntity(entity);
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpPost.setHeader("Authorization", "Bearer " + OPENAI_API_KEY);

            String responseString = EntityUtils.toString(httpClient.execute(httpPost).getEntity(), "UTF-8");
            logger.info("OpenAI Response: " + responseString);

            JSONObject responseJson = new JSONObject(responseString);

            JSONArray choices = responseJson.getJSONArray("choices");
            String content = choices.getJSONObject(0).getJSONObject("message").getString("content").trim();

            String firstWord = content.split(" ")[0];
            return firstWord;
        } catch (Exception e) {
            logger.error("Error while classifying content:", e);
            return e.getMessage();
        }
    }

    public String generateImage(String prompt) {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(DALLE_URL);

            // JSON 요청 본문 생성
            JSONObject json = new JSONObject();
            json.put("prompt", prompt);
            json.put("n", 1);
            json.put("size", "512x512");

            // JSON으로 변환된 요청 본문을 StringEntity로 변환
            StringEntity entity = new StringEntity(json.toString(), "UTF-8");
            httpPost.setEntity(entity);

            // 필요한 헤더 설정
            httpPost.setHeader(HttpHeaders.CONTENT_TYPE, "application/json");
            httpPost.setHeader("Authorization", "Bearer " + OPENAI_API_KEY);

            // HTTP 요청 실행
            String responseString = EntityUtils.toString(httpClient.execute(httpPost).getEntity(), "UTF-8");
            logger.info("OpenAI Response: " + responseString);

            // 응답 JSON 파싱
            JSONObject responseJson = new JSONObject(responseString);

            // 이미지 URL 추출
            String imageUrl = responseJson.getJSONArray("data")
                    .getJSONObject(0)
                    .getString("url");

            return imageUrl;
        } catch (Exception e) {
            logger.error("Error while generating image:", e);
            return e.getMessage();
        }
    }

}
