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
}