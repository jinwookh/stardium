package com.bb.stardium.bench.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@AllArgsConstructor
@Getter
public class Address {
    @NotBlank
    private String city;

    @NotBlank
    private String gu; // TODO : gu를 영어로 뭐라고 하지?

    @NotBlank
    private String detail;
}
