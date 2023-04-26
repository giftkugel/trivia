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

	public void createQuestion(Category category, int index) {
		Question question =  new Question(index, category);
		questions.computeIfAbsent(category, key -> new LinkedList<>()).addLast(question);
	}

	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	public boolean add(String playerName) {
		players.add(new Player(playerName));

		writeMessage(playerName + " was added");
		writeMessage("They are player number " + howManyPlayers());
		return true;
	}

	public int howManyPlayers() {
		return players.size();
	}

	public void roll(int roll) {
		writeMessageForCurrentPlayer(" is the current player");
		writeMessage("They have rolled a " + roll);

		checkPenaltyBox(roll);

		boolean playerNotInPenaltyBox = !getCurrentPlayer().isInPenaltyBox();
		boolean playerWillLeavePenaltyBox = getCurrentPlayer().isInPenaltyBox() && getCurrentPlayer().isGettingOutOfPenaltyBox();

		if (playerNotInPenaltyBox || playerWillLeavePenaltyBox) {
			calculatePlayerPlace(roll);
			askQuestion();
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

	public boolean wrongAnswer() {
		writeMessage("Question was incorrectly answered");
		writeMessageForCurrentPlayer(" was sent to the penalty box");
		getCurrentPlayer().setInPenaltyBox(true);

		chooseNextPlayer();
		return true;
	}

	private void initializeQuestions() {
		for (int i = 0; i < 50; i++) {
			createQuestion(Category.POP, i);
			createQuestion(Category.SCIENCE, i);
			createQuestion(Category.SPORTS, i);
			createQuestion(Category.ROCK, i);
		}
	}

	private boolean handleCorrectAnswer(final String successMessage) {
		writeMessage(successMessage);
		getCurrentPlayer().incrementPurse();
		writeMessageForCurrentPlayer(" now has " + getCurrentPlayer().getPurse() + " Gold Coins.");

		boolean winner = didPlayerWin();
		chooseNextPlayer();

		return winner;
	}

	private void calculatePlayerPlace(final int roll) {
		Player currentPlayer = getCurrentPlayer();
		int newPlace = currentPlayer.getPlace() + roll;

		if (newPlace > 11) {
			newPlace = newPlace - 12;
		}
		currentPlayer.setPlace(newPlace);

		writeMessageForCurrentPlayer("'s new location is " + currentPlayer.getPlace());
		writeMessage("The category is " + currentCategory().getName());
	}

	private void askQuestion() {
		Question question = questions.get(currentCategory()).removeFirst();
		writeMessage(question.toString());
	}

	private void checkPenaltyBox(final int roll) {
		if (getCurrentPlayer().isInPenaltyBox()) {
			boolean outOfPenaltyBox = roll % 2 != 0;
			getCurrentPlayer().setGettingOutOfPenaltyBox(outOfPenaltyBox);
			if (outOfPenaltyBox) {
				writeMessageForCurrentPlayer(" is getting out of the penalty box");
			} else {
				writeMessageForCurrentPlayer(" is not getting out of the penalty box");
			}
		}
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

	private void writeMessageForCurrentPlayer(final String message) {
		messageCollector.writeMessage(getCurrentPlayer() + message);
	}

	private void writeMessage(final String message) {
		messageCollector.writeMessage(message);
	}

	private void chooseNextPlayer() {
		currentPlayerIndex++;
		if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
	}

	private boolean didPlayerWin() {
		return getCurrentPlayer().getPurse() != 6;
	}

	private Player getCurrentPlayer() {
		return players.get(currentPlayerIndex);
	}
}
