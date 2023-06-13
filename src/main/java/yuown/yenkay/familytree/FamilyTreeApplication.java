package yuown.yenkay.familytree;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("yuown.yenkay.familytree.model")
public class FamilyTreeApplication {

	public static void main(String[] args) {
		SpringApplication.run(FamilyTreeApplication.class, args);
	}
}
