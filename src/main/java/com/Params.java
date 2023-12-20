package com;

public class Params {
    /**
     * 模型类型
     */
    String newEngineType;
    /**
     * 用户编号
     */
    String userNumber;
    /**
     * 引擎参数
     */
    String asrParams="{\"piny\":0,\"ori_freq\":16000,\"symbol\":1,\"flag\":0,\"cn\":0,\"type\":0}";
    /**
     * 性别
     */
    int sex;

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
}
