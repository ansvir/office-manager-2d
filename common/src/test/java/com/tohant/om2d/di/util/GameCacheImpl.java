package com.tohant.om2d.di.util;

import com.tohant.om2d.di.annotation.Component;

@Component
public class GameCacheImpl implements GameCache {

    @Override
    public String getString() {
        return "TEST_GAME_CACHE_IMPL_STRING";
    }

}
