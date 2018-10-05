package my.sandbox.pf4j.plugins.test.rest;

import my.sandbox.pf4j.plugins.test.internal.MessageProvider;
import my.sandbox.pf4j.plugins.test.internal.MessageProviderImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Component
@Path("/")
public class TestService {

    @Autowired
    private MessageProvider messageProvider;

    @GET
    @Path("/message")
    public String message() {
        return messageProvider.sayHello();
    }

}
