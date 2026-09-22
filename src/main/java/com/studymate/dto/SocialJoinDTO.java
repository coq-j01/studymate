package com.studymate.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

import lombok.Data;

@Data
public class SocialJoinDTO {
    @NotBlank
    private String nickname;

    @NotBlank
    @Pattern(regexp = "F|M")
    private String gender;

}
