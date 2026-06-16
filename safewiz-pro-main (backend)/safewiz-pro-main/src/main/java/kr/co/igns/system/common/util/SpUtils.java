package kr.co.igns.system.common.util;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import kr.co.igns.framework.config.exception.CUserDefinedException;
import kr.co.igns.system.common.model.BaseVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.Condition;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.system.common.util
 * ※ 파일명 : SpUtils.java
 * ※ 기능명 :
 * ※ 작성자 : 임현섭
 * ※ 최초생성일 : 2024. 5. 8.
 * ※ 프로그램 설명 : 
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 *
 * </pre>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class SpUtils {

	//num만큼 string의 값에서 더하여 string으로 리턴
	public static String addNumToString(String string, int num) {
		// 문자열을 정수로 변환하고 +(num) 계산 후 문자열로 변환하여 반환
		int numericString = Integer.parseInt(string);
		return String.valueOf(numericString + num);
	}

	//yyyyMMdd -> yyyy년 MM월 dd일
	public static String formatDate1(String inputDate) {
		try {
			if(inputDate == null || inputDate.length() == 0){
				return inputDate;
			}
			// yyyyMMdd 형식으로 입력 날짜를 파싱
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			LocalDate date = LocalDate.parse(inputDate, inputFormatter);

			// yyyy년 MM월 dd일 형식으로 출력 포맷 지정
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일");
			return date.format(outputFormatter);
		} catch (DateTimeParseException e) {
			e.printStackTrace();
			return null; // 변환 실패 시 null 반환
		}
	}

	//yyyyMMdd -> MM월 dd일
	public static String formatDate2(String inputDate) {
		try {
			DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
			DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("MM월 dd일");

			LocalDate date = LocalDate.parse(inputDate, inputFormatter);
			return date.format(outputFormatter);
		} catch (DateTimeParseException e) {
			// 예외 처리: 유효하지 않은 날짜 형식
			System.out.println("Invalid date format: " + inputDate);
			return null;
		}
	}

	// NumberFormat을 사용하여 금액 형식으로 변환
	public static String formatCurrency1(int amount) {
		// NumberFormat을 사용하여 금액 형식으로 변환
		NumberFormat formatter = NumberFormat.getNumberInstance(Locale.getDefault());
		return formatter.format(amount);
	}

	//00:00 -> 00시 00분
	public static String formatTime1(String inputTime) {
		// 입력된 시간이 정확히 "HH:mm" 형식인지 확인
		if (inputTime == null || inputTime.length() != 5 || inputTime.charAt(2) != ':') {
			throw new IllegalArgumentException("Input time must be in HH:mm format");
		}

		// "HH" 부분과 "mm" 부분을 추출하여 원하는 형식으로 조합
		String hours = inputTime.substring(0, 2);
		String minutes = inputTime.substring(3, 5);
		return hours + "시 " + minutes + "분";
	}

	//ID 생성(공통) - yyyyMMdd0001(년월일순번)
	//maxId : 해당 테이블의 마지막으로 생성된 Id
	public static String createNewId(String maxId) {

		// 오늘 날짜를 "yyyyMMdd" 형식으로 포맷
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String todayStr = today.format(formatter);

		//maxId가 null일 경우 오늘 날짜 + "0001"
		if(maxId == null || maxId.isEmpty())
			return todayStr + "0001";

		// 기존 maxId에서 날짜와 순번을 추출
		String currentDatePart = maxId.substring(0, 8);
		String currentSeqPart = maxId.substring(8);

		String newId;

		if (todayStr.equals(currentDatePart)) {
			// 오늘 날짜와 일치하면 순번을 +1
			int newSeq = Integer.parseInt(currentSeqPart) + 1;
			String newSeqPart = String.format("%04d", newSeq); // 순번을 4자리로 포맷
			newId = todayStr + newSeqPart;
		} else {
			// 오늘 날짜에 데이터 없으면 -> 오늘 날짜 + "0001"
			newId = todayStr + "0001";
		}

		return newId;
	}
	
	
	//maxId : 해당 테이블의 마지막으로 생성된 Id
	public static String createNewFileId(String maxId) {

		// 오늘 날짜를 "yyyyMMdd" 형식으로 포맷
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String todayStr = today.format(formatter);

		//maxId가 null일 경우 오늘 날짜 + "0001"
		if(maxId == null || maxId.isEmpty())
			return todayStr + "000000000001";

		// 기존 maxId에서 날짜와 순번을 추출
		String currentDatePart = maxId.substring(0, 8);
		String currentSeqPart = maxId.substring(8);

		String newId;

		if (todayStr.equals(currentDatePart)) {
			// 오늘 날짜와 일치하면 순번을 +1
			int newSeq = Integer.parseInt(currentSeqPart) + 1;
			String newSeqPart = String.format("%012d", newSeq); // 순번을 4자리로 포맷
			newId = todayStr + newSeqPart;
		} else {
			// 오늘 날짜에 데이터 없으면 -> 오늘 날짜 + "0001"
			newId = todayStr + "000000000001";
		}

		return newId;
	}

	//#region FEMS 기존 함수

	private static final ObjectMapper objectMapper = new ObjectMapper();

	public static Object objectMap(Object sourceObj, Class<?> targetClazz) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		Object clazzObj = modelMapper.map(sourceObj, targetClazz);
		return clazzObj;
	}

	public static Object objectMap(Object sourceObj, BaseVO targetObj) throws Exception {
		ModelMapper modelMapper = new ModelMapper();

		// SourceObj가 null이 아닐 때만 매핑이 수행되도록 Condition 설정
		Condition<Object, Object> notNullCondition = new Condition<Object, Object>() {
			public boolean applies(MappingContext<Object, Object> context) {
				return context.getSource() != null;
			}
		};

		// 매핑 설정
		modelMapper.getConfiguration().setPropertyCondition(notNullCondition);

		modelMapper.map(sourceObj, targetObj);
		return targetObj;
	}

	//objectMap와 동일, null도 매핑
	public static Object objectMapNull(Object sourceObj, BaseVO targetObj) throws Exception {
		ModelMapper modelMapper = new ModelMapper();
		modelMapper.map(sourceObj, targetObj);
		return targetObj;
	}

	public static String getUuid() throws Exception {

		return getUuid(10);// 기본 10자리
	}

	public static String getUuid(int lng) throws Exception {

		UUID uuid = UUID.randomUUID();

		String result = uuid.toString().replace("-", "");
		result = result.substring(0, lng);

		return result;
	}

	public static ArrayNode setNodeTree(List<? extends BaseVO> voList, String key, String upKey) throws Exception {
		List<BaseVO> roots = new ArrayList<>();
		Map<Object, BaseVO> map = new HashMap<>();
		for (BaseVO vo : voList) {
			// key를 통해 getter 메서드 이름을 생성
			String getterMethodName = "get" + key.substring(0, 1).toUpperCase() + key.substring(1);
			Method getterMethod = vo.getClass().getMethod(getterMethodName);
			// getter 메서드를 호출하여 키 값을 얻음
			Object keyValue = getterMethod.invoke(vo);

			map.put(keyValue, vo);
		}

		for (BaseVO vo : voList) {
			// key를 통해 getter 메서드 이름을 생성
			String getterMethodName = "get" + upKey.substring(0, 1).toUpperCase() + upKey.substring(1);
			Method getterMethod = vo.getClass().getMethod(getterMethodName);
			// getter 메서드를 호출하여 키 값을 얻음
			Object targetKeyValue = getterMethod.invoke(vo);
			if (targetKeyValue == null || targetKeyValue == "0" || !map.containsKey(targetKeyValue)) {
				roots.add(vo);
			} else {
				BaseVO parent = map.get(targetKeyValue);
				Method getterChildMethod = parent.getClass().getMethod("getChildren");
				List<BaseVO> children = (List<BaseVO>) getterChildMethod.invoke(parent);
				children.add(vo);
			}
		}

		ArrayNode resultNode = objectMapper.createArrayNode();
		for (BaseVO root : roots) {
			addNodeToResult(resultNode, root);
		}
		return resultNode;
	}

	public static LocalDateTime parseDateTime(String dateTimeString, boolean isEndTime) throws Exception {
		try {
			return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		} catch (Exception e) {
			try {
				if (isEndTime) {
					dateTimeString += " 23:59:59";
				} else {
					dateTimeString += " 00:00:00";
				}
				return LocalDateTime.parse(dateTimeString, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

			} catch (Exception e2) {
				log.info("Invalid date format: " + dateTimeString);//지속 처리를 위해 throw 하지 않는다.
//				throw new IllegalArgumentException("Invalid date format: " + dateTimeString);
			}
		}
		return null;
	}

	// 순환 참조 체크
	public static void validateTree(ArrayNode tree, Object currentValue, Object newValue, String key) throws Exception {
		for (int i = 0; i < tree.size(); i++) {
			ObjectNode node = (ObjectNode) tree.get(i);
			// 선택된 노드
			if (node.has(key) && compareValues(node.get(key).asText(), currentValue)) {
				if (validateTreeHelper(node, newValue, key)) {
					throw new CUserDefinedException("Circular reference detected");
				}
			}
			// 하위 노드 검사
			if (node.has("_children")) {
				ArrayNode children = (ArrayNode) node.get("_children");
				validateTree(children, currentValue, newValue, key);
			}
		}
	}

	// BaseVo extends하여 사용
	private static void addNodeToResult(ArrayNode resultNode, BaseVO vo) throws Exception {
		ObjectNode node = objectMapper.createObjectNode();
		Method[] methods = vo.getClass().getDeclaredMethods();
		for (Method method : methods) {
			// getter 메서드인지 확인
			if (method.getName().startsWith("get") && method.getParameterCount() == 0) {
				// 메서드에서 속성 이름 가져오기
				String propertyName = method.getName().substring(3);
				propertyName = propertyName.substring(0, 1).toLowerCase() + propertyName.substring(1);

				// getter 메서드 호출
				Object value = method.invoke(vo);

				// enum class extends 하는 경우 처리
//					if (value != null && value instanceof Enum) {
//						Method getKeyMethod = value.getClass().getMethod("getKey");
//						value = getKeyMethod.invoke(value);
//					}

				// 노드에 값 추가
					if (!propertyName.equals("children"))
						node.putPOJO(propertyName, value);
			}
		}
		Method getterMethod = vo.getClass().getMethod("getChildren");
		List<BaseVO> children = (List<BaseVO>) getterMethod.invoke(vo);
		if (children != null && !children.isEmpty()) {
			ArrayNode childrenNode = objectMapper.createArrayNode();
			for (BaseVO child : children) {
				addNodeToResult(childrenNode, child);
			}
			node.set("_children", childrenNode);
		}
		resultNode.add(node);
	}

	private static boolean validateTreeHelper(ObjectNode node, Object newValue, String key) throws Exception {
		// 하위 노드의 key의 value와 같을 경우 true 반환
		if (node.has(key) && compareValues(node.get(key).asText(), newValue)) {
			return true;
		}
		if (node.has("_children")) {
			ArrayNode children = (ArrayNode) node.get("_children");
			for (int i = 0; i < children.size(); i++) {
				if (validateTreeHelper((ObjectNode) children.get(i), newValue, key)) {
					return true;
				}
			}
		}
		return false;
	}

	// 삭제 여부에 따라 트리 재구성
	public static void treeFilter(ArrayNode tree, String yn) {
		String delYn = yn.equals("Y") ? "N" : "Y";
		if (yn.equals("N")) {
			removeDeleteNodes(tree, delYn);
		} else if (yn.equals("Y")) {
			removeNonDeleteNodes(tree, delYn);
		}
	}

	// 삭제 노드 제거 (하위 노드 함께 제거)
	private static void removeDeleteNodes(ArrayNode tree, String yn) {
		String key = "delYn";
		for (int i = tree.size() - 1; i >= 0; i--) {
			ObjectNode node = (ObjectNode) tree.get(i);

			if (node.has(key) && node.get(key).asText().equals(yn)) {
				// 노드와 그 하위 노드 삭제
				removeNodeAndDescendants(tree, i);
			} else {
				// 하위 노드를 재귀적으로 처리
				if (node.has("_children")) {
					ArrayNode children = (ArrayNode) node.get("_children");
					removeDeleteNodes(children, yn);

					// 모든 하위 노드가 삭제된 경우 _children 필드도 삭제
					if (children.size() == 0) {
						node.remove("_children");
					}
				}
			}
		}
	}

	// 미삭제 노드 제거 (삭제 하위 노드는 제거하지 않음)
	private static void removeNonDeleteNodes(ArrayNode tree, String yn) {
		String key = "delYn";
		for (int i = 0; i < tree.size(); i++) {
			ObjectNode node = (ObjectNode) tree.get(i);
			if (node.has(key) && node.get(key).asText().equals(yn)) {
				// 노드를 제거하고 하위 노드를 상위로 올림
				if (node.has("_children")) {
					ArrayNode children = (ArrayNode) node.get("_children");
					// 하위 노드가 모두 삭제된 경우 _children 필드도 삭제
					if (children.size() == 0) {
						node.remove("_children");
					} else {
						// 하위 노드를 상위로 올림
						for (int j = 0; j < children.size(); j++) {
							tree.insert(i + 1, children.get(j));
						}
					}
				}
				tree.remove(i--);
			}
		}
	}

	private static void removeNodeAndDescendants(ArrayNode tree, int index) {
		ObjectNode nodeToRemove = (ObjectNode) tree.remove(index);
		// 삭제된 노드의 하위 노드를 재귀적으로 삭제
		if (nodeToRemove.has("_children")) {
			ArrayNode children = (ArrayNode) nodeToRemove.get("_children");
			for (int i = children.size() - 1; i >= 0; i--) {
				removeNodeAndDescendants(children, i);
			}
		}
	}

	private static boolean compareValues(String nodeValue, Object compareValue) throws Exception {
		if (compareValue instanceof String) {
			return nodeValue.equals(compareValue);
		} else if (compareValue instanceof Long) {
			return Long.parseLong(nodeValue) == (Long) compareValue;
		}
		// 필요한 경우 다른 타입도 추가할 수 있습니다.
		return false;
	}

// #endregion
}
