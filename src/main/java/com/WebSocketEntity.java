package com;

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
