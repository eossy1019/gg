package com.kh.spring.transaction.model.service;

import java.util.ArrayList;

import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.kh.spring.transaction.model.vo.Category;
import com.kh.spring.transaction.model.vo.Product;

@Service
public class TestServiceImpl {

	
	@Autowired
	private SqlSessionTemplate sqlSession;
	
	
	//해당 메소드에서 동작하는 트랜잭션은 하나로 묶어서 처리하도록 설정해주는 어노테이션
	@Transactional
	public void test01() {
		//테스트를 이용하여 데이터 추가해보기 
		
		//과자류 
		Category c = new Category(1,"과자");
		Product p = new Product(1,"홈런볼",1);
		
		//취미류
		Category c2 = new Category(2,"취미");
		Product p2 = new Product(1,"닌텐도",5);
		
		
		int result = sqlSession.insert("testMapper.insertCategory",c2);
		
		int result2 = sqlSession.insert("testMapper.insertProduct",p2);
		
		System.out.println("카테고리 결과값 : "+result);
		System.out.println("프로덕트 결과값 : "+result2);
		
		
		//spring에서 트랜잭션관리를 하기 때문에 임의로 트랜잭션 처리를 할 수 없다.
//		if(result*result2>0) {
//			sqlSession.commit();
//		}else {
//			sqlSession.rollback();
//		}
		
		ArrayList<Category> cList = (ArrayList)sqlSession.selectList("testMapper.selectCList");
		
		ArrayList<Product> pList = (ArrayList)sqlSession.selectList("testMapper.selectPList");
		
		for(Category category : cList) {
			System.out.println(category);
		}
		
		System.out.println();
		
		for(Product product : pList) {
			System.out.println(product);
		}
	}
	
	
}
