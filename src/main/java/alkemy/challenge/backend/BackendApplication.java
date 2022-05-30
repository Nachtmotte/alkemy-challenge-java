package alkemy.challenge.backend;

import alkemy.challenge.backend.entity.Role;
import alkemy.challenge.backend.entity.Roles;
import alkemy.challenge.backend.entity.User;
import alkemy.challenge.backend.repository.RoleRepository;
import alkemy.challenge.backend.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class BackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    CommandLineRunner run(UserRepository userRepo, RoleRepository roleRepo, PasswordEncoder passwordEncoder) {
        return args -> {
            if (!userRepo.existsByUsername("admin")) {
                Role roleUser;
                Role roleAdmin;
                if (!roleRepo.existsByName(Roles.ROLE_ADMIN.name())) {
                    roleAdmin = roleRepo.save(new Role(Roles.ROLE_ADMIN.name()));
                }else{
                    roleAdmin = roleRepo.findByName(Roles.ROLE_ADMIN.name());
                }
                if (!roleRepo.existsByName(Roles.ROLE_USER.name())) {
                    roleUser = roleRepo.save(new Role(Roles.ROLE_USER.name()));
                }else{
                    roleUser = roleRepo.findByName(Roles.ROLE_USER.name());
                }
                User user = new User("admin", "admin@admin.com", passwordEncoder.encode("admin"));
                user.getRoles().add(roleAdmin);
                user.getRoles().add(roleUser);
                userRepo.save(user);
            }
        };
    }
}
