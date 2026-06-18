//package kr.co.igns.framework.api.terms.service;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import org.springframework.stereotype.Service;
//
//import kr.co.igns.framework.api.terms.dto.TermsResponseDto;
//import kr.co.igns.framework.api.terms.entity.TermsEntity;
//import kr.co.igns.framework.api.terms.entity.TermsRepository;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class TermsService {
//
//	private final TermsRepository termsRepository;
//	
//	/*
//	 * 약관 조회
//	 */
//	public List<TermsResponseDto> getTerms(String type, String serviceType) {		
//		List<TermsResponseDto> resultList = new ArrayList<TermsResponseDto>();
//		List<TermsEntity> entity = null;
//		if (type.equals("All"))
//			entity = termsRepository.findAll();
//		else {
//			entity = termsRepository.findByTypeAndServiceType(type, serviceType);
//		}
//		if(!entity.isEmpty()) {
//			for(TermsEntity tmp : entity) {
//				TermsResponseDto dto = new TermsResponseDto(tmp);
//				resultList.add(dto);
//			}
//		}
//		return resultList;
//	}
//}
