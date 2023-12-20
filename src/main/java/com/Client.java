package com;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.io.InputStream;
import java.net.URI;

/**
 * 首先需要导入 maven 库：
 * <dependency>
 * 　　<groupId>org.java-websocket</groupId>
 * 　　<artifactId>Java-WebSocket</artifactId>
 * 　　<version>1.3.8</version>
 * </dependency>
 *
 * @author viruser
 */
public class Client extends WebSocketClient {
    private static final String APP_ID = "6BB241B0A66C46239520231206145533";// 应用Id
    private static final String APP_KEY = "03AB767E8E37882293618F11B8A5C0A3";// 应用key
    private static final String NEW_ENGINE_TYPE = "2101";
    private static final String STREAM = "continue";// 是否连续识别，sentence：单句模式，最长1分钟；continue：连续识别模式，最长2小时
    private static final int SEX = 0;
    private static final int VAD_EOS = 200;
    private static final String WS_URL = "ws://speech.ths8.com:6011/SpeechDictation/v1/ws/";
    private static Client myClient;

    public InputStream inputStream;

    public Client(URI serverUri, InputStream inputStream) {
        super(serverUri);
        this.inputStream = inputStream;
        if (myClient == null) {
            // 建立链接，需要加锁
            myClient = this;
        }
        myClient.connect();
    }

    @Override
    public void onOpen(ServerHandshake arg0) {
        System.out.println("------ MyWebSocket onOpen ------");
        // 引擎参数
        Params params = new Params();
        /**  模型编号 */
        params.setNewEngineType(NEW_ENGINE_TYPE);
        /**  性别开关  */
        params.setSex(SEX);
        // 往 WebSocket 服务端发送数据
        WebSocketEntity entity = new WebSocketEntity();
        /** 消息类型：申请令牌  */
        entity.setAction("apply_token");
        /** 设置 后端静音超时  */
        entity.setVadEos(VAD_EOS);
        /** 引擎参数  */
        entity.setParams(params);
        // 申请令牌
        myClient.send(JSON.toJSONString(entity));
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        System.out.println("------ MyWebSocket onClose ------");
    }

    @Override
    public void onError(Exception arg0) {
        System.out.println("------ MyWebSocket onError ------");
    }

    @Override
    public void onMessage(String arg0) {
        JSONObject result = JSON.parseObject(arg0);
        String action = result.getString("action");

        // 申请令牌
        if (action.equalsIgnoreCase("apply_token")) {
            String data = result.getString("data");
            JSONObject tokenData = JSONObject.parseObject(data);
            if (0 == tokenData.getInteger("code")) {
                System.out.println("-------- 令牌申请成功--------\n" + arg0);
                // 申请令牌成功，开始发送数据
                new Thread(new FileUtil(myClient, inputStream)).start();
            }
        }

        if (action.equalsIgnoreCase("realtime_result")) {
            System.out.println("实时识别结果：" + result);
        }
        if (action.equalsIgnoreCase("sentence_result")) {
            System.out.println("静音检测断句，最终识别结果：" + result);
        }
        if (action.equalsIgnoreCase("asr_result")) {
            System.out.println("最终识别结果：" + result);
        }
        if (action.equalsIgnoreCase("asr_end")) {
            System.out.println("识别结束：" + result);
        }
    }
}