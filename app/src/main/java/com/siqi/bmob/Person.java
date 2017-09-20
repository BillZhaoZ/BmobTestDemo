package com.siqi.bmob;

import cn.bmob.v3.BmobObject;

/**
 * 实体类
 */
public class Person extends BmobObject {

    private String name;
    private String address;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

}