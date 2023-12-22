package com.model;

import lombok.Getter;
import lombok.Setter;

@lombok.Data
@Setter
@Getter
public class WebSocketEntity {
    /**
     * 请求类型
     */
    private String action;
    /**
     * 模型参数
     */
    private Params params;
    /**
     * 发送的音频数据
     */
    private String data;

    /**
     * 后端超时静音时长
     */
    private Integer vadEos = 1000;

}
