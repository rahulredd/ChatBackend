package com.backend.challenge.utils;

import java.io.IOException;

import org.junit.Test;
import org.mindrot.jbcrypt.BCrypt;

import junit.framework.Assert;

public class TestUtils {
	private static final String IMAGE_METADATA = "height=42&width=42";
	private static final String VIDEO_METADATA = "length=x&source=web";

	@Test
	public void testEncrypt() {
		String password = "makeithappen";
		String encryptedPassword = Utils.encrypt(password);
		Assert.assertTrue(BCrypt.checkpw(password, encryptedPassword));
	}

	@Test
	public void testEncryptEmptyPassword() {
		String password = "";
		String encryptedPassword = Utils.encrypt(password);
		Assert.assertTrue(BCrypt.checkpw(password, encryptedPassword));
	}

	@Test
	public void testProcessContentWithText() throws IOException {
		String content = "Hey! ASAPP is the best place to work at";
		Assert.assertEquals(content, Utils.processContent(content));
	}

	@Test
	public void testProcessContentWithImage() throws IOException {
		String content = "https://pbs.twimg.com/media/CdCLAAsWoAEEP16.jpg";
		String processedContent = Utils.processContent(content);
		content += IMAGE_METADATA;
		Assert.assertEquals(content, processedContent);
	}
	
	//@Test
	public void testProcessContentWithVideo() throws IOException {
		String content = "http://up.anv.bz/latest/anvload.html?key=eyJtIjoiY2JzIiwidiI6IjM2MzMwMTEiLCJhbnZhY2siOiJhbnZhdG9fY2JzbG9jYWxfYXBwX3dlYl9wcm9kXzU0N2YzZTQ5MjQxZWYwZTVkMzBjNzliMmVmYmNhNWQ5MmM2OThmNjciLCJwbHVnaW5zIjp7ImNvbXNjb3JlIjp7ImNsaWVudElkIjoiMzAwMDAyMyIsImMzIjoibmV3eW9yay5jYnNsb2NhbC5jb20ifSwiZGZwIjp7ImNsaWVudFNpZGUiOnsiYWRUYWdVcmwiOiJodHRwOi8vcHViYWRzLmcuZG91YmxlY2xpY2submV0L2dhbXBhZC9hZHM%2Fc3o9MngyJml1PS80MTI4L0NCUy5OWSZjaXVfc3pzJmltcGw9cyZnZGZwX3JlcT0xJmVudj12cCZvdXRwdXQ9eG1sX3Zhc3QyJnVudmlld2VkX3Bvc2l0aW9uX3N0YXJ0PTEmdXJsPVtyZWZlcnJlcl91cmxdJmRlc2NyaXB0aW9uX3VybD1bZGVzY3JpcHRpb25fdXJsXSZjb3JyZWxhdG9yPVt0aW1lc3RhbXBdIiwia2V5VmFsdWVzIjp7ImNhdGVnb3JpZXMiOiJbW0NBVEVHT1JJRVNdXSIsInByb2dyYW0iOiJbW1BST0dSQU1fTkFNRV1dIiwic2l0ZVNlY3Rpb24iOiJmZWF0dXJlZCJ9fX0sInJlYWxUaW1lQW5hbHl0aWNzIjp0cnVlLCJoZWFydGJlYXRCZXRhIjp7ImFjY291bnQiOiJjYnNsb2NhbC1nbG9iYWwtdW5pZmllZCIsInB1Ymxpc2hlcklkIjoiY2JzbG9jYWwiLCJqb2JJZCI6InNjX3ZhIiwibWFya2V0aW5nQ2xvdWRJZCI6IjgyM0JBMDMzNTU2NzQ5N0Y3RjAwMDEwMUBBZG9iZU9yZyIsInRyYWNraW5nU2VydmVyIjoiY2JzZGlnaXRhbG1lZGlhLmhiLm9tdHJkYy5uZXQiLCJjdXN0b21UcmFja2luZ1NlcnZlciI6ImNic2RpZ2l0YWxtZWRpYS5kMS5zYy5vbXRyZGMubmV0IiwiY2hhcHRlclRyYWNraW5nIjpmYWxzZSwidmVyc2lvbiI6IjEuNSJ9fX0";
		String processedContent = Utils.processContent(content);
		content += VIDEO_METADATA;
		Assert.assertEquals(content, processedContent);
	}
}
