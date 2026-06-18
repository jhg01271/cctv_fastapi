package kr.co.igns.framework.config.closet;

/**
 * 
 * @ 프로젝트   	: igns
 * @ 패키지		: kr.co.igns.framework.config.closet
 * @ 파일명		: ClosetService.java
 * @ 기능명 		: 일반적인 POJO 객체, Bean으로 등록하려고 설정
 *                dev환경, prod환경 설정
 * @ 작성자 		: 김성준
 * @ 최초생성일 	: 2022. 4. 13.
 */

public class ClosetService {
	String name;
	
	public ClosetService(String name) {
		this.name = name;
	}
	
	public String getClosetName() {
		return "closet Name is [" + name + "]";
	}
}
