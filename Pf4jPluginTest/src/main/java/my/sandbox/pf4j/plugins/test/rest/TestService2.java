package my.sandbox.pf4j.plugins.test.rest;

import my.sandbox.pf4j.plugins.test.internal.MessageProvider;
import my.sandbox.pf4j.plugins.test.internal.MessageProvider2;
import my.sandbox.pf4j.spring.annotation.ExtensionImport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Component
@Path("/")
public class TestService2 {

    @Autowired
    private MessageProvider2 messageProvider2;

    @ExtensionImport
    private my.sandbox.plugin.api.test.extensions.MessageService messageService;

    @GET
    @Path("/message2")
    public String message() {
        return messageProvider2.sayHello();
    }

    @GET
    @Path("/message3")
    public String message3() {
        return messageService.sendMessage();
    }

}
