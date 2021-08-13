package com.psionicgeek.temprecor;

public class Constants {

    public static BitmapTransfer bitmapTransfer;
    public static String temperature;
    public static String Phone_Number;
    public static String Name ;
    public static String Download_Uri;
    public static String DateAndTime;
    public static String Uid;
    public String cropedface;
    public String Temp;

    public Constants( String  FaceUri,String Temp) {
        this.cropedface = FaceUri;
        this.Temp=Temp;

    }
}
