package com.youdao.aicloud.translate.dto;

import lombok.Data;

@Data
public class AiDto {

    //要翻译的文本
    private String word;

    //原语言
    private String i;

    //目标语言
    private String o;

}
