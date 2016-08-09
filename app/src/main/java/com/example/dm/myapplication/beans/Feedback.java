package com.example.dm.myapplication.beans;

import cn.bmob.v3.BmobObject;

/**
 * Created by dm on 16-4-17.
 * 意见反馈javabean
 */
public class Feedback extends BmobObject {
    private String userId;
    private String feebackUserName;
    private String feedbackUserEmail;
    private String feedbackContent;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getFeebackUserName() {
        return feebackUserName;
    }

    public void setFeebackUserName(String feebackUserName) {
        this.feebackUserName = feebackUserName;
    }

    public String getFeedbackUserEmail() {
        return feedbackUserEmail;
    }

    public void setFeedbackUserEmail(String feedbackUserEmail) {
        this.feedbackUserEmail = feedbackUserEmail;
    }

    public String getFeedbackContent() {
        return feedbackContent;
    }

    public void setFeedbackContent(String feedbackContent) {
        this.feedbackContent = feedbackContent;
    }
}
