package com.sw300.community.board.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ResponseMessageHeader {

    private boolean result;
    private String resultCode;
    private String message;
    private int status;

}
