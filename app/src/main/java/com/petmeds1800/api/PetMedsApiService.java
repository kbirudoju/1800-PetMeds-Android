package com.petmeds1800.api;

import com.petmeds1800.PetMedsApplication;
import com.petmeds1800.model.AddRecentlyItemToCart;
import com.petmeds1800.model.AddToCartRequest;
import com.petmeds1800.model.AddVetRequest;
import com.petmeds1800.model.AddVetResponse;
import com.petmeds1800.model.ContactUsResponse;
import com.petmeds1800.model.CountryListResponse;
import com.petmeds1800.model.PayPalCheckoutRequest;
import com.petmeds1800.model.PetImageUploadResponse;
import com.petmeds1800.model.ProductCategoryListResponse;
import com.petmeds1800.model.ReOrderRequest;
import com.petmeds1800.model.ReOrderResponse;
import com.petmeds1800.model.RemoveAddressRequest;
import com.petmeds1800.model.RemoveCardRequest;
import com.petmeds1800.model.RemoveVetRequest;
import com.petmeds1800.model.SearchVetByZipCodeResponse;
import com.petmeds1800.model.StatesListResponse;
import com.petmeds1800.model.UpdateVetRequest;
import com.petmeds1800.model.entities.AddAddressResponse;
import com.petmeds1800.model.entities.AddEditCardResponse;
import com.petmeds1800.model.entities.AddMedicationReminderRequest;
import com.petmeds1800.model.entities.AddMedicationReminderResponse;
import com.petmeds1800.model.entities.AddPetRequest;
import com.petmeds1800.model.entities.AddPetResponse;
import com.petmeds1800.model.entities.AddressRequest;
import com.petmeds1800.model.entities.AgeListResponse;
import com.petmeds1800.model.entities.CardRequest;
import com.petmeds1800.model.entities.CheckResetPasswordTokenRequest;
import com.petmeds1800.model.entities.CommitOrderRequest;
import com.petmeds1800.model.entities.CommitOrderResponse;
import com.petmeds1800.model.entities.CreditCardPaymentMethodRequest;
import com.petmeds1800.model.entities.EasyRefillReminder;
import com.petmeds1800.model.entities.ForgotPasswordRequest;
import com.petmeds1800.model.entities.ForgotPasswordResponse;
import com.petmeds1800.model.entities.InitCheckoutResponse;
import com.petmeds1800.model.entities.LoginRequest;
import com.petmeds1800.model.entities.LoginResponse;
import com.petmeds1800.model.entities.MedicationReminderListResponse;
import com.petmeds1800.model.entities.MyOrder;
import com.petmeds1800.model.entities.MySavedAddress;
import com.petmeds1800.model.entities.MySavedCard;
import com.petmeds1800.model.entities.OrderDetailResponse;
import com.petmeds1800.model.entities.OrderHistoryFilter;
import com.petmeds1800.model.entities.OrderReviewSubmitResponse;
import com.petmeds1800.model.entities.PasswordResetResponse;
import com.petmeds1800.model.entities.PetBreedTypeListResponse;
import com.petmeds1800.model.entities.PetEducationCategoriesResponse;
import com.petmeds1800.model.entities.PetList;
import com.petmeds1800.model.entities.PetMedicalConditionResponse;
import com.petmeds1800.model.entities.PetMedicationResponse;
import com.petmeds1800.model.entities.PetTypesListResponse;
import com.petmeds1800.model.entities.Profile;
import com.petmeds1800.model.entities.PushNotificationRequest;
import com.petmeds1800.model.entities.PushNotificationResponse;
import com.petmeds1800.model.entities.RemoveMedicationReminderRequest;
import com.petmeds1800.model.entities.RemoveMedicationReminderResponse;
import com.petmeds1800.model.entities.RemovePetRequest;
import com.petmeds1800.model.entities.RemovePetResponse;
import com.petmeds1800.model.entities.ResetPasswordResponse;
import com.petmeds1800.model.entities.SavePetVetRequest;
import com.petmeds1800.model.entities.SaveResetPasswordRequest;
import com.petmeds1800.model.entities.SavedShippingAddressRequest;
import com.petmeds1800.model.entities.SecurityStatusResponse;
import com.petmeds1800.model.entities.SessionConfNumberResponse;
import com.petmeds1800.model.entities.SessionConfigRequest;
import com.petmeds1800.model.entities.ShippingAddressRequest;
import com.petmeds1800.model.entities.ShippingMethodsRequest;
import com.petmeds1800.model.entities.ShippingMethodsResponse;
import com.petmeds1800.model.entities.SignOutResponse;
import com.petmeds1800.model.entities.SignUpRequest;
import com.petmeds1800.model.entities.SignUpResponse;
import com.petmeds1800.model.entities.UpdateAccountSettingsRequest;
import com.petmeds1800.model.entities.UpdateAccountSettingsResponse;
import com.petmeds1800.model.entities.UpdateCardRequest;
import com.petmeds1800.model.entities.VetListResponse;
import com.petmeds1800.model.entities.WidgetListResponse;
import com.petmeds1800.model.refillreminder.request.RemoveRefillReminderRequest;
import com.petmeds1800.model.refillreminder.request.UpdateRefillReminderRequest;
import com.petmeds1800.model.refillreminder.response.MonthSelectListResponse;
import com.petmeds1800.model.refillreminder.response.RefillReminderListResponse;
import com.petmeds1800.model.shoppingcart.request.AddItemRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.request.ApplyCouponRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.request.CardDetailRequest;
import com.petmeds1800.model.shoppingcart.request.RemoveItemRequestShoppingCart;
import com.petmeds1800.model.shoppingcart.response.CardDetailResponse;
import com.petmeds1800.model.shoppingcart.response.ShoppingCartListResponse;
import com.petmeds1800.model.shoppingcart.response.Status;

