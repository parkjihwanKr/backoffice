package com.example.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(
		basePackages = {
				"com.example.backoffice.domain.admin.repository",
				"com.example.backoffice.domain.board.repository",
				"com.example.backoffice.domain.comment.repository",
				"com.example.backoffice.domain.file.repository",
				"com.example.backoffice.domain.member.repository",
				"com.example.backoffice.domain.reaction.repository"
		})
public class BackOfficeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackOfficeApplication.class, args);
	}

}
