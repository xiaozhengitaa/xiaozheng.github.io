package com.youdao.aicloud.translate.controller;


import com.youdao.aicloud.translate.dto.AiDto;
import com.youdao.aicloud.translate.pojo.Ai;
import com.youdao.aicloud.translate.pojo.AiImage;
import com.youdao.aicloud.translate.service.AiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

@Controller
@RequestMapping("/ai")
public class AiController {
    @Autowired
    private AiService aiService;

    /**
     * 单词语句翻译
     * @param aiDto
     * @return
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/word")
    public ResponseEntity show(@RequestBody AiDto aiDto) throws NoSuchAlgorithmException {
        Ai ai=aiService.aiDialogue(aiDto);
        return new ResponseEntity<>(ai, HttpStatus.OK);
    }

    /**
     * 图片翻译
     * @param file
     * @return
     * @throws IOException
     * @throws NoSuchAlgorithmException
     */
    @PostMapping("/image")
    public ResponseEntity imageFile(MultipartFile file) throws IOException, NoSuchAlgorithmException {
        AiImage aiImage=aiService.imageFile(file);
        return new ResponseEntity(aiImage,HttpStatus.OK);
    }
}
