package com.backend.challenge.controller;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;

import com.backend.challenge.utils.Utils;


public class Test {
	public static String checkContentType(String link) throws IOException {
		URL url = new URL(link);
		URLConnection u = url.openConnection();
		System.out.println(u.getHeaderFields());
		String type = u.getHeaderField("Content-Type");
		System.out.println(type);
		if (type.contains("image")) {
			return "image";
		} else if (type.contains("video")) {
			return "video";
		}
		return "text";
	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		String link = "https://images.successstory.com/img_people/house/620Xauto/house5_1455950547.jpg";
		System.out.println(Utils.processContent(link));
	}

}
