package com.dewarder.pickerok;

import java.util.Random;

public final class RequestCodeGenerator {

    private static final Random RANDOM = new Random();

    private RequestCodeGenerator() {
        throw new UnsupportedOperationException();
    }

    public static int generate() {
        return RANDOM.nextInt(2048);
    }
}
