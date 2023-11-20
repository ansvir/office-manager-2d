package com.tohant.om2d.di.util;

import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.di.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TestComponent {

    private final AnotherTestComponent anotherTestComponent;
    private final GameCache gameCache;

    @Getter
    private String testString;

    @PostConstruct
    public void init() {
        testString = "TEST_STRING";
    }

    public String getAnotherTestComponentTestString() {
        return anotherTestComponent.getTestString();
    }

    public String getGameCacheString() {
        return gameCache.getString();
    }

}
