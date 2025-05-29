package com.kh.spring.transaction.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Category {
	private int categoryNo; //CATEGORY_NO	NUMBER
	private String categoryName; //CATEGORY_NAME	VARCHAR2(30 BYTE)
}
