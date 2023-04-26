package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;

public class Game {
    ArrayList players = new ArrayList();
    int[] places = new int[6];
    int[] purses  = new int[6];
    boolean[] inPenaltyBox  = new boolean[6];

    LinkedList popQuestions = new LinkedList();
    LinkedList scienceQuestions = new LinkedList();
    LinkedList sportsQuestions = new LinkedList();
    LinkedList rockQuestions = new LinkedList();

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public  Game(){
    	for (int i = 0; i < 50; i++) {
			popQuestions.addLast(createQuestion(Category.POP, i));
			scienceQuestions.addLast(createQuestion(Category.SCIENCE, i));
			sportsQuestions.addLast(createQuestion(Category.SPORTS, i));
			rockQuestions.addLast(createQuestion(Category.ROCK, i));
    	}
    }

	public String createQuestion(Category category, int index){
		return String.format("%s Question %d", category.getName(), index);
	}

	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	public boolean add(String playerName) {


	    players.add(playerName);
	    places[howManyPlayers()] = 0;
	    purses[howManyPlayers()] = 0;
	    inPenaltyBox[howManyPlayers()] = false;

	    System.out.println(playerName + " was added");
	    System.out.println("They are player number " + players.size());
		return true;
	}

	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		System.out.println(players.get(currentPlayer) + " is the current player");
		System.out.println("They have rolled a " + roll);

		if (inPenaltyBox[currentPlayer]) {
			if (roll % 2 != 0) {
				isGettingOutOfPenaltyBox = true;

				System.out.println(players.get(currentPlayer) + " is getting out of the penalty box");
				calculatePlayerPlace(roll);
				askQuestion();
			} else {
				System.out.println(players.get(currentPlayer) + " is not getting out of the penalty box");
				isGettingOutOfPenaltyBox = false;
				}

		} else {

			calculatePlayerPlace(roll);
			askQuestion();
		}

	}

	private void calculatePlayerPlace(final int roll) {
		places[currentPlayer] = places[currentPlayer] + roll;
		if (places[currentPlayer] > 11) places[currentPlayer] = places[currentPlayer] - 12;

		System.out.println(players.get(currentPlayer)
				+ "'s new location is "
				+ places[currentPlayer]);
		System.out.println("The category is " + currentCategory().getName());
	}

	private void askQuestion() {
		if (currentCategory() == Category.POP)
			System.out.println(popQuestions.removeFirst());
		if (currentCategory() == Category.SCIENCE)
			System.out.println(scienceQuestions.removeFirst());
		if (currentCategory() == Category.SPORTS)
			System.out.println(sportsQuestions.removeFirst());
		if (currentCategory() == Category.ROCK)
			System.out.println(rockQuestions.removeFirst());
	}


	private Category currentCategory() {
		if (places[currentPlayer] == 0) return Category.POP;
		if (places[currentPlayer] == 4) return Category.POP;
		if (places[currentPlayer] == 8) return Category.POP;
		if (places[currentPlayer] == 1) return Category.SCIENCE;
		if (places[currentPlayer] == 5) return Category.SCIENCE;
		if (places[currentPlayer] == 9) return Category.SCIENCE;
		if (places[currentPlayer] == 2) return Category.SPORTS;
		if (places[currentPlayer] == 6) return Category.SPORTS;
		if (places[currentPlayer] == 10) return Category.SPORTS;
		return Category.ROCK;
	}

	public boolean wasCorrectlyAnswered() {
		if (inPenaltyBox[currentPlayer]){
			if (isGettingOutOfPenaltyBox) {
				System.out.println("Answer was correct!!!!");
				purses[currentPlayer]++;
				System.out.println(players.get(currentPlayer)
						+ " now has "
						+ purses[currentPlayer]
						+ " Gold Coins.");

				boolean winner = didPlayerWin();
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;

				return winner;
			} else {
				currentPlayer++;
				if (currentPlayer == players.size()) currentPlayer = 0;
				return true;
			}



		} else {

			System.out.println("Answer was corrent!!!!");
			purses[currentPlayer]++;
			System.out.println(players.get(currentPlayer)
					+ " now has "
					+ purses[currentPlayer]
					+ " Gold Coins.");

			boolean winner = didPlayerWin();
			currentPlayer++;
			if (currentPlayer == players.size()) currentPlayer = 0;

			return winner;
		}
	}

	public boolean wrongAnswer(){
		System.out.println("Question was incorrectly answered");
		System.out.println(players.get(currentPlayer)+ " was sent to the penalty box");
		inPenaltyBox[currentPlayer] = true;

		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
		return true;
	}


	private boolean didPlayerWin() {
		return !(purses[currentPlayer] == 6);
	}
}
