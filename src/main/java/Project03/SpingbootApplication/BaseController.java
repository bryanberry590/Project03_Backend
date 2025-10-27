package Project03.SpringbootApplication;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BaseController {

	@GetMapping("/")
	public String index() {
		return "Welcome to the home page. Routes will be listed here in the future!";
	}

}