package kr.co.igns.framework.api.menu.entity;

import java.io.Serializable;

import lombok.Data;


@SuppressWarnings("serial")
@Data
public class AuthPk implements Serializable {
	
	private String grpAuthCd;
	private int menuId;

}
