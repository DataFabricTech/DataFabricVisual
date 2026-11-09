package com.mobigen.framework.service.file.model;

import lombok.Data;

@Data
public class DataRequestVO {
    private Integer id;
	private String writer;
	private String email;
	private String request_type;
	private String title;
	private String data_name;
	private String contents;
    private String request_start;
	private String request_end;
	private String benefit_content;
	private String create_date;
	private String update_date;
	private String process_status;
	private String finish_date;
}
