package com;

import com.alibaba.fastjson.JSON;
import com.model.WebSocketEntity;
import org.springframework.scheduling.annotation.Async;

import java.io.InputStream;
import java.util.Base64;

/**
 * @author: kobe
 * @Date: 2021/7/6 16:14
 */
public class FileUtil extends Thread {

    @Override
    public synchronized void start() {
        super.start();
    }

    @Override
    public void run() {
        sendData();
    }

    private Client client;
    private InputStream inputStream;
    final Base64.Encoder encoder = Base64.getEncoder();

    /**
     * 200ms 包大小，间隔 100ms 发送
     */
    int size = 6400;

    public FileUtil(Client client, InputStream inputStream) {
        this.client = client;
        this.inputStream = inputStream;
    }

    @Async
    public void sendData() {
        try {
            byte[] b = new byte[size];
            int n;

            WebSocketEntity entity = new WebSocketEntity();
            /*
              消息类型：发送数据
             */
            entity.setAction("send_data");
            // 发送音频数据包
            while ((n = inputStream.read(b)) != -1) {
                if (n < b.length) {
                    byte[] tempData = new byte[n];
                    System.arraycopy(b, 0, tempData, 0, n);
                    entity.setData(encoder.encodeToString(tempData));
                    client.send(JSON.toJSONString(entity));
                } else {
                    entity.setData(encoder.encodeToString(b));
                    client.send(JSON.toJSONString(entity));
                    Thread.sleep(100);
                }
            }
            inputStream.close();

            /*
              消息类型：发送结束包
              数据发送完成，开启静音检测可不发送此类型包
             */
            entity.setAction("end_data");
            client.send(JSON.toJSONString(entity));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
