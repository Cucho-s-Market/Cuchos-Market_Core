package com.project.cuchosmarket;

import com.project.cuchosmarket.dto.DtUser;
import com.project.cuchosmarket.exceptions.UserExistException;
import com.project.cuchosmarket.services.UserService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.Date;

import static com.project.cuchosmarket.enums.Role.ADMIN;

@SpringBootApplication
public class CuchosMarketApplication {

	public static void main(String[] args) {
		SpringApplication.run(CuchosMarketApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(
			UserService service
	) {
		System.out.println(new Date());
		return args -> {
			var admin = DtUser.builder()
					.firstName("Admin")
					.lastName("Admin")
					.email("admin@mail.com")
					.password("password")
					.role(ADMIN.name())
					.build();
			try {
				System.out.println("Admin token: " + service.addAdmin(admin).getToken());
			} catch (UserExistException e) {
				System.out.println("Admin token: " + service.authenticate(admin).getToken());
			}
		};
	}

}
