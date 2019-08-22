package com.cqkj.snail.system.entity;

/**
 * 用户信息实体
 * @author 闻维波
 * @since 2019/08/09 13:42
 */
public class UserEntity {
    // 用户ID
    private String id;
    // 用户登陆名
    private String loginName;
    // 用户姓名
    private String userName;
    // 电话
    private String phone;
    // 登录密码
    private String loginPassword;

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public String getPhone() {
        return phone;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public String getLoginName() {
        return loginName;
    }

    public String getUserName() {
        return userName;
    }
}
