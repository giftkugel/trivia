package com.adaptionsoft.games.uglytrivia;

public class Player {

    private final String name;
    private int place;
    private int purse;

    private boolean inPenaltyBox;

    public Player(final String name) {
        this.name = name;
        this.place = 0;
        this.purse = 0;
        this.inPenaltyBox = false;
    }

    public String getName() {
        return name;
    }

    public int getPlace() {
        return place;
    }

    public void calculatePlace(final int roll) {
        place = place + roll;

        if (place > 11) place = place - 12;
    }

    public int getPurse() {
        return purse;
    }

    public void setPurse(final int purse) {
        this.purse = purse;
    }

    public boolean isInPenaltyBox() {
        return inPenaltyBox;
    }

    public void setInPenaltyBox(final boolean inPenaltyBox) {
        this.inPenaltyBox = inPenaltyBox;
    }

    @Override
    public String toString() {
        return name;
    }
}
