package com.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 申请令牌请求参数
 */
@lombok.Data
@NoArgsConstructor
@AllArgsConstructor
public class WebSocketEntity {
    /**
     * 请求类型
     */
    private String action;
    /**
     * 模型参数
     */
    private EngineParams engineParams;
    /**
     * 发送的音频数据
     */
    private String data;

    /**
     * 后端超时静音时长
     */
    private Integer vadEos = 1000;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public EngineParams getEngineParams() {
        return engineParams;
    }

    public void setEngineParams(EngineParams engineParams) {
        this.engineParams = engineParams;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Integer getVadEos() {
        return vadEos;
    }

    public void setVadEos(Integer vadEos) {
        this.vadEos = vadEos;
    }
}
