package com.jju.howe.howeassistant.bean;

/**
 * Created by Howe on 2016/10/23.
 */

public class SlotsBean {
    private String name;
    private String code;
    private String content;
    private String messageType;


    private String keywords;
    /**
     * date : 2016-10-23
     * type : DT_BASIC
     * time : 16:00:00
     * timeOrig : 四点
     */

    private DatetimeBean datetime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getMessageType() {
        return messageType;
    }

    public void setMessageType(String messageType) {
        this.messageType = messageType;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public DatetimeBean getDatetime() {
        return datetime;
    }

    public void setDatetime(DatetimeBean datetime) {
        this.datetime = datetime;
    }

}
