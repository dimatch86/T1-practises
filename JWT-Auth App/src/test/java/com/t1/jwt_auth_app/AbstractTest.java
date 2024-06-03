package com.t1.jwt_auth_app;

import com.t1.jwt_auth_app.model.entity.Role;
import com.t1.jwt_auth_app.model.entity.RoleType;
import com.t1.jwt_auth_app.model.entity.User;
import com.t1.jwt_auth_app.repository.UserRepository;
import com.t1.jwt_auth_app.service.AuthenticationService;
import com.t1.jwt_auth_app.service.UserService;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Set;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@Testcontainers
public abstract class AbstractTest {

    protected static PostgreSQLContainer<?> postgreSQLContainer;
    static {
        DockerImageName postgres = DockerImageName.parse("postgres:15.2");
        postgreSQLContainer = new PostgreSQLContainer<>(postgres)
                .withReuse(true);
        postgreSQLContainer.start();
    }

    @DynamicPropertySource
    public static void registerProperties(DynamicPropertyRegistry registry) {
        String jdbcUrl = postgreSQLContainer.getJdbcUrl();
        registry.add("spring.datasource.username", postgreSQLContainer::getUsername);
        registry.add("spring.datasource.password", postgreSQLContainer::getPassword);
        registry.add("spring.datasource.url", () -> jdbcUrl);

    }

    @Autowired
    protected AuthenticationService authenticationService;
    @Autowired
    protected UserService userService;
    @Autowired
    protected UserRepository userRepository;
    @Autowired
    protected WebApplicationContext context;
    @Autowired
    protected MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        authenticationService.registerUser(
                User.builder()
                        .name("user")
                        .email("user@mail.ru")
                        .password(BCrypt.hashpw("user", BCrypt.gensalt()))
                        .roles(Set.of(Role.from(RoleType.ROLE_USER)))
                        .build()
        );
    }

    @AfterEach
    public void afterEach() {
        userRepository.deleteAll();
    }
}
