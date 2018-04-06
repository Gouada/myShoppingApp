package com.example.gouadadopavogui.myshoppingapp.helpers;

/**
 * Created by GouadaDopavogui on 24.02.2017.
 */

public class MessageProvider {

    public static String messageCode;
    public static String message;
    public MessageProvider() {
    }

    public MessageProvider(String messageCode, String message) {
        this.messageCode = messageCode;
        this.message = message;
    }

    public String getMessageCode() {
        return messageCode;
    }

    public void setMessageCode(String errorCode) {
        this.messageCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String errorMessage) {
        this.message = errorMessage;
    }
}
