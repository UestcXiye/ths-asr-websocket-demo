package com;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.model.Params;
import com.model.WebSocketEntity;
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
    /**
     * newEngineType：引擎类型码
     * 说明：中文普通话、通用、16k
     */
    private static final String NEW_ENGINE_TYPE = "2101";

    /**
     * 是否开启性别识别 0-否 1-是，默认为 0
     */
    private static final int SEX = 0;

    /**
     * 静音检测后端超时时长，单位 ms
     */
    private static final int VAD_EOS = 200;
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
        // 设置模型编号
        params.setNewEngineType(NEW_ENGINE_TYPE);
        // 设置性别开关
        params.setSex(SEX);

        // 往 WebSocket 服务端发送数据
        WebSocketEntity entity = new WebSocketEntity();
        // 设置发送申请令牌指令
        entity.setAction("apply_token");
        // 设置静音检测后端超时时长为 200ms
        entity.setVadEos(VAD_EOS);
        // 设置引擎参数
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
        if ("apply_token".equalsIgnoreCase(action)) {
            String data = result.getString("data");
            JSONObject tokenData = JSONObject.parseObject(data);
            if (0 == tokenData.getInteger("code")) {
                System.out.println("-------- 令牌申请成功--------\n" + arg0);
                // 申请令牌成功，开始发送数据
                new Thread(new FileUtil(myClient, inputStream)).start();
            }
        }

        // 如果 action 是接收实时结果指令
        if ("realtime_result".equalsIgnoreCase(action)) {
            System.out.println("实时识别结果：" + result);
        }
        // 如果 action 是接收静音检测断句结果指令
        if ("sentence_result".equalsIgnoreCase(action)) {
            System.out.println("静音检测断句，最终识别结果：" + result);
        }
        // 如果 action 是接收最终识别结果指令
        if ("asr_result".equalsIgnoreCase(action)) {
            System.out.println("最终识别结果：" + result);
        }
        // 如果 action 是接收识别结束指令
        if ("asr_end".equalsIgnoreCase(action)) {
            System.out.println("识别结束：" + result);
        }
        // 如果 action 是未匹配到指令或者指令异常
        if ("no_command".equalsIgnoreCase((action))) {
            System.out.println("未匹配到指令或者指令异常：" + result);
        }
    }
}