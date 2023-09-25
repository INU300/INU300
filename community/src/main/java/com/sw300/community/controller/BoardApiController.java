package com.sw300.community.controller;

import com.sw300.community.dto.ResponseDto;
import com.sw300.community.model.Board;
import com.sw300.community.service.BoardService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardApiController {

    @Autowired
    private BoardService boardService;

    @ApiOperation("글 작성")
    @PostMapping("api/board")
    public ResponseDto<Integer> save(@RequestBody Board board) {
        boardService.writePost(board);
        return new ResponseDto<>(HttpStatus.OK, 1);
    }

    @ApiOperation("글 삭제")
    @DeleteMapping("/api/board/{id}")
    public ResponseDto<Integer> deleteById(@PathVariable int id) {
        boardService.deletePost(id);
        return new ResponseDto<>(HttpStatus.OK, 1);
    }

    @ApiOperation("글 수정")
    @PutMapping("/api/board/{id}")
    public ResponseDto<Integer> update(@PathVariable int id, @RequestBody Board board) {
        boardService.updatePost(id,board);
        return new ResponseDto<>(HttpStatus.OK, 1);
    }
}
