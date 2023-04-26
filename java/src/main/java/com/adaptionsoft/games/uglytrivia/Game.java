package com.adaptionsoft.games.uglytrivia;


import java.util.stream.IntStream;

public class Game {

    private final MessageCollector messageCollector;

    private final QuestionBox questionBox = new QuestionBox();

    private final GameState gameState = new GameState();

    public Game(final MessageCollector messageCollector) {
        this.messageCollector = messageCollector;
        initializeQuestions();
    }

    public boolean isPlayable() {
        return (gameState.howManyPlayers() >= 2);
    }

    public boolean add(final String playerName) {
        gameState.addPlayer(new Player(playerName));

        writeMessage(playerName + " was added");
        writeMessage("They are player number " + gameState.howManyPlayers());
        return true;
    }

    public void handleDiceValue(final Dice dice) {
        int diceValue = dice.getValue();
        writeMessageForCurrentPlayer(" is the current player");
        writeMessage("They have rolled a " + diceValue);

        checkPenaltyBox(diceValue);

        boolean playerIsPenaltyBox = gameState.isInPenaltyBox();
        boolean playerWillLeavePenaltyBox = playerIsPenaltyBox && gameState.isGettingOutOfPenaltyBox();

        if (!playerIsPenaltyBox || playerWillLeavePenaltyBox) {
            calculatePlayerPlace(diceValue);
            askQuestion();
        }

    }

    public boolean correctAnswer() {
        boolean playerIsPenaltyBox = gameState.isInPenaltyBox();
        if (playerIsPenaltyBox){
            boolean playerWillLeavePenaltyBox = gameState.isGettingOutOfPenaltyBox();
            if (playerWillLeavePenaltyBox) {
                return handleCorrectAnswer("Answer was correct!!!!");
            } else {
                gameState.chooseNextPlayer();
                return true;
            }
        } else {
            return handleCorrectAnswer("Answer was corrent!!!!");
        }
    }

    public boolean wrongAnswer() {
        writeMessage("Question was incorrectly answered");
        writeMessageForCurrentPlayer(" was sent to the penalty box");
        gameState.setInPenaltyBox(true);

        gameState.chooseNextPlayer();
        return true;
    }

    private void initializeQuestions() {
        IntStream.range(0, 50).forEach(this::createQuestionsForIndex);
    }

    private void createQuestionsForIndex(final int index) {
        createQuestion(Category.POP, index);
        createQuestion(Category.SCIENCE, index);
        createQuestion(Category.SPORTS, index);
        createQuestion(Category.ROCK, index);
    }

    private void createQuestion(final Category category, final int index) {
        Question question = new Question(index, category);
        questionBox.addQuestion(question);
    }

    private boolean handleCorrectAnswer(final String successMessage) {
        writeMessage(successMessage);
        gameState.incrementPurse();
        writeMessageForCurrentPlayer(" now has " + gameState.getPurse() + " Gold Coins.");

        boolean winner = didPlayerWin();
        gameState.chooseNextPlayer();

        return winner;
    }

    private void calculatePlayerPlace(final int roll) {
        gameState.updatePlaceWithRoll(roll);

        writeMessageForCurrentPlayer("'s new location is " + gameState.getPlace());
        writeMessage("The category is " + currentCategory().getName());
    }

    private void askQuestion() {
        Question question = questionBox.getNextQuestion(currentCategory());
        writeMessage(question.toString());
    }

    private void checkPenaltyBox(final int roll) {
        boolean playerIsPenaltyBox = gameState.isInPenaltyBox();
        if (playerIsPenaltyBox) {
            boolean outOfPenaltyBox = roll % 2 != 0;
            gameState.setGettingOutOfPenaltyBox(outOfPenaltyBox);
            if (outOfPenaltyBox) {
                writeMessageForCurrentPlayer(" is getting out of the penalty box");
            } else {
                writeMessageForCurrentPlayer(" is not getting out of the penalty box");
            }
        }
    }

    private Category currentCategory() {
        int currentPlace = gameState.getPlace();
        return Category.getCategoryForPlace(currentPlace);
    }

    private void writeMessageForCurrentPlayer(final String message) {
        messageCollector.writeMessage(gameState.getCurrentPlayer() + message);
    }

    private void writeMessage(final String message) {
        messageCollector.writeMessage(message);
    }

    private boolean didPlayerWin() {
        return gameState.getPurse() != 6;
    }

}
