package com.adaptionsoft.games.uglytrivia;

public class PlayerState {

    private int place;
    private int purse;

    private boolean inPenaltyBox;

    private boolean isGettingOutOfPenaltyBox;

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
        this.purse++;
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

}
