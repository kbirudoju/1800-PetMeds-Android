package com.petmeds1800.util;

/**
 * Created by pooja on 9/15/2016.
 */
public class Constants {

    /*WidgetTypes Constant*/
    public static final String VIEW_TYPE_BANNER = "Banner";

    public static enum RepeatFrequency {
        REPEAT_DAILY, REPEAT_WEEKLY, REPEAT_MONTHLY
    }

    public static enum WeekDays {
        SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURUSDAY, FRIDAY, SATURDAY
    }

    /*ShoppingCart Constants*/
    public static final int DELETE_ITEM_REQUEST_SHOPPINGCART = 1268;

    public static final int UPDATE_ITEM_QUANTITY_SHOPPINGCART = 1269;

    public static final int CLICK_ITEM_UPDATE_SHOPPINGCART = 1270;

    public static final String COMMERCE_ITEM_ID = "commerceItemId";

    public static final String QUANTITY_MAP = "QuantityMap";

    public static final String VIEW_TYPE_REFILL = "Refill";

    public static final String VIEW_TYPE_RECOMMENDATIONS = "Recommendation";

    public static final String VIEW_TYPE_TIP = "Tip";

    public static final String VIEW_TYPE_SALES_PITCH = "SalesPitch";

    public static final String VIEW_TYPE_WHATS_NEXT = "WhatsNext";

    public static final String VIEW_TYPE_BROWSE_HISTORY = "BrowseHistory";

    public static final int ADD_NEW_PET_REQUEST = 1;

    public static final int ADD_NEW_ADDRESS_REQUEST = 2;

    public static final int ADD_NEW_PAYMENT_METHOD = 3;

    public static final String CONFIRMATION_ORDER_RESPONSE = "confirmationOrderResponse";

    public static final int ADD_NEW_VET_REQUEST = 4;

    public static final int DIALOG_REMINDER_REQUEST = 5;

    public static final String DIALOG_DATA_TOKEN = "Dialog_data_token";

    public static final String IS_EDITABLE = "isEditable";

    public static final String MEDICATION_REMINDER_INFO = "medicationReminderInfo";


}
