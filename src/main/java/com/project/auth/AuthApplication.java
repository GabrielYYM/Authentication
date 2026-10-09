package com.project.auth;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthApplication {

	public static void main(String[] args) {
		Dotenv atlasEnv = Dotenv.configure().filename("atlas-credentials.env").ignoreIfMissing().load();
		atlasEnv.entries().forEach(entry -> {
			System.setProperty(entry.getKey(), entry.getValue());
		});

		Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
		dotenv.entries().forEach(entry -> {
			if (System.getProperty(entry.getKey()) == null) {
				System.setProperty(entry.getKey(), entry.getValue());
			}
		});

		SpringApplication.run(AuthApplication.class, args);
	}

}
