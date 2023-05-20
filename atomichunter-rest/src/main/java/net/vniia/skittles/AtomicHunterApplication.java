package net.vniia.skittles;

import lombok.extern.log4j.Log4j2;
import net.vniia.skittles.controllers.TelegramBotController;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

import java.util.Properties;

@SpringBootApplication
@EnableScheduling
@RequiredArgsConstructor
@Log4j2
public class AtomicHunterApplication {

	@Value("${telegram.link}")
	private String telegramLink;

	private final TelegramBotController telegramBotController;

	public static void main(String[] args) {
		SpringApplication.run(AtomicHunterApplication.class, args);
	}

	@Bean
	public void telegramBotConstruct() throws TelegramApiException {
		if (!telegramLink.equals("null")) {
			TelegramBotController.telegramBotIsEnabled = true;
			TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
			botsApi.registerBot(telegramBotController);
		} else {
			log.warn("Телеграм-бот не включен! Скорее всего, не проставлены настройки TelegramLink, TelegramToken");
		}
	}

	@Bean
	ObjectMapper objectMapper() {
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.registerModule(new JavaTimeModule());
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		return objectMapper;
	}

	@Bean
	public JavaMailSender getJavaMailSender() {
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost("smtp.rambler.ru");
		mailSender.setPort(465);

		mailSender.setUsername("atomskittles@rambler.ru");
		mailSender.setPassword("Qazwsxedc123");

		Properties props = mailSender.getJavaMailProperties();
		props.put("mail.transport.protocol", "smtp");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.debug", "true");
		props.put("mail.smtp.ssl.enable", "true");

		return mailSender;
	}
}
