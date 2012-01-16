package com.ruihui.erp.web.controller;

import java.text.SimpleDateFormat;

import org.springframework.web.servlet.ModelAndView;

import com.ruihui.common.text.TextObject;

public class Utils {
	
	public static TextObject failureTextObject(String message){
		return new TextObject("result").appendChild(
				new TextObject("message")
				.addAttribute("failure", true)
				.addAttribute("success", false)
				.addAttribute("value", message)
		);
	}
	static public TextObject successTextobject(){
		return new TextObject("result").appendChild(new TextObject("message").addAttribute("failure", false).addAttribute("success", true));
	}
	static public ModelAndView exceptionReturn(Exception e,String uri){

		TextObject obj=failureTextObject(e.getMessage());
		ModelAndView mov;
		if(uri.endsWith(".xml")){
			mov=new ModelAndView("xml");
		} else if(uri.endsWith(".json")){
			mov=new ModelAndView("json");
		} else {
			mov=new ModelAndView("exception");
		}
		mov.addObject("textObject", obj);
		return mov;
	}
	static public SimpleDateFormat dateFormat(String pattern){
		return new SimpleDateFormat(pattern);
	}
}
