package com.psionicgeek.temprecor;

public class ModelClass {
    private String cropedface;
    private String Temp;
    private String Phone;
    private String UserName;
    private String dateandtime;
    ModelClass(){}
    public ModelClass(String cropedface, String temp, String phone, String userName, String dateandtime) {
        this.cropedface = cropedface;
        this.Temp = temp;
        this.Phone = phone;
        this.UserName = userName;
        this.dateandtime = dateandtime;
    }
    public String getCropedface() {
        return cropedface;
    }

    public void setCropedface(String cropedface) {
        this.cropedface = cropedface;
    }

    public String getTemp() {
        return Temp;
    }

    public void setTemp(String temp) {
        this.Temp = temp;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        this.Phone = phone;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        this.UserName = userName;
    }

    public String getDateandtime() {
        return dateandtime;
    }

    public void setDateandtime(String dateandtime) {
        this.dateandtime = dateandtime;
    }
}
