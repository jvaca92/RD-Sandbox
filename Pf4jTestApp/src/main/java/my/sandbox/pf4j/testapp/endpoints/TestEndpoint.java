package my.sandbox.pf4j.testapp.endpoints;


import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/api")
public class TestEndpoint {

//    @GetMapping("/test")
    @GET
    @Path("/test")
    public String message() {
        return "Test message...";
    }
//
//    @GET
//    @Path("/test2/result")
//    public String message2() {
//        return "Test message2...";
//    }
}
