package com.kush.position_management;

import com.kush.position_management.PositionManagementApplication;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.assertj.core.api.Assertions.assertThat;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootTest
class PositionManagementApplicationTests {

	@Autowired
	private PositionManagementApplication application;

	@Test
	void contextLoads() {
	}

	@Test
	void applicationStartsSuccessfully() {
		assertThat(application).isNotNull();
	}
}