package com.adaptionsoft.games.uglytrivia;

public class Question {

    private final String name;
    private final Category category;

    public Question(final int index, final Category category) {
        this.name = String.format("%s Question %d", category.getName(), index);
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public Category getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return name;
    }
}
