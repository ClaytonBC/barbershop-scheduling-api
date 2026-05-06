package com.claytonbc.barbershop.config;

import com.claytonbc.barbershop.entity.Customer;
import com.claytonbc.barbershop.enums.Perfil;
import com.claytonbc.barbershop.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class DatabaseSeeder {

    @Bean
    CommandLineRunner init(CustomerRepository repository, PasswordEncoder encoder) {
        return args -> {

            if (repository.findByEmail("admin@barbershop.com").isEmpty()) {

                Customer admin = new Customer();
                admin.setName("Admin");
                admin.setEmail("admin@barbershop.com");
                admin.setPassword(encoder.encode("admin123"));
                admin.setRole(Perfil.OWNER);
                admin.setActive(true);

                repository.save(admin);

                System.out.println("🔥 Admin criado: admin@barbershop.com / admin123");
            }

            if (repository.findByEmail("barber@barbershop.com").isEmpty()) {

                Customer barber = new Customer();
                barber.setName("Barber");
                barber.setEmail("barber@barbershop.com");
                barber.setPassword(encoder.encode("123456"));
                barber.setRole(Perfil.BARBER);
                barber.setActive(true);

                repository.save(barber);

                System.out.println("💈 Barber criado: barber@barbershop.com / 123456");
            }
        };
    }
}
