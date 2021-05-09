package com.alibenalihospital.models;

import java.io.Serializable;

public class NotificationModel implements Serializable {
    private int id;
    private int notification_info_id;
    private String user_type;
    private int from_user_id;
    private int to_user_id;
    private String title;
    private String message;
    private String image;
    private String notification_type;
    private String action_type;
    private String is_read;
    private String created_at;
    private String updated_at;

    public int getId() {
        return id;
    }

    public int getNotification_info_id() {
        return notification_info_id;
    }

    public String getUser_type() {
        return user_type;
    }

    public int getFrom_user_id() {
        return from_user_id;
    }

    public int getTo_user_id() {
        return to_user_id;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public String getNotification_type() {
        return notification_type;
    }

    public String getAction_type() {
        return action_type;
    }

    public String getIs_read() {
        return is_read;
    }

    public String getCreated_at() {
        return created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getImage() {
        return image;
    }
}
