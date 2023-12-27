package com.websocket;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @BelongsProject:demo
 * @BelongsPackage:com.websocket
 * @Author:Uestc_Xiye
 * @CreateTime:2023-12-26 15:36:10
 */
@Configuration
public class WebSocketConfig {
    /**
     * WebSocket 配置，开启 WebSocket 支持
     * 注入 ServerEndpointExporter，这个 bean 会自动注册使用了 @ServerEndpoint 注解声明的 Websocket endpoint
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}