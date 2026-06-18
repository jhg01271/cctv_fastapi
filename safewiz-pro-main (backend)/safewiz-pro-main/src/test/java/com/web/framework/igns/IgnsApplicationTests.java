package com.web.framework.igns;

import org.junit.jupiter.api.Test;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class IgnsApplicationTests {

	@Test
	SpringApplicationBuilder contextLoads(SpringApplicationBuilder builder) {
		return builder.sources(IgnsApplicationTests.class);
	}

	
}
