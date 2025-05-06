package com.example.testcenter.model.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResp {

    @Schema(description = "id")
    private Long id;

    @Schema(description = "User email")
    private String username;

}
