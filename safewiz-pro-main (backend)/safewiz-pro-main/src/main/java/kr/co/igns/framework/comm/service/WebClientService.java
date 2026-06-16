package kr.co.igns.framework.comm.service;

import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

/**
 * @ 프로젝트       : ESG
 * @ 패키지          : kr.co.igns.framework.comm.service
 * @ 파일명          : WebClientService.java
 * @ 기능명          :
 * @ 작성자          : 소민환
 * @ 최초생성일     : 
 * @ 프로그램 설명  : 
 * ■ 변경이력 (ex : [YYYY-MM-DD] 변경내용 - 수정자)
 *    [2024-01-31] 소민환 - WebClient api Service 추가
 *    [2024-12-04] 이지훈 - 세이프위즈 pro 이관
 */

@Service
@RequiredArgsConstructor
public class WebClientService {
	private static final Logger log = LogManager.getLogger("com.project");
	/**
	 * method: WebClient 버퍼 크기 증가
	 */
    private WebClient.Builder webClientBuilder() {
        return WebClient.builder()
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024)); // 100MB
    }
	/**
	 * method : webClientPostTopicApi (비동기식, fcm topic 전송 사용)
	 *
	 * @param apiUrl, authorizationToken, data
	 * @return void
	 * @throws RuntimeException
	 */ 
    public void webClientPostTopicApi(String apiUrl, String authorizationToken, Object data) {
        WebClient webClient = webClientBuilder().baseUrl(apiUrl).build();

        webClient.method(HttpMethod.POST)
                .uri(uriBuilder -> uriBuilder.build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authorizationToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(data))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                	System.out.println("에러 코드 값 --> " + response.statusCode().value());
               
                    return response.bodyToMono(String.class)
                            .doOnNext(errorMessage -> log.error("4xx Client Error: " + errorMessage))
                            .then(Mono.empty()); // 로그 기록 후 Mono.empty를 반환하여 오류 처리를 완료
                })
                .onStatus(HttpStatus::is5xxServerError, response -> {
                    return response.bodyToMono(String.class)
                            .doOnNext(errorMessage -> log.error("5xx Server Error: " + errorMessage))
                            .then(Mono.empty()); // 로그 기록 후 Mono.empty를 반환하여 오류 처리를 완료
                })
                .toBodilessEntity()
                .doOnNext(responseBody -> log.info("통신 응답 결과 --> " + responseBody))
                .onErrorResume(e -> {
                    // 에러 처리 후 정상적인 Mono<Void> 반환
                    log.error("WebClient 통신 중 에러 발생: " + e.getMessage());
                    return Mono.empty(); // 오류가 발생해도 이후 로직에 영향을 주지 않고 계속 진행하도록 함
                })
                .block();
    }
	/**
	 * method : webClientPostTokenApi (비동기식, fcm token 전송 사용)
	 *
	 * @param apiUrl, authorizationToken, data
	 * @return void
	 * @throws RuntimeException
	 */ 
    public void webClientPostTokenApi(String apiUrl, String authorizationToken, String userCd, Object data) {
        WebClient webClient = webClientBuilder().baseUrl(apiUrl).build();

        webClient.method(HttpMethod.POST)
                .uri(uriBuilder -> uriBuilder.build())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + authorizationToken)
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(BodyInserters.fromValue(data))
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    if (response.statusCode() == HttpStatus.NOT_FOUND || response.statusCode() == HttpStatus.BAD_REQUEST) {
                    }
                    return response.bodyToMono(String.class)
                            .doOnNext(errorMessage -> log.error("4xx Client Error: " + errorMessage))
                            .then(Mono.empty()); // 로그 기록 후 Mono.empty를 반환하여 오류 처리를 완료
                })
                .onStatus(HttpStatus::is5xxServerError, response -> {
                    return response.bodyToMono(String.class)
                            .doOnNext(errorMessage -> log.error("5xx Server Error: " + errorMessage))
                            .then(Mono.empty()); // 로그 기록 후 Mono.empty를 반환하여 오류 처리를 완료
                })
                .toBodilessEntity()
                .doOnNext(responseBody -> log.info("통신 응답 결과 --> " + responseBody))
                .onErrorResume(e -> {
                    // 에러 처리 후 정상적인 Mono<Void> 반환
                    log.error("WebClient 통신 중 에러 발생: " + e.getMessage());
                    return Mono.empty(); // 오류가 발생해도 이후 로직에 영향을 주지 않고 계속 진행하도록 함
                })
                .block();
    }
	/**
	 * method : webClientGetApi (동기식)
	 * 
	 * @param: String, MultiValueMap, Map, Class 
	 * @return T
	 * @throws
	 */
    public <T> T webClientGetApi(String apiUrl, MultiValueMap<String, String> params, Map<String, String> headers, Class<T> responseType) {
        WebClient webClient = webClientBuilder().baseUrl(apiUrl).build();

        if (headers != null && !headers.isEmpty()) {
            headers.forEach(webClientBuilder()::defaultHeader);
        }

        return webClient.method(HttpMethod.GET)
                .uri(uriBuilder -> uriBuilder.queryParams(params).build())
                .accept(MediaType.APPLICATION_JSON)
                .headers(httpHeaders -> httpHeaders.setAll(headers)) 
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, response -> {
                    log.info(response.statusCode() + " 에러");
                    throw new RuntimeException("Client Error");
                })
                .onStatus(HttpStatus::is5xxServerError, response -> {
                    log.info(response.statusCode() + " 에러");
                    throw new RuntimeException("Internal Server Error");
                })
                .bodyToMono(responseType)
                .doOnError(error -> log.info("error --> " + error))
                .doOnSuccess(responseBody -> log.info("responseBody --> " + responseBody))
                .block();
    }
}