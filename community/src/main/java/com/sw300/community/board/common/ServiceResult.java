package com.sw300.community.board.common;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ServiceResult {

    private boolean result;
    private String message;

    public static ServiceResult fail(String message) {
        return ServiceResult.builder()
                .result(false)
                .message(message)
                .build();
    }

    public static ServiceResult success() {
        return ServiceResult.builder()
                .result(true)
                .build();
    }

    public boolean isFail() {
        return !result;
    }

}
