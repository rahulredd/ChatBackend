package com.backend.challenge.utils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.mindrot.jbcrypt.BCrypt;

/**
 * 
 * @author Rahul
 *
 */

public class Utils {
	/*
	 * Three different message types are supported. (1) is a basic text-only message. 
	 * (2) is an image link. (3) is a video link. 
	 * The image and video links are saved with some additional metadata: width and height for the image, 
	 * length of the video and source (YouTube, Vevo) for the video. For the purpose of the challenge,
	 *  you can hard-code the metadata to some fixed values when you're saving the message.
	 */

	private static final String URL_REGEX = "^((https?|ftp)://|(www|ftp)\\.)?[a-z0-9-]+(\\.[a-z0-9-]+)+([/?].*)?$";
	private static final String IMAGE_METADATA = "height=42&width=42";
	private static final String VIDEO_METADATA = "length=x&source=web";

	public enum CONTENT_TYPE {
		TEXT,
		IMAGE,
		VIDEO;
	}

	private static boolean hasUrl(String content) {
		Pattern p = Pattern.compile(URL_REGEX);
		Matcher m = p.matcher(content);//replace with string to compare
		if(m.find()) {
			return true;
		}
		return false;
	}
   
	/**
	 * Based on the content of the message, opens the URL and retrieves the type of URL
	 * 
	 * @param content
	 * @return type of the content of message - image or text or video
	 * @throws IOException
	 */
	private static CONTENT_TYPE getContentType(String content) throws IOException {
		URL url = new URL(content);
		URLConnection u = url.openConnection();
		String type = u.getHeaderField("Content-Type");
		if (type.contains("image")) {
			return CONTENT_TYPE.IMAGE;
		} else if (type.contains("video")) {
			return CONTENT_TYPE.VIDEO;
		}
		return CONTENT_TYPE.TEXT;
	}

	/**
	 * If message content of type image or video appends hard coded metadata
	 * @param content
	 * @param contentType
	 * @return content 
	 */
	private static String appendMetadata(String content, String contentType) {
		if (contentType.equals(CONTENT_TYPE.IMAGE.toString())) {
			content += IMAGE_METADATA;
		} else if (contentType.equals(CONTENT_TYPE.VIDEO.toString())) {
			content += VIDEO_METADATA;
		}
		return content;
	}

	/**
	 * Process content of message.
	 * Checks if it has URL. Retrieves the content type and appends metadata
	 * @param content
	 * @return content
	 * @throws IOException
	 */
	public static String processContent(String content) throws IOException {
		if (hasUrl(content)) {
			CONTENT_TYPE contentType = getContentType(content);
			if (contentType.equals(CONTENT_TYPE.IMAGE) || contentType.equals(CONTENT_TYPE.VIDEO)) {
				content = appendMetadata(content, contentType.toString());
			}
		}
		return content;
	}

	/**
	 * Encrypts the password
	 * @param password
	 * @return encrypted password 
	 */
	public static String encrypt(String password) {
		return BCrypt.hashpw(password, BCrypt.gensalt());
	}
}
