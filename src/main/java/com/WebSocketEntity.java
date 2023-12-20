package com;

public class WebSocketEntity {
    /**
     * 请求类型
     */
    String action;
    /**
     * 模型参数
     */
    Params params;
    /**
     * 发送的音频数据
     */
    String data;

    /**
     * 后端超时静音时长
     */
    Integer vadEos = 1000;

    public Integer getVadEos() {
        return vadEos;
    }

    public void setVadEos(Integer vadEos) {
        this.vadEos = vadEos;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Params getParams() {
        return params;
    }

    public void setParams(Params params) {
        this.params = params;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
