package com.adaptionsoft.games.uglytrivia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameState {

    private final Map<Player, PlayerState> playerState = new HashMap<>();

    private final List<Player> players = new ArrayList<>();

    private int currentPlayerIndex = 0;

    public void addPlayer(final Player player) {
        players.add(player);
    }

    public int howManyPlayers() {
        return players.size();
    }

    public int getPlace() {
        return getPlayerState().getPlace();
    }

    public void updatePlaceWithRoll(final int roll) {
        int newPlace = getPlayerState().getPlace() + roll;

        if (newPlace > 11) {
            newPlace = newPlace - 12;
        }
        getPlayerState().setPlace(newPlace);
    }

    public int getPurse() {
        return getPlayerState().getPurse();
    }

    public void incrementPurse() {
        getPlayerState().incrementPurse();
    }

    public boolean isInPenaltyBox() {
        return getPlayerState().isInPenaltyBox();
    }

    public void setInPenaltyBox(final boolean inPenaltyBox) {
        getPlayerState().setInPenaltyBox(inPenaltyBox);
    }

    public boolean isGettingOutOfPenaltyBox() {
        return getPlayerState().isGettingOutOfPenaltyBox();
    }

    public void setGettingOutOfPenaltyBox(final boolean gettingOutOfPenaltyBox) {
        getPlayerState().setGettingOutOfPenaltyBox(gettingOutOfPenaltyBox);
    }

    public Player getCurrentPlayer() {
        return players.get(currentPlayerIndex);
    }

    public void chooseNextPlayer() {
        currentPlayerIndex++;
        if (currentPlayerIndex == players.size()) currentPlayerIndex = 0;
    }

    private PlayerState getPlayerState() {
        return playerState.computeIfAbsent(getCurrentPlayer(), key -> new PlayerState());
    }
}
