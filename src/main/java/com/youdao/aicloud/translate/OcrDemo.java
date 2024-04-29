package com.youdao.aicloud.translate;

import com.youdao.aicloud.translate.utils.AuthV3Util;
import com.youdao.aicloud.translate.utils.FileUtil;
import com.youdao.aicloud.translate.utils.HttpUtil;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * 网易有道智云通用OCR服务api调用demo
 * api接口: https://openapi.youdao.com/ocrapi
 */
public class OcrDemo {

    private static final String APP_KEY = "3cd0bd1978d380ec";     // 您的应用ID
    private static final String APP_SECRET = "L9u05yyWj1GTfCvvSleIibuXfoW3cFH9";  // 您的应用密钥

    // 待识别图片路径, 例windows路径：PATH = "C:\\youdao\\media.jpg";
    private static final String PATH = "C:\\Users\\Administrator\\Pictures\\Saved Pictures\\aaa.webp";

    public static void main(String[] args) throws NoSuchAlgorithmException, IOException {
        // 添加请求参数
        Map<String, String[]> params = createRequestParams();
        // 添加鉴权相关参数
        AuthV3Util.addAuthParams(APP_KEY, APP_SECRET, params);
        // 请求api服务
        byte[] result = HttpUtil.doPost("https://openapi.youdao.com/ocrapi", null, params, "application/json");
        // 打印返回结果
        if (result != null) {
            System.out.println(new String(result, StandardCharsets.UTF_8));
        }
        System.exit(1);
    }

    private static Map<String, String[]> createRequestParams() throws IOException {
        /*
         * note: 将下列变量替换为需要请求的参数
         * 取值参考文档: https://ai.youdao.com/DOCSIRMA/html/%E6%96%87%E5%AD%97%E8%AF%86%E5%88%ABOCR/API%E6%96%87%E6%A1%A3/%E9%80%9A%E7%94%A8%E6%96%87%E5%AD%97%E8%AF%86%E5%88%AB%E6%9C%8D%E5%8A%A1/%E9%80%9A%E7%94%A8%E6%96%87%E5%AD%97%E8%AF%86%E5%88%AB%E6%9C%8D%E5%8A%A1-API%E6%96%87%E6%A1%A3.html
         */
        String langType = "zh-CHS";
        String detectType = "10012";
        String angle = "0";
//        String column = "是否按多列识别";
//        String rotate = "是否需要获得文字旋转角度";
        String docType = "json";
        String imageType = "1";

        // 数据的base64编码
        String img = FileUtil.loadMediaAsBase64(PATH);
        return new HashMap<String, String[]>() {{
            put("img", new String[]{img});
            put("langType", new String[]{langType});
            put("detectType", new String[]{detectType});
            put("angle", new String[]{angle});
//            put("column", new String[]{column});
//            put("rotate", new String[]{rotate});
            put("docType", new String[]{docType});
            put("imageType", new String[]{imageType});
        }};
    }
}
