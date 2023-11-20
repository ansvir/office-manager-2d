package com.tohant.om2d.di.util;

import com.tohant.om2d.di.annotation.Dao;
import lombok.Getter;

@Dao
@Getter
public class DaoComponent extends BaseDaoImpl {

    private final AnotherTestComponent anotherTestComponent;

    public DaoComponent(AnotherTestComponent anotherTestComponent) {
        super(SampleEntity.class);
        this.anotherTestComponent = anotherTestComponent;
    }

}
