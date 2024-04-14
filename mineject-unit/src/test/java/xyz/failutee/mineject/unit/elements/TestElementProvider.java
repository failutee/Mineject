package xyz.failutee.mineject.unit.elements;

import xyz.failutee.mineject.annotation.Bean;
import xyz.failutee.mineject.annotation.BeanSetup;

@BeanSetup
class TestElementProvider {

    @Bean
    TestElement getTestElement() {
        return new TestElement();
    }
}
