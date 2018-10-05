package my.sandbox.kubernetes.test.controller;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.MessageFormat;

@RestController
public class TestController {


    @Value("${prop.test1}")
    private String prop1;

    @Value("${prop.test2}")
    private String prop2;

    @GetMapping(path = "/test")
    public String sendMesssage() {
        return MessageFormat.format("The prop1 has value {0} and prop2 has value {1}", prop1, prop2);
    }
}
