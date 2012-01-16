package com.ruihui.web.mvc.interceptor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.ruihui.web.mvc.view.LiteViewResolver;


public class TextInterceptorAdapter implements HandlerInterceptor{
	private Log log=LogFactory.getLog(TextInterceptorAdapter.class);
	private static enum Type {
		XML,JSON,T_DEFAULT
	}
	private static Pattern PATTERN_XML=Pattern.compile("\\.xml$");
	private static Pattern PATTERN_JSON=Pattern.compile("\\.json$");

	private Type suffix;
	@Override
	public void afterCompletion(HttpServletRequest request,HttpServletResponse response, Object target, Exception ex) throws Exception {
		log.info("remote ip:"+request.getRemoteAddr()+" request uri:"+request.getRequestURI());
//		if(ex==null)return;
/*		
		if(ex instanceof MessageException){
			switch(suffix){
			case T_DEFAULT:
				return;
			case XML:
				response.sendRedirect("/WEB-INF/jsp/xml.jsp");
				//request.getRequestDispatcher("/WEB-INF/jsp/xml.jsp").forward(request, response);
				break;
			case JSON:
				response.sendRedirect("/WEB-INF/jsp/json.jsp");
				//request.getRequestDispatcher("/WEB-INF/jsp/json.jsp").forward(request, response);
				break;
			}
		}
*/		
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response,Object target, ModelAndView mav) throws Exception {
		Object obj= mav.getModel().get("textObject");
		switch(suffix){
		case T_DEFAULT:
			return;
		case XML:
			if(obj==null){
				throw new ServletException("Return type should be class TextObject");
			}
			mav.setViewName(LiteViewResolver.XML_VIEW);
			break;
		case JSON:
			if(obj==null){
				throw new ServletException("Return type should be class TextObject");
			}
			mav.setViewName(LiteViewResolver.JSON_VIEW);
			break;
		}
	}


	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response,Object target) throws Exception {
		String uri=request.getRequestURI();
		Matcher m=PATTERN_XML.matcher(uri);
		if(m.find()){
			suffix=Type.XML;
		} else { 
			m=PATTERN_JSON.matcher(uri);
			if(m.find()){
				suffix=Type.JSON;
			} else {
				suffix=Type.T_DEFAULT;
			}
		}
		return true;
	}
}
