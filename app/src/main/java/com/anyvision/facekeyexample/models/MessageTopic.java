package com.anyvision.facekeyexample.models;

public class MessageTopic {
    static String topic;
    static  String title;
    static String message;

    public MessageTopic(String topic, String title, String message){
        this.topic = topic;
        this.title = title;
        this.message = message;
    }

    public static String getTopic(){
        return topic;
    }

    public static String getTitle(){
        return title;
    }

    public static String getMessage(){
        return message;
    }
}
