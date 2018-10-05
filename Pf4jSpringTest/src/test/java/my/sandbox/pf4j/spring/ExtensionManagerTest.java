package my.sandbox.pf4j.spring;

import my.sandbox.pf4j.spring.test.TestService;
import my.sandbox.pf4j.spring.test.TestServiceImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.Assert;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = TestAppConfig.class)
public class ExtensionManagerTest {

    @Autowired
    ExtensionRegistry extensionRegistry;

    @Autowired
    PluginManager pluginManager;

    private ExtensionManager extensionManager;

    @Before
    void setUp() {
        extensionManager = ExtensionManager.getInstance();
        extensionRegistry.registerExtension(TestService.class, new TestServiceImpl());
    }

    @Test
    void getExtension() {
        Assert.isInstanceOf(TestService.class, extensionManager.getExtension(TestService.class));
    }

    @Test
    void getInstance() {
        Assert.isInstanceOf(ExtensionManager.class, extensionManager);
    }

    @Test
    void getPluginManager() {
        Assert.isInstanceOf(PluginManager.class, pluginManager);
    }

}