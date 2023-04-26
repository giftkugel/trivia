package com.adaptionsoft.games.uglytrivia;

import java.util.EnumMap;
import java.util.LinkedList;
import java.util.Map;

public class QuestionBox {

    private final Map<Category, LinkedList<Question>> questions = new EnumMap<>(Category.class);

    public void addQuestion(final Question question) {
        questions.computeIfAbsent(question.getCategory(), key -> new LinkedList<>()).addLast(question);
    }

    public Question getNextQuestion(final Category category) {
        return questions.get(category).removeFirst();
    }

}
