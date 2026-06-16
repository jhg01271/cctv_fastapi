package kr.co.igns.framework.utils.client;

import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import org.apache.commons.collections4.map.HashedMap;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import kr.co.igns.framework.config.log.LogUtil;

@Service
public class HttpClient {

	@SuppressWarnings("unchecked")
	public Map<String, Object> httpClient(String url, String type, String token, Map<String, Object> body) {
		Map<String, Object> result = new HashedMap<String, Object>();
		BufferedReader in = null;
		OutputStream os = null;
		try {
			// URL
			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			StringBuilder response = new StringBuilder();
			String inputLine;

			// 요청방식
			con.setRequestMethod(type);
			if (!token.isBlank())
				con.setRequestProperty("Authorization", "Bearer " + token);

			con.setRequestProperty("Content-Type", "application/json");
			con.setDoOutput(true);

			if (!body.isEmpty()) {
				// Map형식 파라미터
				String requestBody = getJsonStringFromMap(body);
				os = con.getOutputStream();
				os.write(requestBody.getBytes("utf-8"));
				os.flush();
				os.close();
			}
			int responseCode = con.getResponseCode();

			// responseBody 구하기
			if (responseCode >= 200 && responseCode <= 209) {
				in = new BufferedReader(new InputStreamReader(con.getInputStream(), "utf-8"));
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				LogUtil.ConsoleLogInfo(HttpStatus.OK, "HttpClient -> Response Body", response.toString(), null);
			} else {
				in = new BufferedReader(new InputStreamReader(con.getErrorStream(), "utf-8"));
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				LogUtil.ConsoleLogInfo(HttpStatus.OK, "HttpClient -> Response Body", response.toString(), null);
			}
			if (isJSONValid(response.toString())) {
				try {
					JSONParser jsonParser = new JSONParser();
					JSONObject jsonObj = (JSONObject) jsonParser.parse(response.toString());
					Map<String, Object> tmp = (Map<String, Object>) jsonObj;
					result.put("code", con.getResponseCode());
					result.put("response", tmp);
				} catch (ParseException e) {
					LogUtil.ConsoleLogInfo(HttpStatus.OK, "HttpClient -> ParseException", e.getMessage(), null);
					return result;
				}
			} else {
				result.put("code", con.getResponseCode());
				result.put("response", response.toString());
			}
			return result;
		} catch (IOException e) {
			LogUtil.ConsoleLogInfo(HttpStatus.OK, "HttpClient -> IOException", e.getMessage(), null);
			result.put("code", 500);
			return result;
		} finally {
			try {
				if (in != null) {
		            in.close();
		        }
		        if (os != null) {
		            os.close();
		        }
			}catch (IOException e) {
				LogUtil.ConsoleLogInfo(HttpStatus.OK, "HttpClient -> IOException", e.getMessage(), null);
			}
		}

	}

	@SuppressWarnings("unchecked")
	public static String getJsonStringFromMap(Map<String, Object> map) {

		JSONObject json = new JSONObject();

		for (Map.Entry<String, Object> entry : map.entrySet()) {

			String key = entry.getKey();
			Object value = entry.getValue();
			json.put(key, value);
		}

		return json.toJSONString();
	}

	@SuppressWarnings("unused")
	private static String getRequestResult(HttpURLConnection conn) throws IOException {
		BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
		StringBuilder response = new StringBuilder();
		String line;

		while ((line = reader.readLine()) != null) {
			response.append(line);
		}

		reader.close();
		return response.toString();
	}

	public static boolean isJSONValid(String jsonString) {
		try {
			JSONParser parser = new JSONParser();
			parser.parse(jsonString);
			return true;
		} catch (ParseException | NullPointerException e) {
			LogUtil.ConsoleLogInfo(HttpStatus.OK, "HttpClient -> ParseException | NullPointerException", e.getMessage(), null);
			return false;
		}
	}

}
