package com.kh.spring.transaction.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class Product {
	private int productNo;//	PRODUCT_NO	NUMBER
	private String productName;//	PRODUCT_NAME	VARCHAR2(30 BYTE)
	private int categoryNo;//	CATEGORY_NO	NUMBER
}
