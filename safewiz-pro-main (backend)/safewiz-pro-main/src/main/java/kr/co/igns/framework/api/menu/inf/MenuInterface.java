package kr.co.igns.framework.api.menu.inf;

public interface MenuInterface {

	int getMenuId();
	String getMenuNm();		
	String getMenuNmCd();
	String getMenuEngNm();
	String getMenuIcon();
	String getRouterPath();
	String getGrpAuthCd();
	String getGrpAuthNm();
	String getRouterName();
	int getSort();
	int getLvl();
	String getSvcGubun();
	String getSvcType();		
	String getUseYn();
	String getParentId();
	Boolean getAuth();
	
}
