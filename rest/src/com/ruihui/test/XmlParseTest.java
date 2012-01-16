package com.ruihui.test;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XmlParseTest {
	public static void main(String[] args) throws SAXException, IOException, ParserConfigurationException {
		FileInputStream in=new FileInputStream("d:/test.xml");

		SAXParserFactory spf=SAXParserFactory.newInstance();
		SAXParser parser=spf.newSAXParser();
		parser.parse(in, new MyHandler());
		in.close();
	}
}

class MyHandler extends DefaultHandler{
	private int space=0;
	@Override
	public void startElement(String uri, String localName, String qName,
			Attributes attributes) throws SAXException {
		for(int i=0;i<space;i++){
			System.out.print('\t');
		}
		System.out.println(qName+' '+attributes.getValue("title"));
		space ++;
	}
	@Override
	public void endElement(String uri, String localName, String qName)
			throws SAXException {
		space --;
	}
	@Override
	public void characters(char[] buf, int start, int length) throws SAXException {
		StringBuilder sb=new StringBuilder();
		boolean hasContent=false;
		for(int i=0;i<length;i++){
			if(buf[start+i]=='\n')continue;
			hasContent=true;
			sb.append(buf[start+i]);
		}
		if(hasContent){
			for(int i=0;i<space;i++){
				System.out.print('\t');
			}

			System.out.println(sb);
		}
	}
	@Override
	public void endDocument() throws SAXException {
		super.endDocument();
	}
	@Override
	public void startDocument() throws SAXException {
		super.startDocument();
	}
}