package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Game {

	MessageCollector messageCollector;
    List<Player> players = new ArrayList<>();

	Map<Category, LinkedList<Question>> questions = new EnumMap<>(Category.class);

    int currentPlayerIndex = 0;

    public  Game(final MessageCollector messageCollector) {
		this.messageCollector = messageCollector;
		initializeQuestions();
	}

	private void initializeQuestions() {
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

		messageCollector.writeMessage(playerName + " was added");
		messageCollector.writeMessage("They are player number " + players.size());
		return true;
	}

	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		messageCollector.writeMessage(players.get(currentPlayerIndex) + " is the current player");
		messageCollector.writeMessage("They have rolled a " + roll);

		if (getCurrentPlayer().isInPenaltyBox()) {
			if (roll % 2 != 0) {
				getCurrentPlayer().setGettingOutOfPenaltyBox(true);

				messageCollector.writeMessage(players.get(currentPlayerIndex) + " is getting out of the penalty box");
				calculatePlayerPlace(roll);
				askQuestion();
			} else {
				messageCollector.writeMessage(players.get(currentPlayerIndex) + " is not getting out of the penalty box");
				getCurrentPlayer().setGettingOutOfPenaltyBox(false);
			}
		} else {
			calculatePlayerPlace(roll);
			askQuestion();
		}

	}

	private void calculatePlayerPlace(final int roll) {
		Player currentPlayer = getCurrentPlayer();
		currentPlayer.calculatePlace(roll);


		messageCollector.writeMessage(currentPlayer.getName()
				+ "'s new location is "
				+ currentPlayer.getPlace());
		messageCollector.writeMessage("The category is " + currentCategory().getName());
	}

	private void askQuestion() {
		Question question = questions.get(currentCategory()).removeFirst();
		messageCollector.writeMessage(question.toString());
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
			if (getCurrentPlayer().isGettingOutOfPenaltyBox()) {
				return handleCorrectAnswer("Answer was correct!!!!");
			} else {
				chooseNextPlayer();
				return true;
			}
		} else {
			return handleCorrectAnswer("Answer was corrent!!!!");
		}
	}

	private boolean handleCorrectAnswer(final String successMessage) {
		messageCollector.writeMessage(successMessage);
		getCurrentPlayer().incrementPurse();
		messageCollector.writeMessage(players.get(currentPlayerIndex)
				+ " now has "
				+ getCurrentPlayer().getPurse()
				+ " Gold Coins.");

		boolean winner = didPlayerWin();
		chooseNextPlayer();

		return winner;
	}

	private void chooseNextPlayer() {
		currentPlayerIndex++;
		if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
	}

	public boolean wrongAnswer(){
		messageCollector.writeMessage("Question was incorrectly answered");
		messageCollector.writeMessage(players.get(currentPlayerIndex)+ " was sent to the penalty box");
		getCurrentPlayer().setInPenaltyBox(true);

		chooseNextPlayer();
		return true;
	}


	private boolean didPlayerWin() {
		return getCurrentPlayer().getPurse() != 6;
	}

	private Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}
}
