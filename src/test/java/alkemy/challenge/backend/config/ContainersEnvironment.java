package alkemy.challenge.backend.config;

import alkemy.challenge.backend.containers.PostgresTestContainer;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
public class ContainersEnvironment {

    @Container
    public static PostgreSQLContainer<PostgresTestContainer> postgreSQLContainer =
            PostgresTestContainer.getInstance();
}
