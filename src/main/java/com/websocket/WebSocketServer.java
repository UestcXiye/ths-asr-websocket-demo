package com.websocket;

import com.Client;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.sql.Blob;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @BelongsProject:demo
 * @BelongsPackage:com.websocket
 * @Author:Uestc_Xiye
 * @CreateTime:2023-12-26 15:34:21
 */


@ServerEndpoint("/websocket/{userId}")  // 接口路径 ws://localhost:8087/webSocket/userId;
@Component
@Slf4j
public class WebSocketServer {
    /**
     * 接口地址
     */
    private static final String WS_URL = "ws://speech.ths8.com:6011/SpeechDictation/v1/ws/";

    /**
     * 应用 Id
     */
    private static final String APP_ID = "6BB241B0A66C46239520231206145533";
    /**
     * 应用 key
     */
    private static final String APP_KEY = "03AB767E8E37882293618F11B8A5C0A3";
    /**
     * 是否连续识别
     * sentence：单句模式，最长 1 分钟
     * continue：连续识别模式，最长 2 小时
     */
    private static final String STREAM = "continue";
    private static Client myClient;

    /**
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的
     */
    private static int onlineCount = 0;

    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;

    /**
     * 用户ID
     */
    private String userId;

    /**
     * concurrent 包的线程安全 HashMap，用来存放每个客户端对应的 WebSocket 对象
     * 虽然 @Component 默认是单例模式的，但 SpringBoot 还是会为每个 WebSocket 连接初始化一个 bean，所以可以用一个静态 HashMap 保存起来
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();


    /**
     * 连接建立成功调用的方法
     *
     * @param session
     * @param userId
     */
    @OnOpen
    public void onOpen(Session session, @PathParam(value = "userId") String userId) {
        try {
            this.session = session;
            this.userId = userId;
            if (webSocketMap.containsKey(userId)) {
                webSocketMap.remove(userId);
                // 加入 HashMap
                webSocketMap.put(userId, this);
            } else {
                // 加入 HashMap
                webSocketMap.put(userId, this);
                // 在线人数 +1
                addOnlineCount();
            }
            log.info("【WebSocket 消息】有新的连接，用户 ID：" + userId + "，当前在线人数为：" + getOnlineCount());
            sendMessage("连接成功");
        } catch (Exception e) {
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            if (webSocketMap.containsKey(userId)) {
                // 从 HashMap 中删除
                webSocketMap.remove(userId);
                // 在线人数 -1
                subOnlineCount();
            }
            log.info("【WebSocket 消息】用户退出：" + userId + "，当前在线人数为：" + getOnlineCount());
        } catch (Exception e) {
        }
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param session
     * @param message 客户端发送过来的消息
     */
    //@OnMessage
    //public void onMessage(Session session, String message) {
    //    log.info("【WebSocket 消息】收到客户端消息：" + message + "，用户ID：" + userId);
    //}

    @OnMessage(maxMessageSize = 10000000)
    public void onMessage(Session session, ByteBuffer message) throws URISyntaxException {
        log.info("【WebSocket 消息】收到客户端传来的二进制流，用户ID：" + userId);
        // System.out.println("音频数据报文：" + message);
        // WebSocket 服务端 URI
        URI url = new URI(WS_URL + APP_ID + "/" + APP_KEY + "/" + STREAM);
        myClient = new Client(url, new ByteArrayInputStream(message.array()));
    }

    /**
     * 发送错误时的处理
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("用户错误：" + this.userId + "，原因：" + error.getMessage());
        error.printStackTrace();
    }

    /**
     * 广播消息
     *
     * @param message 要发送的消息
     */
    public void sendAllMessage(String message) {
        log.info("【WebSocket 消息】广播消息：" + message);
        for (WebSocketServer webSocketServer : webSocketMap.values()) {
            try {
                if (webSocketServer.session.isOpen()) {
                    webSocketServer.session.getAsyncRemote().sendText(message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 单播消息
     *
     * @param userId  用户 Id
     * @param message 要发送的消息
     */
    public void sendOneMessage(String userId, String message) {
        Session session = webSocketMap.get(userId).session;
        if (session != null && session.isOpen()) {
            try {
                log.info("【WebSocket 消息】单播消息：" + message);
                session.getAsyncRemote().sendText(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 多播消息
     *
     * @param userIds 用户 Id 数组
     * @param message 要发送的消息
     */
    public void sendMoreMessage(String[] userIds, String message) {
        for (String userId : userIds) {
            Session session = webSocketMap.get(userId).session;
            if (session != null && session.isOpen()) {
                try {
                    log.info("【WebSocket 消息】多播消息：" + message);
                    session.getAsyncRemote().sendText(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 服务器主动推送消息
     *
     * @param message 要发送的消息
     */
    public void sendMessage(String message) {
        this.session.getAsyncRemote().sendText(message);
    }


    /**
     * 向指定客户端发送消息
     *
     * @param userId
     * @param message
     */
    public static void sendInfo(@PathParam("userId") String userId, String message) {
        log.info("发送消息到：" + userId + "，报文：" + message);
        if (!StringUtils.isEmpty(userId) && webSocketMap.containsKey(userId)) {
            webSocketMap.get(userId).sendMessage(message);
        } else {
            log.error("用户 " + userId + "，不在线！");
        }
    }

    /**
     * 获得此时的在线人数
     *
     * @return onlineCount
     */
    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    /**
     * 在线人数 +1
     */
    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    /**
     * 在线人数 -1
     */
    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}
