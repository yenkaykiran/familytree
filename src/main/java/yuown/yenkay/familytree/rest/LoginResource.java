package yuown.yenkay.familytree.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class LoginResource {

	@PostMapping("/login")
    public Boolean login() {
		return true;
    }
	
}
