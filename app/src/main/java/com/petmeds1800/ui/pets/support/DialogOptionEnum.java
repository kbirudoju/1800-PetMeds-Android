package com.petmeds1800.ui.pets.support;

/**
 * Created by pooja on 9/9/2016.
 */
public enum DialogOptionEnum {
    PET_PASSED(1),PET_SOLD(2),MALE(3),FEMALE(4);
    private int value;

    DialogOptionEnum(int value) {
        this.value=value;
    }

    public int getValue() {
        return value;
    }
    public static String getSelectedKey(int id) {
        DialogOptionEnum optionEnum = null;
        switch (id) {
            case 1:
                optionEnum = PET_PASSED;
                break;
            case 2:
                optionEnum = PET_SOLD;
                break;
            case 3:
                optionEnum = MALE;
                break;
            case 4:
                optionEnum = FEMALE;
                break;


        }
        return optionEnum.name();
    }

}
