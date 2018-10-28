package com.jikeh.model;

/**
 * 广告信息
 * @author 极客慧 www.jikeh.cn
 *
 */
public class AdInfo {

	private Long id;
	private String name;
	private String destination_url;
	private Long price;
	private String img_url;
	private String startTime;
	private String endTime;

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

	public String getDestination_url() {
		return destination_url;
	}

	public void setDestination_url(String destination_url) {
		this.destination_url = destination_url;
	}

	public Long getPrice() {
		return price;
	}

	public void setPrice(Long price) {
		this.price = price;
	}

	public String getImg_url() {
		return img_url;
	}

	public void setImg_url(String img_url) {
		this.img_url = img_url;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
}
