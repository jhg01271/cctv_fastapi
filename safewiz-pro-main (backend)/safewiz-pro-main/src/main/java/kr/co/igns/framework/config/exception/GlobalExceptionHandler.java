package kr.co.igns.framework.config.exception;

import kr.co.igns.framework.config.exception.CMailSenderException.*;
import kr.co.igns.framework.config.log.LogUtil;
import kr.co.igns.framework.config.message.MessageService;
import kr.co.igns.framework.config.response.ResponseUtil;
import kr.co.igns.framework.config.response.SingleResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.dao.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.InvalidResultSetAccessException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.zip.ZipException;

/**
 * <pre>
 * ※ 프로젝트 : sp_backend
 * ※ 패키지 : kr.co.igns.framework.config.exception
 * ※ 파일명 : GlobalExceptionHandler.java
 * ※ 기능명 :
 * ※ 작성자 :
 * ※ 최초생성일 : 2024. 5. 17.
 * ※ 프로그램 설명 :
 * ■ 변경이력(ex : [YYYY-MM-DD] 변경 내용 - 수정자)
 * 2024-05-17 MethodArgumentNotValidException 추가 - 임현섭
 * </pre>
 */
@RequiredArgsConstructor
@RestControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class GlobalExceptionHandler {

    private final MessageService messageService;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<SingleResponseDto<Object>> processValidationError(MethodArgumentNotValidException exception) {
        BindingResult bindingResult = exception.getBindingResult();

        StringBuilder builder = new StringBuilder();
        for (FieldError fieldError : bindingResult.getFieldErrors()) {
            builder.append("[");
            builder.append(fieldError.getField());
            builder.append("](은)는 ");
            builder.append(fieldError.getDefaultMessage());
            builder.append(" 입력된 값: \"");
            builder.append(fieldError.getRejectedValue());
            builder.append("\"");
        }

        return ResponseUtil.SingleResponse(HttpStatus.BAD_REQUEST, false, messageService.getMessage("eNotFound.code"), builder.toString(), null);
    }

    @ExceptionHandler(CPrintConnectException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<SingleResponseDto<Object>> handlePrintConnectException(HttpServletRequest request, CNotFoundException ex) {
        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Print_Connect_Exception", ex, request);
        return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("ePrintConnect.code"), messageService.getMessage("ePrintConnect.msg"), null);
    }

    @ExceptionHandler(CNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<SingleResponseDto<Object>> handleNotFoundException(HttpServletRequest request, CNotFoundException ex) {
        LogUtil.ConsoleLogError(HttpStatus.BAD_REQUEST, "Not_Found_Exception", ex, request);
        return ResponseUtil.SingleResponse(HttpStatus.BAD_REQUEST, false, messageService.getMessage("eNotFound.code"), messageService.getMessage("eNotFound.msg"), null);
    }

    @ExceptionHandler(CNullPointException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<SingleResponseDto<Object>> handleNullPointException(HttpServletRequest request, CNullPointException ex) {
        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Null_Point_Exception", ex, request);
        return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eNullPoint.code"), messageService.getMessage("eNullPoint.msg"), null);
    }

    @ExceptionHandler(CIndexBoundException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<SingleResponseDto<Object>> handleIndexBoundException(HttpServletRequest request, CIndexBoundException ex) {
        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IndexOfBounding_Exception", ex, request);
        return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eIndexOfBounding.code"), messageService.getMessage("eIndexOfBounding.msg"), null);
    }

    @ExceptionHandler(CEmailValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<SingleResponseDto<Object>> handleEmailValidationException(HttpServletRequest request, CEmailValidationException ex) {
        LogUtil.ConsoleLogError(HttpStatus.BAD_REQUEST, "Email_Validation_Exception", ex, request);
        return ResponseUtil.SingleResponse(HttpStatus.BAD_REQUEST, false, messageService.getMessage("emailValidation.code"), messageService.getMessage("emailValidation.msg"), null);
    }

    @ExceptionHandler(CDataAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<SingleResponseDto<Object>> handleDataAccessFailException(HttpServletRequest request, CDataAccessException ex) {
        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Data_Access_Exception", ex, request);
        return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eDataAccessFail.code"), ex.getMessage(), null);
    }

    @ExceptionHandler(CDateTimeFormatException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<SingleResponseDto<Object>> handleDateTimeFormatException(HttpServletRequest request, CDateTimeFormatException ex) {
        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Datetime_format_Exception", ex, request);
        return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eDateTimeFormatFail.code"), messageService.getMessage("eDateTimeFormatFail.msg"), null);
    }

    @ExceptionHandler(CDataFormatException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<SingleResponseDto<Object>> handleDataFormatException(HttpServletRequest request, CDataFormatException ex) {
        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Data_Format_Exception", ex, request);
        return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eDataFormat.code"), messageService.getMessage("eDataFormat.msg"), null);
    }

    @ExceptionHandler(CJsonSyntaxException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<SingleResponseDto<Object>> handleJsonSyntaxException(HttpServletRequest request, CJsonSyntaxException ex) {
        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Json_Syntax_Exception", ex, request);
        return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eJsonSyntax.code"), messageService.getMessage("eJsonSyntax.msg"), null);
    }

    @ExceptionHandler(CUserExistException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<SingleResponseDto<Object>> handleJsonSyntaxException(HttpServletRequest request, CUserExistException ex) {
        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "AA", ex, request);
        return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "-1005", "jjjjjj", null); // FIXME
    }


    @ExceptionHandler(CTokenValicationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<SingleResponseDto<Object>> handleCTokenValicationException(HttpServletRequest request, CTokenValicationException ex) {
        LogUtil.ConsoleLogError(HttpStatus.UNAUTHORIZED, "토큰이 없거나 잘 못 되었습니다.", ex, request);
        return ResponseUtil.SingleResponse(HttpStatus.UNAUTHORIZED, false, "-1100", "토큰이 없거나 잘 못 되었습니다.", null);
    }

    @ExceptionHandler(CUserDefinedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<SingleResponseDto<Object>> handleCUserDefinedException(HttpServletRequest request, CUserDefinedException e) {
        System.out.println("##############################");
        System.out.println("# CUserDefinedException => " + e);
        System.out.println("##############################");
        LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "CUserDefinedException", e, request);
        return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, "500", e.getMessage() +"\n"+e.toString(), e);        
    }


    @ExceptionHandler(CMailSenderException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<SingleResponseDto<Object>> handleMailSenderException(HttpServletRequest request, CMailSenderException e) {
        e.printStackTrace();
        if (e instanceof CMailAuthenticationException) { // 메일 서버 인증 에러
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Mail_Authentication_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eMailAuthentication.code"), messageService.getMessage("eMailAuthentication.msg"), null);
        } else if (e instanceof CMailParseException) { // 메일 파싱 에러
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Mail_Parse_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eMailParse.code"), messageService.getMessage("eMailParse.msg"), null);
        } else if (e instanceof CMailPreparationException) { // 메시지 구성 중 메일 서버 통신 에러
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Mail_Preparation_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eMailPreparation.code"), messageService.getMessage("eMailPreparation.msg"), null);
        } else if (e instanceof CMailSendException) { // 메일 전송 에러
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Mail_Send_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eMailSend.code"), messageService.getMessage("eMailSend.msg"), null);
        } else if (e instanceof CMessagingException) { // 자바 메일 API 에러
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Messaging_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eMailMessaging.code"), messageService.getMessage("eMailMessaging.msg"), null);
        } else {
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Mail_Sender_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eMailSend.code"), messageService.getMessage("eMailSend.msg"), null);
        }
    }

    @ExceptionHandler(IOException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<SingleResponseDto<Object>> handleGenericIOException(HttpServletRequest request, IOException e) {
        e.printStackTrace();
        if (e instanceof FileNotFoundException) { // 파일을 찾을 수 없을 때 발생
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "File_Not_Found_Exception", e.getMessage(), request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eFileNotFound.code"), messageService.getMessage("eFileNotFound.msg"), null);
        } else if (e instanceof EOFException) { // 입력 스트림이 끝에 도달했을 때 발생하는 예외. 데이터를 읽을 때 더 이상 읽을 데이터가 없을 때 발생
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "EOF_Exception", e.getMessage(), request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eEOF.code"), messageService.getMessage("eEOF.msg"), null);
        } else if (e instanceof InterruptedIOException) { //
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Interrupted_IOException", e.getMessage(), request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eInterruptedIO.code"), messageService.getMessage("eInterruptedIO.msg"), e);
        } else if (e instanceof CharConversionException) { //
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Char_Conversion_Exception", e.getMessage(), request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eCharConversion.code"), messageService.getMessage("eCharConversion.msg"), e);
        } else if (e instanceof UnsupportedEncodingException) { //
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Unsupported_Encoding_Exception", e.getMessage(), request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eUnsupportedEncoding.code"), messageService.getMessage("eUnsupportedEncoding.msg"), e);
        } else if (e instanceof SyncFailedException) { //
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Sync_Failed_Exception", e.getMessage(), request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eSyncFailed.code"), messageService.getMessage("eSyncFailed.msg"), e);
        } else if (e instanceof ObjectStreamException) { //
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Object_Stream_Exception", e.getMessage(), request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eObjectStream.code"), messageService.getMessage("eObjectStream.msg"), e);
        } else if (e instanceof ZipException) { //
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Zip_Exception", e.getMessage(), request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eZip.code"), messageService.getMessage("eZip.msg"), e);
        } else if (e instanceof SocketException) { //
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Socket_Exception", e.getMessage(), request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eSocket.code"), messageService.getMessage("eSocket.msg"), e);
        } else if (e instanceof MalformedURLException) { // URL 형식이 잘못되었음
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Malformed_URL_Exception", e.getMessage(), request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eMalformedURL.code"), messageService.getMessage("eMalformedURL.msg"), e);
        } else if (e instanceof ConnectException) { // URL 연결 실패
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Connect_Exception", e.getMessage(), request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eConnectError.code"), messageService.getMessage("eConnectError.msg"), e);
        } else {
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "IOException", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("unKnown.code"), messageService.getMessage("unKnown.msg") +"\n"+e.toString(), e);
        }
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<SingleResponseDto<Object>> handleGenericException(HttpServletRequest request, Exception e) {
        e.printStackTrace();
        // SQL 문법 및 데이터 접근 관련 예외
        if (e instanceof BadSqlGrammarException) { // SQL 오류처리
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "BadSql_Grammar_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eBadSqlGrammar.code"), messageService.getMessage("eBadSqlGrammar.msg") + ": " + e.getClass().getName(), e);
        } else if (e instanceof InvalidDataAccessResourceUsageException) { // SQL 문법 오류
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid_Data_Access_Resource_Usage_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eInvalidDataAccessResourceUsage.code"), messageService.getMessage("eInvalidDataAccessResourceUsage.msg") + ": " + e.getClass().getName(), e);
        } else if (e instanceof InvalidResultSetAccessException) { // 데이터베이스 결과 집합 처리 오류 
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid_ResultSet_Access_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eInvaildResult.code"), messageService.getMessage("eInvaildResult.msg") + ": " + e.getClass().getName(), e);
        } else if (e instanceof DataRetrievalFailureException) { // 데이터베이스 데이터를 검색하지 못함
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Data_Retrieval_Failure_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eDataRetrievalFailure.code"), messageService.getMessage("eDataRetrievalFailure.msg") + ": " + e.getClass().getName(), e);
        }
        // 무결성 및 키 제약
        else if (e instanceof DuplicateKeyException) { // 데이터베이스 고유 키 제약 또는 고유 인덱스 위반
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Duplicate_Key_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eDuplicateKey.code"), messageService.getMessage("eDuplicateKey.msg") + ": " + e.getClass().getName(), e);
        } else if (e instanceof DataIntegrityViolationException) {// 데이터 무결성 위반
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Data_Integrity_Violation_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eDataIntegrityViolation.code"), messageService.getMessage("eDataIntegrityViolation.msg") + ": " + e.getClass().getName(), e);
        }
        // 데이터 접근 유형
        else if (e instanceof TransientDataAccessException) { // 일시적인 데이터 엑세스 문제
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Transient_Data_Access_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eTransientDataAccess.code"), messageService.getMessage("eTransientDataAccess.msg") + ": " + e.getClass().getName(), e);
        } else if (e instanceof NonTransientDataAccessException) { // 구조적인 데이터베이스 엑세스 문제
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Non_Transient_Data_Access_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eNonTransientDataAccess.code"), messageService.getMessage("eNonTransientDataAccess.msg") + ": " + e.getClass().getName(), e);
        } else if (e instanceof DataAccessResourceFailureException) { // 데이터 액세스 리소스가 완전히 실패했습니다 (예 : 데이터베이스에 연결할 수 없음)
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Data_Access_Resource_Failure_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eDataAccessResourceFailure.code"), messageService.getMessage("eDataAccessResourceFailure.msg") + ": " + e.getClass().getName(), e);
        }
        // 트랜잭션 관련 예외
        else if (e instanceof OptimisticLockingFailureException) {
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Optimistic_Locking_Failure_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eOptimisticLocking.code"), messageService.getMessage("eOptimisticLocking.msg") + ": " + e.getClass().getName(), e);
        } else if (e instanceof ConcurrencyFailureException) { // 트랜잭션 병행 처리 에러
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Concurrency_Failure_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eConcurrencyFailure.code"), messageService.getMessage("eConcurrencyFailure.msg") + ": " + e.getClass().getName(), e);
        } else if (e instanceof CannotAcquireLockException) { // 데이터베이스 테이블에 Rock이 걸려있는 경우, 여러 트랙잭션간 충돌
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot_Acquire_Lock_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eCannotAcquireLock.code"), messageService.getMessage("eCannotAcquireLock.msg") + ": " + e.getClass().getName(), e);
        } else if (e instanceof DeadlockLoserDataAccessException) {// 데드락(Deadlock)이 발생하여 트랜잭션이 실패한 경우
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Deadlock_Loser_Data_Access_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eDeadlockLoserDataAccess.code"), messageService.getMessage("eDeadlockLoserDataAccess.msg") + ": " + e.getClass().getName(), e);
        } else if (e instanceof CannotSerializeTransactionException) {// 직렬화 가능한 트랜잭션에 실패
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Cannot_Serialize_Transaction_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eCannotSerializeTransaction.code"), messageService.getMessage("eCannotSerializeTransaction.msg") + ": " + e.getClass().getName(), e);
        }
        // 권한/보안
        else if (e instanceof PermissionDeniedDataAccessException) { // 데이터 액세스 권한 부족
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Permission_Denied_Data_Access_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("ePermissionDeniedDataAccess.code"), messageService.getMessage("ePermissionDeniedDataAccess.msg") + ": " + e.getClass().getName(), e);
        }
        // 기타 (암호화 관련)
        else if (e instanceof InvalidKeyException) { // 암호화와 관련된 작업에서 발생하는 예외
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Invalid_Key_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eInvalidKey.code"), messageService.getMessage("eInvalidKey.msg") + ": " + e.getClass().getName(), e);
        } else if (e instanceof NoSuchAlgorithmException) {
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "No_Such_Algorithm_Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("eNoSuchAlgorithm.code"), messageService.getMessage("eNoSuchAlgorithm.msg") + ": " + e.getClass().getName(), e);
        }
        // 잘못된 인자
        else if (e instanceof IllegalArgumentException) {
            LogUtil.ConsoleLogError(HttpStatus.BAD_REQUEST, "IllegalArgumentException", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.BAD_REQUEST, false, messageService.getMessage("eIllegalArgument.code"), messageService.getMessage("eIllegalArgument.msg") + ": " + e.getClass().getName(), e);
        }
        // 포괄 예외
        else {
            LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "Exception", e, request);
            return ResponseUtil.SingleResponse(HttpStatus.INTERNAL_SERVER_ERROR, false, messageService.getMessage("unKnown.code"), messageService.getMessage("unKnown.msg") + "\n" + e.toString(), e);
        }
    }
}
