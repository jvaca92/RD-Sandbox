package my.sandbox.pf4j.plugins.test.internal;


import org.springframework.stereotype.Component;

@Component
public class MessageProviderImpl2 implements MessageProvider2 {

    @Override
    public String sayHello() {
        return "Hello there. What is your name ?";
    }
}
