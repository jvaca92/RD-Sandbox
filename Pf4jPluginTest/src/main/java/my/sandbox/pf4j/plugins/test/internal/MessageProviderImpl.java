package my.sandbox.pf4j.plugins.test.internal;


import org.springframework.stereotype.Component;

@Component
public class MessageProviderImpl implements MessageProvider {

    @Override
    public String sayHello() {
        return "Hello There";
    }
}
