package my.sanbox.fabric.spring.test.controller;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping(path = "/test")
    public String sendMesssage() {
        return "Hello there !!!";
    }
}
