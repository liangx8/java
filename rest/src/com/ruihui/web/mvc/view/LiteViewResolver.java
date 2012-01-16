package com.ruihui.web.mvc.view;

import java.util.Locale;

import org.springframework.core.Ordered;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.AbstractCachingViewResolver;

public class LiteViewResolver extends AbstractCachingViewResolver implements Ordered{
	private int order;
	public static final String DOWNLOAD_VIEW="download";
	public static final String JSON_VIEW="json";
	public static final String XML_VIEW="xml";
	@Override
	public int getOrder() {
		return order;
	}
	public void setOrder(int order) {
		this.order = order;
	}
	@Override
	protected View loadView(String name, Locale locale) throws Exception {
		if(name == DOWNLOAD_VIEW)
			return new DownloadView();
		if(XML_VIEW.equals(name)){
			return new LiteXmlView();
		}
		if(JSON_VIEW.equals(name)){
			return new LiteJsonView();
		}
		return null;
	}

}
