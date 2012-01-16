package com.ruihui.web.mvc.view;

import java.io.Writer;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.View;

import com.ruihui.common.text.TextObject;
import com.ruihui.common.text.ToJson;

public class LiteJsonView implements View {

	@Override
	public String getContentType() {
		// TODO Auto-generated method stub
		return "text/html; charset=UTF-8";
	}

	@Override
	public void render(Map<String, ?> map, HttpServletRequest arg1,
			HttpServletResponse response) throws Exception {
		response.addHeader("Content-Type", "text/html; charset=UTF-8");
		TextObject obj=(TextObject) map.get("textObject");
		Writer out=response.getWriter();
		out.write(new ToJson(obj).toString());
		arg1.getContextPath();
	}

}
