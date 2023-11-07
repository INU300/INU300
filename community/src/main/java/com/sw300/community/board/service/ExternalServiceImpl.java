package com.sw300.community.board.service;

import com.sw300.community.board.repository.BoardRepository;
import com.sw300.community.category.repository.CategoryRepository;
import java.util.List;
import java.util.stream.Collectors;
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
            systemMessage.put("content", "대답은 무슨게시판 이렇게 무슨 게시판인지 단어만 말해줘. 다른 말은 하지 말고.");
            messages.put(systemMessage);

            List<String> mainCategoryNames = categoryRepository.findAllMainCategoryNames();
            String categoriesString = mainCategoryNames.stream()
                    .collect(Collectors.joining(", ", "[", "]"));

            // 사용자 메시지
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", "제목은 \"" + title +
                    "\"이고, 내용은 \"" + contents +
                    "\"이야. 이 글을 " + categoriesString + " 중에서 카테고리 분류해줘.");
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

    public String generateImage(String title, String contents) {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(DALLE_URL);

            // JSON 요청 본문 생성
            JSONObject json = new JSONObject();
            json.put("model", "dall-e-3");
            json.put("prompt", "제목은 \"" + title +
                    "\"이고, 내용은 \"" + contents + "\"이야. "
                    + "이 글을 쓴 사람에게 위로와 격려를 주는 그림을 그려줘. 글쓴이의 감정을 편안하게 좋은 상태로 해줄 수 있는 그림 그려줘.");
            json.put("n", 1);
            json.put("size", "1024x1024");

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

    @Override
    public String hasViolence(String title, String contents) {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(GPT_URL);

            JSONObject json = new JSONObject();
            json.put("model", "gpt-4-0613");

            JSONArray messages = new JSONArray();

            // 시스템 메시지
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "대답은 0 또는 1 둘 중 하나로만 해줘. 다른 말은 하지 말고.");
            messages.put(systemMessage);

            // 사용자 메시지
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", "제목은 " + title +
                    "이고, 내용은 " + contents +
                    "이야. 이 글의 유해성, 폭력성을 5단계로 분석해서 4단계 이상에 해당하는지 판단해줘. 4단계 이상이면 1, 아니면 0으로 대답해줘.");
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

    @Override
    public String giveEncouragement(String title, String contents) {

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost httpPost = new HttpPost(GPT_URL);

            JSONObject json = new JSONObject();
            json.put("model", "gpt-4-0613");

            JSONArray messages = new JSONArray();

            // 시스템 메시지
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "깨끗한 커뮤니티를 만들기 위한 의도야.");
            messages.put(systemMessage);

            // 사용자 메시지
            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", "제목은 " + title +
                    "이고, 내용은 " + contents +
                    "이야. 이 글의 작성자에게 위로와 격려의 메시지를 작성해줘.");
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
            String content = choices.getJSONObject(0).getJSONObject("message").getString("content");

            return content;
        } catch (Exception e) {
            logger.error("Error while classifying content:", e);
            return e.getMessage();
        }
    }

}
