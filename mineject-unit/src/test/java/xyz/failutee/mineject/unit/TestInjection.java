package xyz.failutee.mineject.unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import xyz.failutee.mineject.Mineject;
import xyz.failutee.mineject.MinejectFactory;
import xyz.failutee.mineject.dependency.DependencyProvider;
import xyz.failutee.mineject.unit.elements.TestComponent;
import xyz.failutee.mineject.unit.elements.TestElement;

public class TestInjection {

    @Test
    void testInjection() {
        Mineject mineject = new MinejectFactory()
                .dependencySettings(settings -> settings.packageName("xyz.failutee.mineject.unit.elements"))
                .build(true);

        DependencyProvider dependencyProvider = mineject.getDependencyProvider();

        TestComponent testComponent = dependencyProvider.getDependency(TestComponent.class);

        TestElement testElement = testComponent.getTestElement();

        Assertions.assertNotNull(testComponent);
        Assertions.assertNotNull(testElement);
    }
}
