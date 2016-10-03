package com.petmeds1800.model.entities;

import java.util.ArrayList;

/**
 * Created by Abhinav on 24/9/16.
 */
public class CheckoutSteps {

    private StepState stepState;
    private ArrayList<String> applicableSteps;
    private int totalStepCount;

    public StepState getStepState() {
        return stepState;
    }

    public void setStepState(StepState stepState) {
        this.stepState = stepState;
    }

    public ArrayList<String> getApplicableSteps() {
        return applicableSteps;
    }

    public void setApplicableSteps(ArrayList<String> applicableSteps) {
        this.applicableSteps = applicableSteps;
    }

    public int getTotalStepCount() {
        return totalStepCount;
    }

    public void setTotalStepCount(int totalStepCount) {
        this.totalStepCount = totalStepCount;
    }
}
