package com.wanda.chatbot.pojo;

/**
 * Created by nocml on 2017/6/8.
 */
public class ExtraInformation {
    private String classOrigin;
    //patternFlag 0为默认值
    private int patternFlag = 0;
    public void setExtraInformation(ExtraInformation extraInformation){
        this.classOrigin = extraInformation.getClassOrigin();
        this.patternFlag = extraInformation.getPatternFlag();
    }
    public ExtraInformation() {
    }

    public ExtraInformation(String classOrigin) {
        this.classOrigin = classOrigin;
    }

    public ExtraInformation(int patternFlag) {
        this.patternFlag = patternFlag;
    }

    public ExtraInformation(String classOrigin, int patternFlag) {
        this.classOrigin = classOrigin;
        this.patternFlag = patternFlag;
    }

    @Override
    public String toString() {
        return "ExtraInformation{" +
                "classOrigin='" + classOrigin + '\'' +
                ", patternFlag=" + patternFlag +
                '}';
    }

    public String getClassOrigin() {
        return classOrigin;
    }

    public void setClassOrigin(String classOrigin) {
        this.classOrigin = classOrigin;
    }

    public int getPatternFlag() {
        return patternFlag;
    }

    public void setPatternFlag(int patternFlag) {
        this.patternFlag = patternFlag;
    }
}
