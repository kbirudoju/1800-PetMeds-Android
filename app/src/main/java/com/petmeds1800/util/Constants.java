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

    public static final int SHOW_VET_ON_MAP = 5;

    public static final int REFRESH_VET_DATA = 6;

    public static final int STORAGE_ACCESS_REQUEST_CODE = 111;

    public static final int CALL_PHONE_REQUEST_CODE = 222;

    public static final int DIALOG_REMINDER_REQUEST = 5;

    public static final int DIALOG_REMINDER_PET_NAME_REQUEST = 6;

    public static final String DIALOG_DATA_TOKEN = "Dialog_data_token";

    public static final String IS_EDITABLE = "isEditable";

    public static final String FROM_PUSH = "fromPush";

    public static final String MEDICATION_REMINDER_INFO = "medicationReminderInfo";

    public static final String PET_NAME = "petName";

    public static final String IS_CANCEL_ALARM = "isCancelAlarm";

    public static final String MEDICATION_RESULT_RECEIVER = "medicationResultReceiver";

    public static final String RESULT_VALUE = "resultValue";

    public static final String SUCCESS = "success";

//    Refill Reminder Constants

    public static final int REFILL_LIST_PER_PET_SORT_COMPLETE = 1271;

    public static final int REFILL_LIST_PER_PET_EDIT = 1272;

    public static final String REFILL_LIST_PER_PET_SKUID = "refill_list_per_pet_skuid";

    public static final String REFILL_LIST_PER_PET_IMAGE_URL = "refill_list_per_pet_image_url";

    public static final String REFILL_LIST_PER_PET_PRICE = "refill_list_per_pet_price";

    public static final String REFILL_LIST_PER_PET_MONTH = "refill_list_per_pet_month";

    public static final String REFILL_LIST_PER_PET_NAME = "refill_list_per_pet_name";

    public static final String REFILL_LIST_PER_PET_QUANTITY_REMAINING = "refill_list_per_pet_quantity_remaining";

    public static final String REFILL_LIST_PER_PET_ORDER_ID = "refill_list_per_pet_order_id";

    public static final String REFILL_LIST_PER_PET_ITEM_ID = "refill_list_per_pet_item_id";

    public static final String REFILL_LIST_PER_PET_ = "refill_list_per_pet_";

//	General Keys for Animation or Progress Dialog Visibility Control

    public static boolean SHOW_PROGRESSBAR_OR_ANIMATION = true;

    public static boolean HIDE_PROGRESSBAR_OR_ANIMATION = false;

//  Keys for Async Update of Shopping Cart Icon Count

    public static final int KEY_COMPLETED_ASYN_COUNT_FETCH = 1268;

    public static final String KEY_SHOPPING_CART_ICON_VALUE = "shopping_cart_icon_value";

    public static final String KEY_SHOPPING_CART_ASYNC_SUCCESS = "shopping_cart_async_success";

    public static final String KEY_CART_FRAGMENT_INTENT_FILTER = "cart_fragment_intent_filter";

    public static final String KEY_PAYMENT_INTENT_FILTER = "payment_intent_filter";

    public static final String KEY_AUTHENTICATION_SUCCESS = "authentication_success_event";
    public static final String PUSH_EXTRA_ID = "id";
    public static final String PUSH_SCREEN_TYPE = "screenType";
    public static final String PUSH_TYPE = "type";
    public static final String PUSH_TARGET = "target";
    public static final String ORDER_ID_KEY = "orderId";

    public static final String KEY_HOME_ROOT_SESSION_CONFIRMATION = "session_confirmation";


}
