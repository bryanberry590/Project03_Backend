package Project03.SpringbootApplication.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import Project03.SpringbootApplication.service.ExampleService;

@RestController
public class BaseController {
    
    @Autowired
    private ExampleService exampleService;

    @GetMapping("/")
    public String index() {
        return "Welcome to the home page. Example Service route is /data";
    }
    
    @PostMapping("/data")
    public String saveData(@RequestBody Map<String, Object> data) {
        try {
            return exampleService.saveData("test-collection", "test-doc", data);
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
    
    @GetMapping("/data")
    public Map<String, Object> getData() {
        try {
            return exampleService.getData("test-collection", "test-doc");
        } catch (Exception e) {
            Map<String, Object> error = new HashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }
}