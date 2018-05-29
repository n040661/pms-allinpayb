package com.dominator.utils.excel;

/**
 * 导出配置
 * 
 * @author gsh
 *
 */
public class ColumonCfg {
	String title;
	String fieldName;
	Integer width;

	public ColumonCfg(String fieldName, String title, Integer width) {
		this.title = title;
		this.fieldName = fieldName;
		this.width = width;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public Integer getWidth() {
		return width;
	}

	public void setWidth(Integer width) {
		this.width = width;
	}

}
