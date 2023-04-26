package com.adaptionsoft.games.uglytrivia;

public enum Category {
    POP("Pop"),
    SCIENCE("Science"),
    SPORTS("Sports"),
    ROCK("Rock");

    private final String name;

    Category(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Category getCategoryForPlace(final int place) {
        int categoryValue = place % 4;
        switch (categoryValue) {
            case 0:
                return Category.POP;
            case 1:
                return Category.SCIENCE;
            case 2:
                return Category.SPORTS;
            default:
                return Category.ROCK;
        }
    }
}