import java.util.HashMap;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Digvijay on 8/10/2016.
 */
public interface PetMedsApiService {



    @Headers({"Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/atg/rest/SessionConfirmationActor/getSessionConfirmationNumber")
    Observable<SessionConfNumberResponse> getSessionConfirmationNumber();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/atg/userprofiling/ProfileActor/login")
    Observable<LoginResponse> login(@Body LoginRequest loginRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/atg/userprofiling/ProfileActor/forgotPasswordEmail")
    Observable<ForgotPasswordResponse> forgotPassword(@Body ForgotPasswordRequest forgotPasswordRequest);

    @Headers({"Content-Type: application/json", "Request-Credential:"+ PetMedsApplication.requestCredential})
    @GET("rest/model/atg/userprofiling/ProfileActor/orderHistory")
    Observable<MyOrder> getOrderList(@Query("_dynSessConf") String sessionConfirmation,
            @Query("filterId") String filterId);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("rest/model/atg/userprofiling/ProfileActor/orderHistoryFilterlist")
    Observable<OrderHistoryFilter> getOrderHistoryFilter(@Query("_dynSessConf") String sessionConfirmation);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/payment/PaymentActor/add")
    Observable<AddEditCardResponse> addPaymentCard(@Body CardRequest cardRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/payment/PaymentActor/list")
    Observable<MySavedCard> getSavedCards(@Query("_dynSessConf") String sessionConfirmation);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/atg/userprofiling/ProfileActor/summary")
    Observable<Profile> getAccountSettings();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/atg/userprofiling/ProfileActor/update")
    Observable<UpdateAccountSettingsResponse> updateAccountSettings(
            @Body UpdateAccountSettingsRequest updateAccountSettingsRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/contact/ContactActor/list")
    Observable<MySavedAddress> getSavedAddress(@Query("_dynSessConf") String sessionConfirmation);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/contact/ContactActor/add")
    Observable<AddAddressResponse> addAddress(@Body AddressRequest addressRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/pet/PetActor/list")
    Observable<PetList> getPetList();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("rest/model/1800petmeds/pet/PetActor/add")
    Observable<AddPetResponse> addPet(@Body AddPetRequest petRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/atg/userprofiling/ProfileActor/stateList")
    Observable<StatesListResponse> getUsaStatesList();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/atg/userprofiling/ProfileActor/countryList")
    Observable<CountryListResponse> getCountryList();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/contact/ContactActor/update")
    Observable<AddAddressResponse> updateAddress(@Body AddressRequest addressRequest);

    @Headers({"Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/atg/userprofiling/SecurityStatusActor/status")
    Observable<SecurityStatusResponse> getSecurityStatus();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/contact/ContactActor/remove")
    Observable<AddAddressResponse> removeAddress(@Body RemoveAddressRequest addressRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/contact/ContactActor/detailed")
    Observable<AddAddressResponse> getAddressById(@Query("_dynSessConf") String sessionConfirmation,
            @Query("addressId") String addressId);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/payment/PaymentActor/update")
    Observable<AddEditCardResponse> updateCard(@Body UpdateCardRequest updateCardRequestRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/reminder/ReminderActor/easyRefillReminder")
    Observable<EasyRefillReminder> getReminderList(@Query("_dynSessConf") String sessionConfirmation);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("rest/model/1800petmeds/pet/PetActor/update")
    Observable<AddPetResponse> updatePet(@Body AddPetRequest petRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("rest/model/1800petmeds/pet/PetActor/remove")
    Observable<RemovePetResponse> removePet(@Body RemovePetRequest removeRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/atg/userprofiling/ProfileActor/create")
    Observable<SignUpResponse> signUp(@Body SignUpRequest signUpRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/atg/userprofiling/ProfileActor/logout")
    Observable<SignOutResponse> logout(@Body SessionConfigRequest sessionConfigRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/payment/PaymentActor/remove")
    Observable<AddEditCardResponse> removeCard(@Body RemoveCardRequest removeCardRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/pet/PetActor/medications")
    Observable<PetMedicationResponse> getPetMedicationList();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/pet/PetActor/ageOptions")
    Observable<AgeListResponse> getPetAgeList();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/pet/PetActor/types")
    Observable<PetTypesListResponse> getPetTypesList();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/pet/PetActor/breeds")
    Observable<PetBreedTypeListResponse> getPetBreedList();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/pet/PetActor/medConditions")
    Observable<PetMedicalConditionResponse> getPetPetMedicalConditions();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/home/HomeActor/loadWidgets")
    Observable<WidgetListResponse> getWidgetData();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/home/HomeActor/categoryUrls")
    Observable<ProductCategoryListResponse> getProductCategory();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/cart/CartActor/list")
    Observable<ShoppingCartListResponse> getShoppingCartList(@Query("_dynSessConf") String sessionConfirmation);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/checkout/CheckoutActor/listShippingMethods")
    Observable<ShippingMethodsResponse> getShippingMethods();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/checkout/CheckoutActor/shippingRatesWebView")
    Observable<String> getShippingOptions();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/checkout/CheckoutActor/checkout")
    Observable<InitCheckoutResponse> initializeCheckout(@Body HashMap<String, String> itemDetail);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/checkout/CheckoutActor/applySavedShippingAddress")
    Observable<ShoppingCartListResponse> saveShippingAddress(
            @Body SavedShippingAddressRequest savedShippingAddressRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/vet/VetActor/list")
    Observable<VetListResponse> getVetList();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/checkout/CheckoutActor/linkPetVet")
    Observable<ShoppingCartListResponse> savePetVet(@Body SavePetVetRequest savePetVetRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/checkout/CheckoutActor/orderReview")
    Observable<OrderReviewSubmitResponse> getOrderReviewDetails(
            @Query("_dynSessConf") String sessionConfirmation);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/checkout/CheckoutActor/applyShippingMethod")
    Observable<ShoppingCartListResponse> applyShippingMethods(@Body ShippingMethodsRequest shippingMethodsRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/checkout/CheckoutActor/applyCreditCardPaymentMethod")
    Observable<ShoppingCartListResponse> applyCreditCardPaymentMethod(
            @Body CreditCardPaymentMethodRequest creditCardPaymentMethodRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/cart/CartActor/list")
    Observable<ShoppingCartListResponse> getGeneralPopulateShoppingCart(
            @Query("_dynSessConf") String sessionConfirmation);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/cart/CartActor/add")
    Observable<ShoppingCartListResponse> getAddItemShoppingCart(
            @Body AddItemRequestShoppingCart addItemRequestShoppingCart);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/cart/CartActor/remove")
    Observable<ShoppingCartListResponse> getRemoveItemShoppingCart(
            @Body RemoveItemRequestShoppingCart removeItemRequestShoppingCart);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/cart/CartActor/applyCoupon")
    Observable<ShoppingCartListResponse> getApplyCouponShoppingCart(
            @Body ApplyCouponRequestShoppingCart applyCouponRequestShoppingCart);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/cart/CartActor/update")
    Observable<ShoppingCartListResponse> getUpdateItemQuantityRequestShoppingCart(
            @Body HashMap<String, String> getmCommerceIDQuantityMap);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/checkout/CheckoutActor/commitOrder")
    Observable<CommitOrderResponse> submitCommitedOrderDetails(@Body CommitOrderRequest request);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/checkout/CheckoutActor/applyNewShippingAddress")
    Observable<ShoppingCartListResponse> saveGuestShippingAddressData(@Body ShippingAddressRequest request);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/vet/VetActor/add")
    Observable<AddVetResponse> addVet(@Body AddVetRequest addVetRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/vet/VetActor/findByZip")
    Observable<SearchVetByZipCodeResponse> getVetByZipCode(@Query("zip") String zip);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/atg/userprofiling/ProfileActor/reOrder")
    Observable<ReOrderResponse> reOrder(@Body ReOrderRequest request);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/cart/CartActor/add")
    Observable<ShoppingCartListResponse> addToCart(@Body AddToCartRequest request);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/atg/userprofiling/ProfileActor/cancelOrder")
    Observable<ReOrderResponse> cancelOrder(@Body ReOrderRequest request);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/reminder/ReminderActor/listMedReminders")
    Observable<MedicationReminderListResponse> getMedicationReminderList();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/vet/VetActor/update")
    Observable<AddVetResponse> updateVet(@Body UpdateVetRequest request);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/vet/VetActor/remove")
    Observable<RemovePetResponse> removeVet(@Body RemoveVetRequest request);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/vet/VetActor/notify")
    Observable<RemovePetResponse> requestReferral(@Body UpdateVetRequest request);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/payment/PaymentActor/detailed")
    Observable<CardDetailResponse> getCardByPaymentCardKey(@Body CardDetailRequest request);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/checkout/CheckoutActor/payPalCheckoutRedirect")
    Observable<Response<String>> payPalCheckout(@Body PayPalCheckoutRequest request);


    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/content/ContentActor/educationCategories")
    Observable<PetEducationCategoriesResponse> getPetEducationCategories();

    @POST("/rest/model/1800petmeds/reminder/ReminderActor/createMedReminder")
    Observable<AddMedicationReminderResponse> saveMedicationReminders(
            @Body AddMedicationReminderRequest addMedicationReminderRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/reminder/ReminderActor/updateMedReminder")
    Observable<AddMedicationReminderResponse> updateMedicationReminders(
            @Body AddMedicationReminderRequest addMedicationReminderRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/reminder/ReminderActor/deleteMedReminder")
    Observable<RemoveMedicationReminderResponse> removeMedicationReminders(
            @Body RemoveMedicationReminderRequest removeMedicationReminderRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/reminder/ReminderActor/easyRefillReminder")
    Observable<RefillReminderListResponse> getRefillReminderList(@Query("_dynSessConf") String sessionConfirmation);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/reminder/ReminderActor/monthOptions")
    Observable<MonthSelectListResponse> getRefillReminderMonthList(@Query("_dynSessConf") String sessionConfirmation);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/reminder/ReminderActor/updateRefillReminder")
    Observable<Status> getUpdateRefillReminder(@Body UpdateRefillReminderRequest updateRefillReminderRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/reminder/ReminderActor/removeRefillReminder")
    Observable<Status> getRemoveRefillReminder(@Body RemoveRefillReminderRequest removeRefillReminderRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/atg/userprofiling/ProfileActor/orderDetails")
    Observable<OrderDetailResponse> getOrderDetail(
            @Query("_dynSessConf") String sessionConfirmation, @Query("orderId") String orderId);

    @Multipart
    @POST("/petImageUpload.jsp")
    Observable<PetImageUploadResponse> uploadPetImage( @Part("petid") RequestBody id,@Part MultipartBody.Part file);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/reminder/ReminderActor/medReminderDetails")
    Observable<AddMedicationReminderResponse> getMedicationReminderDetails(@Query("_dynSessConf") String sessionConfNumber, @Query("reminderId")int reminderId);


    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/checkout/CheckoutActor/orderReceipt")
    Observable<CommitOrderResponse> getCommitedOrderDetails(@Query("orderId") String orderId);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/contact/ContactActor/defaultBillingAddress")
    Observable<AddAddressResponse> getDefaultBillingAddress(@Query("_dynSessConf") String sessionConfirmation);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/atg/userprofiling/ProfileActor/pushNotification")
    Observable<PushNotificationResponse> savePushNotificationFlag(@Body PushNotificationRequest pushNotificationRequest);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/cart/CartActor/addEasyRefillItemsToShoppingCart")
    Observable<ShoppingCartListResponse> addEasyRefillItemsToShoppingCart();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @GET("/rest/model/1800petmeds/home/HomeActor/contactUs")
    Observable<ContactUsResponse> getContactDetail ();

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/1800petmeds/cart/CartActor/addRecentOrderedItemToCart")
    Observable<ShoppingCartListResponse> addRecentlyItemToCart(@Body AddRecentlyItemToCart request);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/atg/userprofiling/ProfileActor/resetPasswordByToken")
    Observable<ResetPasswordResponse> saveResetPasswordDetails(@Body SaveResetPasswordRequest save);

    @Headers({"Content-Type: application/json", "Request-Credential: "+ PetMedsApplication.requestCredential})
    @POST("/rest/model/atg/userprofiling/ProfileActor/checkPasswordResetToken")
    Observable<PasswordResetResponse> checkPasswordResetToken(@Body CheckResetPasswordTokenRequest checkResetPasswordTokenRequest);

}
