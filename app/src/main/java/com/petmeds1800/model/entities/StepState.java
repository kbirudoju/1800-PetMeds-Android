package com.petmeds1800.model.entities;

/**
 * Created by Abhinav on 24/9/16.
 */
public class StepState {

    private boolean reviewComplete;
    private String lastCompletedStep;
    private boolean showPetVetStep;
    private boolean shipMethodComplete;
    private String nextCheckoutStep;
    private boolean paymentComplete;
    private boolean shippAddressComplete;
    private boolean petvetComplete;

    public boolean isReviewComplete() {
        return reviewComplete;
    }

    public void setReviewComplete(boolean reviewComplete) {
        this.reviewComplete = reviewComplete;
    }

    public String getLastCompletedStep() {
        return lastCompletedStep;
    }

    public void setLastCompletedStep(String lastCompletedStep) {
        this.lastCompletedStep = lastCompletedStep;
    }

    public boolean isShowPetVetStep() {
        return showPetVetStep;
    }

    public void setShowPetVetStep(boolean showPetVetStep) {
        this.showPetVetStep = showPetVetStep;
    }

    public boolean isShipMethodComplete() {
        return shipMethodComplete;
    }

    public void setShipMethodComplete(boolean shipMethodComplete) {
        this.shipMethodComplete = shipMethodComplete;
    }

    public String getNextCheckoutStep() {
        return nextCheckoutStep;
    }

    public void setNextCheckoutStep(String nextCheckoutStep) {
        this.nextCheckoutStep = nextCheckoutStep;
    }

    public boolean isPaymentComplete() {
        return paymentComplete;
    }

    public void setPaymentComplete(boolean paymentComplete) {
        this.paymentComplete = paymentComplete;
    }

    public boolean isShippAddressComplete() {
        return shippAddressComplete;
    }

    public void setShippAddressComplete(boolean shippAddressComplete) {
        this.shippAddressComplete = shippAddressComplete;
    }

    public boolean isPetvetComplete() {
        return petvetComplete;
    }

    public void setPetvetComplete(boolean petvetComplete) {
        this.petvetComplete = petvetComplete;
    }
}
