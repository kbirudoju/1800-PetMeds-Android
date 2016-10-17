package com.petmeds1800.model.entities;

import com.petmeds1800.model.Card;

import java.io.Serializable;

/**
 * Created by Abhinav on 21/8/16.
 */
public class AddEditCardResponse implements Serializable {

    private Card card;

    private Status status;

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Card getCard() {
        return card;
    }

    public void setCard(Card card) {
        this.card = card;
    }
}
