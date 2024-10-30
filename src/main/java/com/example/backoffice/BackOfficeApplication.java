package com.example.backoffice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication
@EnableJpaRepositories(
		basePackages = {
				"com.example.backoffice.domain.memberAnswer.repository",
				"com.example.backoffice.domain.answer.repository",
				"com.example.backoffice.domain.question.repository",
				"com.example.backoffice.domain.memberEvaluation.repository",
				"com.example.backoffice.domain.evaluation.repository",
				"com.example.backoffice.domain.favorite.repository",
				"com.example.backoffice.domain.finance.repository",
				"com.example.backoffice.domain.event.repository",
				"com.example.backoffice.domain.board.repository",
				"com.example.backoffice.domain.comment.repository",
				"com.example.backoffice.domain.file.repository",
				"com.example.backoffice.domain.member.repository",
				"com.example.backoffice.domain.reaction.repository",
				"com.example.backoffice.domain.vacation.repository",
				"com.example.backoffice.domain.expense.repository"
		})
@EnableMongoRepositories(
		basePackages = {
				"com.example.backoffice.global.audit.repository",
				"com.example.backoffice.domain.notification.repository"
		}, mongoTemplateRef = "mongoTemplate")

public class BackOfficeApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackOfficeApplication.class, args);
	}

}
