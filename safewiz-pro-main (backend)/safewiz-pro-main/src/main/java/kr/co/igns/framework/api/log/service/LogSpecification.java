package kr.co.igns.framework.api.log.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import kr.co.igns.framework.api.log.entity.LogEntity;

public class LogSpecification {

	public static Specification<LogEntity> searchLog(Map<String, Object> searchKey) {
		return ((root, query, criteriaBuilder) -> {
			List<Predicate> predicates = new ArrayList<>();
			for(String key : searchKey.keySet()){
				predicates.add(criteriaBuilder.equal(root.get(key), searchKey.get(key)));
			}
			return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		});
	}
	
	@SuppressWarnings("serial")
	public static Specification<LogEntity> betweenLogDate(String startDatetime, String endDatetime) {
        return new Specification<LogEntity>() {
            @Override
            public Predicate toPredicate(Root<LogEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                // 3) between
                return criteriaBuilder.between(root.get("logDate"), startDatetime, endDatetime);
            }
        };
    }
}
