/**
 * 프론트에서 전달받은 데이터를 맵핑하고, Reply 모델과 맵핑하기 위한 객체
 */

package com.sw300.community.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReplySaveRequestDto {
//    private long userId;
    private long boardId;
    private String content;
}
