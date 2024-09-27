package dev.kayange.Multivendor.E.commerce;

import dev.kayange.Multivendor.E.commerce.entity.users.Role;
import dev.kayange.Multivendor.E.commerce.enumeration.SystemUserRole;
import dev.kayange.Multivendor.E.commerce.service.RoleService;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.TimeZone;

@SpringBootApplication
@EnableAsync
@EnableJpaAuditing(auditorAwareRef = "auditorAware")
@EnableTransactionManagement
public class Application extends SpringBootServletInitializer {

	public static void main(String[] args) {
		System.setProperty("server.connection-timeout", "300000");
		SpringApplication.run(Application.class, args);
	}

	@Bean
	@Transactional
	CommandLineRunner saveRoles(RoleService roleService){
		return args -> {
			List<String> roles = List.of(SystemUserRole.ADMIN.getRoleName(), SystemUserRole.SUPER_ADMIN.getRoleName(), SystemUserRole.MANAGER.getRoleName(), SystemUserRole.VENDOR.getRoleName(), SystemUserRole.CUSTOMER.getRoleName());
			for (String role : roles) {
				if(roleService.findByName(role).isEmpty()) {
					Role userRole = Role.builder().name(role).build();
					roleService.save(userRole);
				}
			}
		};
	}

	@PostConstruct
	void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("GMT+3"));
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(Application.class);
	}
}
