
package com.adaptionsoft.games.trivia.runner;
import java.util.Random;

import com.adaptionsoft.games.uglytrivia.Game;


public class GameRunner {

	private static boolean notAWinner;

	public static void main(String[] args) {
		long seed = 42;
		if (args.length == 1) {
			seed = Long.parseLong(args[0]);
		}
		Game aGame = new Game();

		aGame.add("Chet");
		aGame.add("Pat");
		aGame.add("Sue");

		Random rand = new Random(seed);

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
