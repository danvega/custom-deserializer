package dev.danvega.customdeserializer;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@JsonTest
class CustomDeserializerApplicationTests {

	@Value("classpath:data/blog-posts.json")
	Resource blogPostsJson;

	@Autowired
	ObjectMapper objectMapper;

	String json;

	@Test
	void contextLoads() {
		assertNotNull(objectMapper);
	}

	@BeforeEach
	void setUp() throws IOException {
		json = new String(Files.readAllBytes(blogPostsJson.getFile().toPath()));
		var blog = objectMapper.readValue(json, Blog.class);
		blog.posts().forEach(System.out::println);

	}
}
