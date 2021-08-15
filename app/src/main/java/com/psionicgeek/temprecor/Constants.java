package com.psionicgeek.temprecor;

public class Constants {

    public static BitmapTransfer bitmapTransfer;
    public static String temperature;
    public static String UserMobileNumber;
    public static String Name ;
    public static String Download_Uri;
    public static String DateAndTime;
    public static String Uid;
    public String cropedface;
    public String Temp;
    public String Phone;
    public String UserName;
    public String dateandtime;

//
//    public Constants( String  FaceUri,String Temp) {
//        this.cropedface = FaceUri;
//        this.Temp=Temp;
//
//    }
    public Constants(){}

    public Constants(String cropedface, String temp, String phone, String userName, String dateandtime) {
        this.cropedface = cropedface;
        Temp = temp;
        this.Phone = phone;
        UserName = userName;
        this.dateandtime = dateandtime;
    }


}
