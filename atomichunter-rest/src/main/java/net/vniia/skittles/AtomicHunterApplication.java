package net.vniia.skittles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class AtomicHunterApplication {

	public static void main(String[] args) {
		SpringApplication.run(AtomicHunterApplication.class, args);
	}
}
