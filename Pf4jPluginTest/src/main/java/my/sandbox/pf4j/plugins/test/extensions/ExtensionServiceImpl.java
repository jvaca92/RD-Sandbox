package my.sandbox.pf4j.plugins.test.extensions;

import org.pf4j.Extension;

@Extension
public class ExtensionServiceImpl implements ExtensionService {
    @Override
    public String testExtension() {
        return "Service is tested !!!!";
    }
}
