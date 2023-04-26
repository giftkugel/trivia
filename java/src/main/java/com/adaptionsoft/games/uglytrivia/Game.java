package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.List;

public class Game {

	private final MessageCollector messageCollector;
	private final List<Player> players = new ArrayList<>();

	private final QuestionBox questionBox = new QuestionBox();

    int currentPlayerIndex = 0;

    public Game(final MessageCollector messageCollector) {
		this.messageCollector = messageCollector;
		initializeQuestions();
	}

	public boolean isPlayable() {
		return (howManyPlayers() >= 2);
	}

	public boolean add(final String playerName) {
		players.add(new Player(playerName));

		writeMessage(playerName + " was added");
		writeMessage("They are player number " + howManyPlayers());
		return true;
	}

	public int howManyPlayers() {
		return players.size();
	}

	public void roll(final int roll) {
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

	private void createQuestion(final Category category, final int index) {
		Question question =  new Question(index, category);
		questionBox.addQuestion(question);
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
		Question question = questionBox.getNextQuestion(currentCategory());
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
		return Category.getCategoryForPlace(currentPlace);
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
