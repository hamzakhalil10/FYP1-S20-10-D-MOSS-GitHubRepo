package com.example.moss;

public class ClassAlert {
    private String image;
    private String time;
    private String type;
    private String received;

    public ClassAlert() {

    }

    public ClassAlert(String image, String time, String type, String received) {
        this.image = image;
        this.time = time;
        this.type = type;
        this.received = received;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getReceived() {
        return received;
    }

    public void setReceived(String received) {
        this.received = received;
    }
}
