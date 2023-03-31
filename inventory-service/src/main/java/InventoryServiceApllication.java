import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableAutoConfiguration
@EnableJpaRepositories("com.project.repository")
@EntityScan("com.project.model")
@ComponentScan("com.project")
public class InventoryServiceApllication {
	public static void main(String[] args) {
		SpringApplication.run(InventoryServiceApllication.class, args);
	}
}
