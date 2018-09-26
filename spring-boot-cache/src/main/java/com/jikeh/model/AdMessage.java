package com.jikeh.model;

public class AdMessage {

	/**
	 * 操作类型：1为新增，2是修改
	 */
	private int operation;

	/**
	 * 主键字段，值为具体的ID值
	 */
	private Long id;

    /**
     * 消息的唯一key：用于消息去重
     */
    private String uuidKey;

    /**
     * 广告信息：
     */
    private String content;

    public int getOperation() {
        return operation;
    }

    public void setOperation(int operation) {
        this.operation = operation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUuidKey() {
        return uuidKey;
    }

    public void setUuidKey(String uuidKey) {
        this.uuidKey = uuidKey;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}