
package com.adaptionsoft.games.trivia.runner;
import java.util.Random;

import com.adaptionsoft.games.uglytrivia.Game;
import com.adaptionsoft.games.uglytrivia.MessageCollector;


public class GameRunner {

	public static void main(String[] args) {
		long seed = 42;
		if (args.length == 1) {
			seed = Long.parseLong(args[0]);
		}

		runGame(seed, new MessageCollector(System.out));
	}

	public static void runGame(final long seed, final MessageCollector messageCollector) {
		Game aGame = new Game(messageCollector);

		aGame.add("Chet");
		aGame.add("Pat");
		aGame.add("Sue");

		Random rand = new Random(seed);

		boolean notAWinner;
		do {

			aGame.roll(rand.nextInt(5) + 1);

			if (rand.nextInt(9) == 7) {
				notAWinner = aGame.wrongAnswer();
			} else {
				notAWinner = aGame.wasCorrectlyAnswered();
			}



		} while (notAWinner);
	}
}
