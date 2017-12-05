package com.classroom.zhu.EasyClassroom;

import com.classroom.zhu.common.spring.SpringConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(SpringConfiguration.class)
public class EasyClassroomApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyClassroomApplication.class, args);
	}
}
