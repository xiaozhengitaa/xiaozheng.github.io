package com.youdao.aicloud.translate.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.youdao.aicloud.translate.dto.AiDto;
import com.youdao.aicloud.translate.pojo.Ai;
import com.youdao.aicloud.translate.pojo.AiImage;
import com.youdao.aicloud.translate.service.AiService;
import com.youdao.aicloud.translate.utils.AuthV3Util;
import com.youdao.aicloud.translate.utils.FileUtil;
import com.youdao.aicloud.translate.utils.HttpUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * 有道业务层
 */

@Service
public class AiServiceImpl implements AiService {

    private static final String APP_KEY = "4f55bce435a24054";     // 您的应用ID
    private static final String APP_SECRET = "0QeAnVDegamLhDgducm9yOi6UEUGsWgx";  // 您的应用密钥

    /**
     * ai对话
     *
     * @param aiDto
     * @return
     */
    public Ai aiDialogue(AiDto aiDto) throws NoSuchAlgorithmException {
        Map<String, String[]> params = createRequestParams(aiDto);
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/api", null, params, "application/json");

        String str = new String(result, StandardCharsets.UTF_8);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(str);

            String query = jsonNode.get("query").asText();

            String translation = jsonNode.get("translation").get(0).asText();
            Ai ai = new Ai();
            ai.setUserConversation(query);
            ai.setAiConversation(translation);
            return ai;
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.exit(1);
        return null;
    }

    private static Map<String, String[]> createRequestParams(AiDto aiDto) {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档: https://ai.youdao.com/DOCSIRMA/html/%E8%87%AA%E7%84%B6%E8%AF%AD%E8%A8%80%E7%BF%BB%E8%AF%91/API%E6%96%87%E6%A1%A3/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1/%E6%96%87%E6%9C%AC%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1-API%E6%96%87%E6%A1%A3.html
         */
        String q = aiDto.getWord();
        String from = aiDto.getI();
        String to = aiDto.getO();
//        String vocabId = "您的用户词表ID";
        return new HashMap<String, String[]>() {{
            put("q", new String[]{q});
            put("from", new String[]{from});
            put("to", new String[]{to});
//            put("vocabId", new String[]{vocabId});
        }};
    }

    /**
     * 获取图片文字
     *
     * @param file
     * @return
     */
    public AiImage imageFile(MultipartFile file) throws NoSuchAlgorithmException, IOException {

        String uploadDir = "src/main/resources/image/";
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();
                String filename = file.getOriginalFilename();
                String imageName= UUID.randomUUID().toString();
                String substring = filename.substring(file.getOriginalFilename().lastIndexOf("."));
                File newFile = new File(uploadDir + imageName+substring);
                FileOutputStream fileOutputStream = new FileOutputStream(newFile);
                fileOutputStream.write(bytes);
                fileOutputStream.close();
                // 添加请求参数
                Map<String, String[]> params = createRequestParams(newFile.getAbsolutePath());
                // 添加鉴权相关参数
                AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
                // 请求api服务
                byte[] result = HttpUtil.doPost("https://openapi.youdao.com/ocrtransapi", null, params, "application/json");
                // 打印返回结果
                if (result != null) {
                    String re = new String(result, StandardCharsets.UTF_8);
                    Gson gson = new Gson();
                    JsonObject jsonObject = gson.fromJson(re, JsonObject.class);
                    StringBuffer stringBuffer=new StringBuffer();
                    JsonArray resRegionsArray = jsonObject.getAsJsonArray("resRegions");
                    if (resRegionsArray != null && resRegionsArray.size() > 0) {
                        for (int i=0;i<resRegionsArray.size();i++){
                            JsonObject firstResRegion = resRegionsArray.get(i).getAsJsonObject();
                            String tranContent = firstResRegion.get("tranContent").getAsString();
                            stringBuffer.append(tranContent);
                        }
                        AiImage aiImage = new AiImage();
                        aiImage.setText(stringBuffer.toString());
                        return aiImage;
                    } else {
                        return null;
                    }
                }
                System.exit(1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Map<String, String[]> createRequestParams(String path) throws IOException {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档: https://ai.youdao.com/DOCSIRMA/html/%E8%87%AA%E7%84%B6%E8%AF%AD%E8%A8%80%E7%BF%BB%E8%AF%91/API%E6%96%87%E6%A1%A3/%E5%9B%BE%E7%89%87%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1/%E5%9B%BE%E7%89%87%E7%BF%BB%E8%AF%91%E6%9C%8D%E5%8A%A1-API%E6%96%87%E6%A1%A3.html
         */
        String from = "zh-CHS";
        String to = "zh-CHS";
        String render = "1";
        String type = "1";

        // 数据的base64编码
        String q = FileUtil.loadMediaAsBase64(path);
        return new HashMap<String, String[]>() {{
            put("q", new String[]{q});
            put("from", new String[]{from});
            put("to", new String[]{to});
            put("render", new String[]{render});
            put("type", new String[]{type});
        }};
    }

}