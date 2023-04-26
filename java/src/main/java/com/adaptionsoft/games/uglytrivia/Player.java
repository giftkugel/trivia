package com.adaptionsoft.games.uglytrivia;

public class Player {

    private final String name;
    private int place;
    private int purse;

    private boolean inPenaltyBox;

    private boolean isGettingOutOfPenaltyBox;

    public Player(final String name) {
        this.name = name;
        this.place = 0;
        this.purse = 0;
        this.inPenaltyBox = false;
        this.isGettingOutOfPenaltyBox = false;
    }

    public String getName() {
        return name;
    }

    public int getPlace() {
        return place;
    }

    public void setPlace(final int place) {
        this.place = place;
    }

    public int getPurse() {
        return purse;
    }

    public void incrementPurse() {
        purse++;
    }

    public boolean isInPenaltyBox() {
        return inPenaltyBox;
    }

    public void setInPenaltyBox(final boolean inPenaltyBox) {
        this.inPenaltyBox = inPenaltyBox;
    }

    public boolean isGettingOutOfPenaltyBox() {
        return isGettingOutOfPenaltyBox;
    }

    public void setGettingOutOfPenaltyBox(final boolean gettingOutOfPenaltyBox) {
        isGettingOutOfPenaltyBox = gettingOutOfPenaltyBox;
    }

    @Override
    public String toString() {
        return name;
    }
}
