package com.adaptionsoft.games.uglytrivia;

import java.util.Random;

public class Dice {

    private final Random rand;

    private int value;

    public Dice(final Random rand) {
        this.rand = rand;
    }

    public void roll() {
        value = rand.nextInt(5) + 1;
    }

    public int getValue() {
        return value;
    }
}
