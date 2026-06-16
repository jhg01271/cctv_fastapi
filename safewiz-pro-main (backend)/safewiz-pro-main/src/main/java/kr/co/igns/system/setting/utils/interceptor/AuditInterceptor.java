package kr.co.igns.system.setting.utils.interceptor;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import kr.co.igns.framework.config.security.SecurityUtil;
import kr.co.igns.framework.config.security.jwt.service.JwtService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.co.igns.system.setting.model.AuditVO;
import kr.co.igns.system.setting.service.AuditService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Component
public class AuditInterceptor implements HandlerInterceptor {

	private final AuditService auditService;
	private final JwtService jwtService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String browser = "";
		String userAgent = request.getHeader("User-Agent").toLowerCase();
		if (userAgent.contains("trident/7.0")) {
			browser = "ie11";
		} else if (userAgent.contains("msie 10")) {
			browser = "ie10";
		} else if (userAgent.contains("msie 9")) {
			browser = "ie9";
		} else if (userAgent.contains("msie 8")) {
			browser = "ie8";
		} else if (userAgent.contains("chrome/")) {
			if (userAgent.contains("edge/") || userAgent.contains("edg/")) {
				browser = "Edge";
			} else if (userAgent.contains("whale/")) {
				browser = "Whale";
			} else {
				browser = "Chrome";
			}
		} else if (!userAgent.contains("chrome/") && userAgent.contains("safari/")) {
			if (userAgent.contains("crios/")) {
				browser = "Chrome";
			} else {
				browser = "Safari";
			}
		} else if (userAgent.contains("firefox/")) {
			browser = "Firefox";
		} else {
			browser = "etc";
		}

		String os = "";
		userAgent = userAgent.toLowerCase();
		if (userAgent.contains("windows nt 10.0")) {
			os = "Windows10";
		} else if (userAgent.contains("windows nt 6.3")) {
			os = "Windows8.1";
		} else if (userAgent.contains("windows nt 6.2")) {
			os = "Windows8";
		} else if (userAgent.contains("windows nt 6.1")) {
			os = "Windows7";
		} else if (userAgent.contains("windows nt 6.0")) {
			os = "WindowsVista";
		} else if (userAgent.contains("windows nt 5.1")) {
			os = "WindowsXP";
		} else if (userAgent.contains("windows nt 5.0")) {
			os = "Windows2000";
		} else if (userAgent.contains("windows nt 4.0")) {
			os = "WindowsNT";
		} else if (userAgent.contains("windows 98")) {
			os = "Windows98";
		} else if (userAgent.contains("windows 95")) {
			os = "Windows95";
		}
		// window 외
		else if (userAgent.contains("iphone")) {
			os = "iPhone";
		} else if (userAgent.contains("ipad")) {
			os = "iPad";
		} else if (userAgent.contains("android")) {
			os = "android";
		} else if (userAgent.contains("mac")) {
			os = "mac";
		} else if (userAgent.contains("linux")) {
			os = "Linux";
		} else {
			os = "etc";
		}

		// 파라메터
		String param = mapToString(request.getParameterMap());
		param = param.length() > 500 ? param.substring(0, 500) : param;

		// userId
		String userId = "";
		// TODO: swagger 이동시 오류로 조건 추가, 다른 로직에 문제가 없는지 확인
		if (request.getHeader("Authorization") != null) {
			String token = request.getHeader("Authorization").toString();
			if (token != null) {
				try {
					if (jwtService.isTokenValid(token) != "ok") {
						userId = SecurityUtil.getCurrentMemberId();
					}
				} catch (UsernameNotFoundException e) {
					// e.printStackTrace();//에러 뿌리지 않게 주석
					log.info("===============토큰 오류 : " + token);
				}
			}
		}

		AuditVO vo = AuditVO.builder()
				.referer(request.getHeader("Referer"))//
				.url(request.getRequestURL().toString())//
				.param(param)//
				.userLocale(request.getLocale().toString())//
				.localName(request.getLocalName())//
				.os(os)//
				.ipAddress(request.getRemoteAddr())//
				.browser(browser)//
				.userAgent(request.getHeader("User-Agent"))//
				.sessionId(request.getSession().getId())//
				.userId(userId)//
				.build();

		//auditService.create(vo);

		return HandlerInterceptor.super.preHandle(request, response, handler);
	}

	// @Override
	// public void postHandle(HttpServletRequest request, HttpServletResponse
	// response, Object handler, ModelAndView modelAndView) throws Exception {
	// HandlerInterceptor.super.postHandle(request, response, handler,
	// modelAndView);
	// }

	private String mapToString(Map<String, String[]> param) {

		Vector vt = new Vector(param.keySet());
		Collections.sort(vt);

		String[] value = null;

		Iterator<?> iter = vt.iterator();
		StringBuilder sb = new StringBuilder();
		HashMap<String, Object> hm = new HashMap<String, Object>();

		// sb.append("Parameter =\n");

		while (iter.hasNext()) {
			String key = (String) iter.next();
			if (param.get(key) instanceof String[]) {// String[]
				value = (String[]) (param.get(key));// output은 String or String[]
				for (int validx = 0; validx < value.length; validx++) {
					sb.append(key + "[" + validx + "]" + " : " + String.valueOf(value[validx]) + ", ");

					hm.put(key, String.valueOf(value[validx]));

				}
			} else {
				sb.append(key + " : " + String.valueOf(param.get(key)) + ", ");
			}
		}

		return sb.toString();
	}

}
