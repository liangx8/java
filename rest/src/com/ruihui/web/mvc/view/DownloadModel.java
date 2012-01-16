package com.ruihui.web.mvc.view;

import java.io.InputStream;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

public class DownloadModel extends ModelAndView {
	final static String STREAM_NAME="stream";
	final static String CONTENT_TYPE_NAME="contenttype";
	final static String FILENAME_NAME="filename";
	public DownloadModel(InputStream is) {
		super(LiteViewResolver.DOWNLOAD_VIEW);
		addObject(STREAM_NAME,is);
	}
	@Override
	public void setView(View view) {
		throw new UnsupportedOperationException();
	}
	@Override
	public void setViewName(String viewName) {
		throw new UnsupportedOperationException();
	}
	public void setContentType(String contentType) {
		addObject(CONTENT_TYPE_NAME, contentType);
	}
	public void setFilename(String filename){
		addObject(FILENAME_NAME,filename);
	}
}
