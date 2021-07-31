package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

	@Value("${myimgpath}")
	private String imgPath;

	@Test
	void contextLoads() {
		System.out.println(imgPath);
	}

}
