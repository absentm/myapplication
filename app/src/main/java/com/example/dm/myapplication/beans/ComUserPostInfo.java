package com.example.dm.myapplication.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by dm on 16-4-24.
 * 用户发布信息表
 */
public class ComUserPostInfo extends BmobObject implements Serializable {
    private String userNameStr;   // 用户名
    private String userHeadImgUrl;      // 用户图像id：如需求变更，可设置为String
    private String userNickNameStr;     // 用户昵称
    private String userTimeStr;     // 用户发布时间
    private String userContentStr;  // 用户发布内容
    private long userTimeMills;    // 时间戳

    private List<String> userImageUrlList = new ArrayList<>();      // 用户发布图片集合

    private Integer userRepostCounter;
    private Integer userCommentCounter;
    private Integer userLikeCounter;

    public ComUserPostInfo() {
    }

    public ComUserPostInfo(String userNameStr, String userHeadImgUrl, String userNickNameStr, String userTimeStr, String userContentStr, long userTimeMills, List<String> userImageUrlList, Integer userRepostCounter, Integer userCommentCounter, Integer userLikeCounter) {
        this.userNameStr = userNameStr;
        this.userHeadImgUrl = userHeadImgUrl;
        this.userNickNameStr = userNickNameStr;
        this.userTimeStr = userTimeStr;
        this.userContentStr = userContentStr;
        this.userTimeMills = userTimeMills;
        this.userImageUrlList = userImageUrlList;
        this.userRepostCounter = userRepostCounter;
        this.userCommentCounter = userCommentCounter;
        this.userLikeCounter = userLikeCounter;
    }

    public String getUserNameStr() {
        return userNameStr;
    }

    public void setUserNameStr(String userNameStr) {
        this.userNameStr = userNameStr;
    }

    public String getUserHeadImgUrl() {
        return userHeadImgUrl;
    }

    public void setUserHeadImgUrl(String userHeadImgUrl) {
        this.userHeadImgUrl = userHeadImgUrl;
    }

    public String getUserNickNameStr() {
        return userNickNameStr;
    }

    public void setUserNickNameStr(String userNickNameStr) {
        this.userNickNameStr = userNickNameStr;
    }

    public String getUserTimeStr() {
        return userTimeStr;
    }

    public void setUserTimeStr(String userTimeStr) {
        this.userTimeStr = userTimeStr;
    }

    public String getUserContentStr() {
        return userContentStr;
    }

    public void setUserContentStr(String userContentStr) {
        this.userContentStr = userContentStr;
    }

    public long getUserTimeMills() {
        return userTimeMills;
    }

    public void setUserTimeMills(long userTimeMills) {
        this.userTimeMills = userTimeMills;
    }

    public List<String> getUserImageUrlList() {
        return userImageUrlList;
    }

    public void setUserImageUrlList(List<String> userImageUrlList) {
        this.userImageUrlList = userImageUrlList;
    }

    public Integer getUserRepostCounter() {
        return userRepostCounter;
    }

    public void setUserRepostCounter(Integer userRepostCounter) {
        this.userRepostCounter = userRepostCounter;
    }

    public Integer getUserCommentCounter() {
        return userCommentCounter;
    }

    public void setUserCommentCounter(Integer userCommentCounter) {
        this.userCommentCounter = userCommentCounter;
    }

    public Integer getUserLikeCounter() {
        return userLikeCounter;
    }

    public void setUserLikeCounter(Integer userLikeCounter) {
        this.userLikeCounter = userLikeCounter;
    }
}
