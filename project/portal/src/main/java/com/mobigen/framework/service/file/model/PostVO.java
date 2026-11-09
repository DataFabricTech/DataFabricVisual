package com.mobigen.framework.service.file.model;

import lombok.Data;

@Data
public class PostVO {
	private Integer id;
	private String post_type;
	private String writer;
	private String email;
	private String title;
	private String data_name;
	private String post_content;
	private String file_link;
	private String process_status;
	private String benefit_content;
	private String request_start;
	private String request_end;
	private Integer view_count;
	private String category;
    private Boolean is_open;
	private String admin_reply;
}
