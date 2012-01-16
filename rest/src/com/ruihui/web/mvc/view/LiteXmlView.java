package com.ruihui.web.mvc.view;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import com.ruihui.common.text.TextObject;
import com.ruihui.common.text.ToXml;

public class LiteXmlView implements View {

	@Override
	public String getContentType() {
		return "text/xml; charset=UTF-8";
	}

	@Override
	public void render(Map<String, ?> map, HttpServletRequest arg1,
			HttpServletResponse response) throws Exception {
		response.addHeader("Content-Type", "text/xml; charset=UTF-8");
		TextObject obj=(TextObject) map.get("textObject");
		Writer out=response.getWriter();
		out.write(new ToXml(obj).toString());
	}

}
