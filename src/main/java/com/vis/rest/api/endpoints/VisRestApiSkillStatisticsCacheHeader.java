package com.vis.rest.api.endpoints;

import java.lang.reflect.Method;

import org.springframework.core.MethodParameter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.ccp.aop.CcpAllowNullParameter;
import com.ccp.aop.CcpAllowNullReturn;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Puts the {@code Cache-Control} of {@link VisSkillStatisticsCacheControl} in the successful (200) responses of
 * {@link VisRestApiSkillStatistics}, the only routes of the vis behind the CDN. An error comes from the exception handler,
 * another class, so it never gets the header and is never kept for long by the CDN.
 */
@RestControllerAdvice(assignableTypes = VisRestApiSkillStatistics.class)
public class VisRestApiSkillStatisticsCacheHeader implements ResponseBodyAdvice<Object> {

	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		Class<?> containingClass = returnType.getContainingClass();
		boolean statisticsRoute = VisRestApiSkillStatistics.class.equals(containingClass);
		return statisticsRoute;
	}

	@CcpAllowNullParameter
	@CcpAllowNullReturn
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

		boolean notAServletRequest = false == request instanceof ServletServerHttpRequest;
		if(notAServletRequest) {
			return body;
		}

		boolean notAServletResponse = false == response instanceof ServletServerHttpResponse;
		if(notAServletResponse) {
			return body;
		}

		ServletServerHttpResponse servletServerHttpResponse = (ServletServerHttpResponse) response;
		HttpServletResponse servletResponse = servletServerHttpResponse.getServletResponse();
		int status = servletResponse.getStatus();
		int ok = HttpStatus.OK.value();
		boolean notOk = status != ok;
		if(notOk) {
			return body;
		}

		ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) request;
		HttpServletRequest servletRequest = servletServerHttpRequest.getServletRequest();
		Method method = returnType.getMethod();
		String methodName = method.getName();
		VisSkillStatisticsCacheControl cacheControl = VisSkillStatisticsCacheControl.valueOf(methodName);
		String headerValue = cacheControl.getHeaderValue(servletRequest);
		HttpHeaders headers = response.getHeaders();
		headers.set(HttpHeaders.CACHE_CONTROL, headerValue);
		return body;
	}
}
