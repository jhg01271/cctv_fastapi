package kr.co.igns.business.planning.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;

import lombok.Data;

@Data
@XmlAccessorType(XmlAccessType.FIELD)
public class ChemDetailVO {
	@XmlElementWrapper(name = "items")
	@XmlElement(name = "item")
	private List<Item> items;
	
	@Data
	@XmlAccessorType(XmlAccessType.FIELD)
	private static class Item {
		private String apiOperationValue;
		private String apiOperationNotes;
		
		private String lev;
		private String msdsItemCode;
		private String upMsdsItemCode;
		private String msdsItemNameKor;
		private String msdsItemNo;
		private String ordrIdx;
		private String itemDetail;
	}
}
