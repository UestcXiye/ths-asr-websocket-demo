package com;

public class Params {
    /**
     * 模型类型
     */
    private String newEngineType;

    /**
     * 用户编号
     */
    private String userNumber;

    /**
     * 引擎参数
     * piny: 0 表不获取拼音信息，1 表示获取，默认为 0
     * symbol: 是否获取标点，0-否，1-是
     * type: 0 表示纯文本，1 表示 json 格式的文本，2 表示增加时间戳的 json 格式文本，默认为 0
     */
    private String asrParams="{\"piny\":0,\"ori_freq\":16000,\"symbol\":1,\"flag\":0,\"cn\":0,\"type\":0}";

    /**
     * 性别
     */
    private int sex;

    /**
     * 是否开启情绪识别 0-否 1-是，默认为 0
     */
    private int emotion;

    public String getNewEngineType() {
        return newEngineType;
    }

    public void setNewEngineType(String newEngineType) {
        this.newEngineType = newEngineType;
    }

    public String getUserNumber() {
        return userNumber;
    }

    public void setUserNumber(String userNumber) {
        this.userNumber = userNumber;
    }

    public String getAsrParams() {
        return asrParams;
    }

    public void setAsrParams(String asrParams) {
        this.asrParams = asrParams;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getEmotion() {
        return emotion;
    }

    public void setEmotion(int emotion) {
        this.emotion = emotion;
    }
}
