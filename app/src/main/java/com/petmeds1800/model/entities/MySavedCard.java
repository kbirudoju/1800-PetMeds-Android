package com.petmeds1800.model.entities;

import com.petmeds1800.model.Card;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Abhinav on 17/8/16.
 */
public class MySavedCard implements Serializable {

    private int cardsCount;
    private Status status;
    private ArrayList<Card> creditCardList;

    public int getCardsCount() {
        return cardsCount;
    }

    public void setCardsCount(int cardsCount) {
        this.cardsCount = cardsCount;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public ArrayList<Card> getCreditCardList() {
        return creditCardList;
    }

    public void setCreditCardList(ArrayList<Card> creditCardList) {
        this.creditCardList = creditCardList;
    }
}
