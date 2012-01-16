package com.ruihui.web.mvc.view;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

public class DownloadView implements View {
	//private Log log=LogFactory.getLog(DownloadView.class);
	@Override
	public String getContentType() {
		return null;
	}

	@Override
	public void render(Map<String, ?> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		InputStream in=(InputStream) model.get(DownloadModel.STREAM_NAME);
		String contentType=(String) model.get(DownloadModel.CONTENT_TYPE_NAME);
		String filename=(String) model.get(DownloadModel.FILENAME_NAME);
		if(contentType != null)
			response.addHeader("Content-Type", contentType);
		if(filename != null)
			response.addHeader("Content-Disposition", "attachment; filename="+filename);
		
		OutputStream out=response.getOutputStream();
		int b;
		while((b=in.read())!=-1){
			out.write(b);
		}
		in.close();
	}

}
