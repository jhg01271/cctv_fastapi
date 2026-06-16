package kr.co.igns.business.planning.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class LegalSearchVO {
    private ItemsWrapper items;
    private Integer numOfRows;
    private Integer pageNo;
    private Integer totalCount;
    private String latestDt;

    @Data
    public static class ItemsWrapper {
        private List<Item> item;
    }

    @Data
    public static class Item {
        private String category;
        private String content;

        @JsonProperty("doc_id")
        private String refId;

        private String title;

    }

}



