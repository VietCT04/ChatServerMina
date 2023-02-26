package com.facenet.mina.object;

/**
 *
 * @author VietCT
 */
public class messageObj extends java.lang.Object implements java.io.Serializable {
    public String time, message;
    public messageObj(String time, String message){
        this.time = time;
        this.message = message;
    }
}

