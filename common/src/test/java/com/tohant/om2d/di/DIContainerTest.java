package com.tohant.om2d.di;

import com.tohant.om2d.di.util.DaoComponent;
import com.tohant.om2d.di.util.SampleEntity;
import com.tohant.om2d.di.util.TestComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DIContainerTest {

    @Test
    public void testResolveImplementation() {
        // given
        DIContainer diContainer = Assertions.assertDoesNotThrow(() -> new DIContainer(getClass()));
        // when
        TestComponent testComponent = Assertions
                .assertDoesNotThrow(() -> diContainer.resolve(TestComponent.class));
        DaoComponent daoComponent = Assertions
                .assertDoesNotThrow(() -> diContainer.resolve(DaoComponent.class));
        // then
        Assertions.assertEquals("TEST_STRING", testComponent.getTestString());
        Assertions.assertEquals("ANOTHER_TEST_COMPONENT_TEST_STRING", testComponent.getAnotherTestComponentTestString());
        Assertions.assertEquals(SampleEntity.class, daoComponent.getClazz());
        Assertions.assertEquals("ANOTHER_TEST_COMPONENT_TEST_STRING", daoComponent.getAnotherTestComponent().getTestString());
        Assertions.assertEquals(testComponent.getGameCacheString(), "TEST_GAME_CACHE_IMPL_STRING");
    }

}
