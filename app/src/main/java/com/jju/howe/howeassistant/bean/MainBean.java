package com.jju.howe.howeassistant.bean;

import java.util.List;

/**
 * Created by Howe on 2016/10/23.
 */

public class MainBean {

    /**
     * moreResults : [{"rc":0,"answer":{"type":"T","text":"\u201c晚上好\u201d是注重环保和舒适的织花布家纺品牌，此外，\u201c晚上好\u201d也是日常用语之一!"},"service":"baike","text":"晚上好。","operation":"ANSWER"}]
     * rc : 0
     * operation : ANSWER
     * service : chat
     * answer : {"text":"确实很晚了\u2026\u2026还不睡呢！再睡会儿就天亮啦！","type":"T"}
     * text : 晚上好。
     */

    private int rc ;
    private String operation;
    private String service;
    /**
     * text : 确实很晚了……还不睡呢！再睡会儿就天亮啦！
     * type : T
     */

    private AnswerBean answer;
    private String text;
    /**
     * rc : 0
     * answer : {"type":"T","text":"\u201c晚上好\u201d是注重环保和舒适的织花布家纺品牌，此外，\u201c晚上好\u201d也是日常用语之一!"}
     * service : baike
     * text : 晚上好。
     * operation : ANSWER
     */

    private List<MoreResultsBean> moreResults;
    /**
     * slots : {"name":"张三"}
     */

    private SemanticBean semantic;
    /**
     * header :
     * url : http://kcbj.openspeech.cn/service/iss?wuuid=3b7c98f06fae3918d9ae139ad5abb44f&ver=2.0&method=webPage&uuid=9c998fecffe2744d816b7578bb18432bquery
     */

    private WebPageBean webPage;
    private DataBean data;


    public int getRc() {
        return rc;
    }

    public void setRc(int rc) {
        this.rc = rc;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public AnswerBean getAnswer() {
        return answer;
    }

    public void setAnswer(AnswerBean answer) {
        this.answer = answer;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public List<MoreResultsBean> getMoreResults() {
        return moreResults;
    }

    public void setMoreResults(List<MoreResultsBean> moreResults) {
        this.moreResults = moreResults;
    }


    public SemanticBean getSemantic() {
        return semantic;
    }

    public void setSemantic(SemanticBean semantic) {
        this.semantic = semantic;
    }

    public WebPageBean getWebPage() {
        return webPage;
    }

    public void setWebPage(WebPageBean webPage) {
        this.webPage = webPage;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }



}
