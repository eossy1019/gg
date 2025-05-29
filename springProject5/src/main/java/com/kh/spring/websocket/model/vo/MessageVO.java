package com.kh.spring.websocket.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MessageVO {
	private String userId;
	private String msg;
	private String currentTime;
}
