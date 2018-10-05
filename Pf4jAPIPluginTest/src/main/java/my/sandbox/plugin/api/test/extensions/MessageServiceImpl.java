package my.sandbox.plugin.api.test.extensions;

import org.pf4j.Extension;

@Extension
public class MessageServiceImpl implements MessageService {
    @Override
    public String sendMessage() {
        return "Hello this is message for your plugin friend....";
    }
}
