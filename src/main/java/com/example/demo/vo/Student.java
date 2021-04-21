package com.example.demo.vo;

public class Student {
    // 学号
    String stuId;
    // 答题得分
    int score;
    // 观看页数
    int page;
    // 签到次数
    int attendance;
    // 弹幕总次数
    int barrage;
    // 投稿总次数
    int submission;
    // 阅读公告数
    int announcement;

    public String getStuId() {
        return stuId;
    }

    public void setStuId(String stuId) {
        this.stuId = stuId;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getAttendance() {
        return attendance;
    }

    public void setAttendance(int attendance) {
        this.attendance = attendance;
    }

    public int getBarrage() {
        return barrage;
    }

    public void setBarrage(int barrage) {
        this.barrage = barrage;
    }

    public int getSubmission() {
        return submission;
    }

    public void setSubmission(int submission) {
        this.submission = submission;
    }

    public int getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(int announcement) {
        this.announcement = announcement;
    }
}
