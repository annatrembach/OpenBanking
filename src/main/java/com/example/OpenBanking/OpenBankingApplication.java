package com.example.OpenBanking;

import com.example.OpenBanking.data.DbContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class OpenBankingApplication {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(OpenBankingApplication.class, args);
		DbContext dbContext = context.getBean(DbContext.class);
		dbContext.initTestData();
	}

}
