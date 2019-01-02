package com.hots.learn.model;

import com.hots.common.component.excel.basic.BasicCellField;

public class User {

    @BasicCellField(column = 1, name = "序号", isExport = true)
    private int id;

    @BasicCellField(column = 2, name = "用户名", isExport = true)
    private String userName;

    @BasicCellField(column = 3, name = "密码", isExport = true)
    private String password;

    @BasicCellField(column = 4, name = "邮箱", isExport = true)
    private String email;

    @BasicCellField(column = 5, name = "卡号", isExport = true)
    private String cardNo;

    @BasicCellField(column = 6, name = "电话号码", isExport = true)
    private String phoneNo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }
}
