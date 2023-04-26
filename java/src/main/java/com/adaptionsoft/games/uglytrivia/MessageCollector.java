package com.adaptionsoft.games.uglytrivia;

import java.io.PrintStream;

public class MessageCollector {

    private final PrintStream printStream;

    public MessageCollector(final PrintStream printStream) {
        this.printStream = printStream;
    }

    public void writeMessage(final String message) {
        printStream.println(message);
    }
}
