package com.tohant.om2d.di.util;

import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.di.annotation.PostConstruct;
import lombok.Getter;

@Component
@Getter
public class AnotherTestComponent {

    private String testString;

    @PostConstruct
    public void init() {
        testString = "ANOTHER_TEST_COMPONENT_TEST_STRING";
    }

}
