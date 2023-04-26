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
		messageCollector.writeMessage("They are player number " + howManyPlayers());
		return true;
	}

	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		messageCollector.writeMessage(getCurrentPlayer() + " is the current player");
		messageCollector.writeMessage("They have rolled a " + roll);

		boolean outOfPenaltyBox = roll % 2 != 0;
		if (getCurrentPlayer().isInPenaltyBox()) {
			getCurrentPlayer().setGettingOutOfPenaltyBox(outOfPenaltyBox);
			if (outOfPenaltyBox) {
				messageCollector.writeMessage(getCurrentPlayer() + " is getting out of the penalty box");
			} else {
				messageCollector.writeMessage(getCurrentPlayer() + " is not getting out of the penalty box");
			}
		}

		boolean playerNotInPenaltyBox = !getCurrentPlayer().isInPenaltyBox();
		boolean playerWillLeavePenaltyBox = getCurrentPlayer().isInPenaltyBox() && getCurrentPlayer().isGettingOutOfPenaltyBox();

		if (playerNotInPenaltyBox || playerWillLeavePenaltyBox) {
			calculatePlayerPlace(roll);
			askQuestion();
		}

	}

	private void calculatePlayerPlace(final int roll) {
		Player currentPlayer = getCurrentPlayer();
		currentPlayer.calculatePlace(roll);


		messageCollector.writeMessage(getCurrentPlayer()
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

	public boolean correctAnswer() {
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
		messageCollector.writeMessage(getCurrentPlayer()
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

	public boolean wrongAnswer() {
		messageCollector.writeMessage("Question was incorrectly answered");
		messageCollector.writeMessage(getCurrentPlayer() + " was sent to the penalty box");
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
