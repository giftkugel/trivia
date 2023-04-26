package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Game {
    List<Player> players = new ArrayList<>();

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
		int currentPlace = getCurrentPlayer().getPlace();
		if (currentPlace == 0) return Category.POP;
		if (currentPlace == 4) return Category.POP;
		if (currentPlace == 8) return Category.POP;
		if (currentPlace == 1) return Category.SCIENCE;
		if (currentPlace == 5) return Category.SCIENCE;
		if (currentPlace == 9) return Category.SCIENCE;
		if (currentPlace == 2) return Category.SPORTS;
		if (currentPlace == 6) return Category.SPORTS;
		if (currentPlace == 10) return Category.SPORTS;
		return Category.ROCK;
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
		return !(getCurrentPlayer().getPurse() == 6);
	}

	private Player getCurrentPlayer() {
		return players.get(currentPlayer);
	}
}
