package com.youdao.aicloud.translate.service;

import com.youdao.aicloud.translate.dto.AiDto;
import com.youdao.aicloud.translate.pojo.Ai;
import com.youdao.aicloud.translate.pojo.AiImage;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;

/**
 * 有道业务层
 */
public interface AiService {

    /**
     * ai对话
     * @param aiDto
     * @return
     */
    public Ai aiDialogue(AiDto aiDto) throws NoSuchAlgorithmException;

    /**
     * 获取图片文字
     * @param file
     * @return
     */
    public AiImage imageFile(MultipartFile file) throws NoSuchAlgorithmException, IOException;
}
