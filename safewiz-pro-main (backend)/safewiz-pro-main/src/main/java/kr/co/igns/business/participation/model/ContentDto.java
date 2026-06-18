package kr.co.igns.business.participation.model;

import lombok.Data;

@Data
public class ContentDto {
    private String docSeq;
    private String writeYear;
    private String docType;
    private String docNo;
    private String contentId;
    private Integer percent;
    private String ordSeq;
    private String content;
}
