/**
 * board.js의 요청을 받는 클래스
 */

package com.sw300.community.controller;

import com.sw300.community.dto.ReplySaveRequestDto;
import com.sw300.community.dto.ResponseDto;
import com.sw300.community.model.Board;
import com.sw300.community.service.BoardService;
import io.swagger.annotations.ApiOperation;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

@RestController
public class BoardApiController {

    private static final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private static final String OPENAI_API_KEY = "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";
    private static final Logger logger = LoggerFactory.getLogger(BoardApiController.class);

    @Autowired
    private BoardService boardService;

//    @ApiOperation("글 작성")
    @PostMapping("api/board")
    public ResponseDto<Integer> save(@RequestBody Board board) {
        boardService.writePost(board);
        return new ResponseDto<>(HttpStatus.OK, 1);
    }

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
                    "이야. 이 글을 연애게시판/학업게시판/진로게시판 중에서 카테고리 분류해줘. 대답은 '무슨게시판' 이렇게 단어만 말해줘. 다른 말은 하지 말고.");
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

            String firstWord = content.split(" ")[0];
            return ResponseEntity.ok(firstWord);
        } catch (Exception e) {
            logger.error("Error while classifying content:", e);
            return ResponseEntity.status(500).body("Error: " + e.getMessage());
        }
    }

//    @ApiOperation("글 삭제")
    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable int id) {
        boardService.deletePost(id);
        return new ResponseDto<>(HttpStatus.OK, 1);
    }

//    @ApiOperation("글 수정")
    @PutMapping("/api/board/{id}")
    public ResponseDto<Integer> update(@PathVariable int id, @RequestBody Board board) {
        boardService.updatePost(id,board);
        return new ResponseDto<>(HttpStatus.OK, 1);
    }

    @PostMapping("api/board/{boardId}/{voteType}")
    public ResponseDto<Integer> vote(@PathVariable Long boardId, @PathVariable String voteType) {
        return boardService.vote(boardId, voteType);
    }

    @PostMapping("/api/board/{boardId}/reply")
    public ResponseDto<Integer> saveReply(@PathVariable Long boardId, @RequestBody ReplySaveRequestDto reply) {
        reply.setBoardId(boardId);
        boardService.writeReply(reply);
        return new ResponseDto<>(HttpStatus.OK, 1);
    }

    @DeleteMapping("/api/board/reply/{replyId}")
    public ResponseDto<Integer> deleteReply(@PathVariable long replyId) {
        boardService.deleteReply(replyId);
        return new ResponseDto<>(HttpStatus.OK, 1);
    }


}
