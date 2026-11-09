package com.mobigen.framework.service.file.model;

import lombok.Data;

@Data
public class ShareVO {
    private Integer id;
	private String writer;
	private String title;
	private String contents;
	private String create_date;
	private String update_date;
}
