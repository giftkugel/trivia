package com.adaptionsoft.games.trivia;


import com.adaptionsoft.games.trivia.runner.GameRunner;
import com.adaptionsoft.games.uglytrivia.MessageCollector;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SomeTest {

    private static final Random seedGenerator = new Random(1);

    private static List<Long> getSeeds() {
        var list = new ArrayList<Long>();
        for (int i = 0; i < 1_000; i++) {
            list.add(seedGenerator.nextLong());
        }
        return list;
    }

    private static Stream<Arguments> seedSource() {
        return getSeeds().stream()
                .map(Arguments::of);
    }

    @Test
    @Disabled("file creation")
    void generateGoldenMaster() throws Exception {
        for (var seed : getSeeds()) {
            var file = new File(String.format("golden_master/seed-%s.out", seed));

            var testOutput = new FileOutputStream(file);
            PrintStream ps = new PrintStream(testOutput);

            GameRunner.runGame(seed, new MessageCollector(ps));
        }

    }


    @ParameterizedTest
    @MethodSource("seedSource")
    void compareOutput(Long seed) throws Exception {
        var file = new File(String.format("golden_master/seed-%s.out", seed));
        var reference = Files.readString(file.toPath());

        var testOutput = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(testOutput);

        GameRunner.runGame(seed, new MessageCollector(ps));

        var output = testOutput.toString();
        assertEquals(output, reference);
    }

}
