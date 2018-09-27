package com.jikeh.model;

import com.alibaba.fastjson.annotation.JSONField;

public class Ad extends SerializableDTO {

    private static final long serialVersionUID = 107359365546366356L;

    private Long id;
    private String name;
    private Long price;

    @JSONField(name = "destination_url")
    private String destinationUrl;

    @JSONField(name = "img_url")
    private String imgUrl;

    @JSONField(name = "start_time")
    private Long startTime;

    @JSONField(name = "end_time")
    private Long endTime;

    @JSONField(name = "update_time")
    private String updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public String getDestinationUrl() {
        return destinationUrl;
    }

    public void setDestinationUrl(String destinationUrl) {
        this.destinationUrl = destinationUrl;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }
}