package xyz.failutee.mineject.unit.elements;

import xyz.failutee.mineject.annotation.Component;
import xyz.failutee.mineject.annotation.Injectable;

@Component
public class TestComponent {

    private final TestElement testElement;

    @Injectable
    public TestComponent(TestElement testElement) {
        this.testElement = testElement;
    }

    public TestElement getTestElement() {
        return this.testElement;
    }
}
