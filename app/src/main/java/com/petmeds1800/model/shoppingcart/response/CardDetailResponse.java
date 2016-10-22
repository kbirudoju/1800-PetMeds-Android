package com.petmeds1800.model.shoppingcart.response;

import com.petmeds1800.model.Card;

/**
 * Created by Abhinav on 24/10/16.
 */
public class CardDetailResponse {

    private Card creditCard;

    private Status status;

    public Card getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(Card creditCard) {
        this.creditCard = creditCard;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
