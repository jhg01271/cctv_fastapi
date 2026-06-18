package kr.co.igns.framework.config.security.jwt.util;

import kr.co.igns.framework.config.log.LogUtil;
import org.springframework.http.HttpStatus;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Random;

public class PasswordUtil {

    public static String generateRandomPassword() {
        int index = 0;
        char[] charSet = new char[] {
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
                'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
                'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
        };	//배열안의 문자 숫자는 원하는대로

        StringBuilder password = new StringBuilder();
        Random random;
		try {
			random = SecureRandom.getInstance("SHA1PRNG");
			for (int i = 0; i < 8 ; i++) {
	            double rd = random.nextDouble();
	            index = (int) (charSet.length * rd);
	            password.append(charSet[index]);
	        }	        
		} catch (NoSuchAlgorithmException e) {
			LogUtil.ConsoleLogError(HttpStatus.INTERNAL_SERVER_ERROR, "No_Such_Algorithm_Exception", e.getMessage(), null);
		}
		return password.toString();
    }
}
