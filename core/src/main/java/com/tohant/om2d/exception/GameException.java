package com.tohant.om2d.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class GameException extends RuntimeException {

    private final Code code;

    public GameException(Code code) {
        super(code.getMessage());
        this.code = code;
    }

    @Getter
    @AllArgsConstructor
    public enum Code {
        E000("Room must be destroyed before!", Type.WARNING),
        E100("Room can be placed only next to hall!", Type.WARNING),
        E200("At least one hall is required!", Type.WARNING),
        E300("One single hall near room cannot be destroyed!", Type.WARNING),
        E400("No more than 3 games can be saved at a time!", Type.WARNING),
        E500("Insufficient funds to build a new office!", Type.WARNING),
        E600("You are already have an office in that region!", Type.WARNING),
        E700("Not enough budget to build a new office!", Type.WARNING);

        private final String message;
        private final Type type;

    }

    @Getter
    @AllArgsConstructor
    public enum Type {
        WARNING("Warning!"), HELP("Help");

        private final String title;

    }

}
