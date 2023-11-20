package com.tohant.om2d.di.util;

import com.tohant.om2d.di.annotation.Component;
import com.tohant.om2d.di.annotation.Inject;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ThirdTestComponent {


    private final AnotherTestComponent anotherTestComponent;

    public String getAnotherTestComponentTestString() {
        return anotherTestComponent.getTestString();
    }

}
