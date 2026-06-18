package kr.co.igns.business.planning.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ChemSearchVO {
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<Item> items;
	private Integer numOfRows;
	private Integer pageNo;
	private Integer totalCount;
	
	@Data
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Item {
		private String casNo;
		private String chemId;
		private String chemNameKor;
		private String enNo;
		private String keNo;
		private String unNo;
		private String lastDate;
	}
}
