package com.controller;

import com.Client;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * @BelongsProject:dream_house
 * @BelongsPackage:com.example.dream_house.controller
 * @Author:Uestc_Xiye
 * @CreateTime:2023-12-17 17:17:36
 */


@RestController
@CrossOrigin
public class DataController {

    private static final String APP_ID = "6BB241B0A66C46239520231206145533";// 应用Id
    private static final String APP_KEY = "03AB767E8E37882293618F11B8A5C0A3";// 应用key
    private static final String STREAM = "continue";// 是否连续识别，sentence：单句模式，最长1分钟；continue：连续识别模式，最长2小时
    private static final String WS_URL = "ws://speech.ths8.com:6011/SpeechDictation/v1/ws/";
    private static Client myClient;

    @PostMapping("/upload")
    @ResponseBody
    public String getPCMFile(@RequestParam("file") MultipartFile pcmFile) throws IOException, URISyntaxException {
        // WebSocket 服务端 URI
        URI url = new URI(WS_URL + APP_ID + "/" + APP_KEY + "/" + STREAM);
        myClient = new Client(url, pcmFile.getInputStream());
        return "succeed";
    }
}
