package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Game {
    List<Player> players = new ArrayList<>();

	Map<Category, LinkedList<Question>> questions = new EnumMap<>(Category.class);

    int currentPlayer = 0;
    boolean isGettingOutOfPenaltyBox;

    public  Game(){
    	for (int i = 0; i < 50; i++) {
			createQuestion(Category.POP, i);
			createQuestion(Category.SCIENCE, i);
			createQuestion(Category.SPORTS, i);
			createQuestion(Category.ROCK, i);
    	}
    }

	public void createQuestion(Category category, int index) {
		Question question =  new Question(index, category);
		questions.computeIfAbsent(category, key -> new LinkedList<>()).addLast(question);
	}

	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	public boolean add(String playerName) {


	    players.add(new Player(playerName));

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

		if (getCurrentPlayer().isInPenaltyBox()) {
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
		Player currentPlayer = getCurrentPlayer();
		currentPlayer.calculatePlace(roll);


		System.out.println(currentPlayer.getName()
				+ "'s new location is "
				+ currentPlayer.getPlace());
		System.out.println("The category is " + currentCategory().getName());
	}

	private void askQuestion() {
		System.out.println(questions.get(currentCategory()).removeFirst());
	}


	private Category currentCategory() {
		int currentPlace = getCurrentPlayer().getPlace();
		int categoryValue = currentPlace % 4;
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

	public boolean wasCorrectlyAnswered() {
		if (getCurrentPlayer().isInPenaltyBox()){
			if (isGettingOutOfPenaltyBox) {
				System.out.println("Answer was correct!!!!");
				getCurrentPlayer().incrementPurse();
				System.out.println(players.get(currentPlayer)
						+ " now has "
						+ getCurrentPlayer().getPurse()
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
			getCurrentPlayer().incrementPurse();
			System.out.println(players.get(currentPlayer)
					+ " now has "
					+ getCurrentPlayer().getPurse()
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
		getCurrentPlayer().setInPenaltyBox(true);

		currentPlayer++;
		if (currentPlayer == players.size()) currentPlayer = 0;
		return true;
	}


	private boolean didPlayerWin() {
		return getCurrentPlayer().getPurse() != 6;
	}

	private Player getCurrentPlayer() {
		return players.get(currentPlayer);
	}
}
