package com.audiospotapp.DataLayer.Retrofit;

import com.audiospot.DataLayer.Model.AuthResponse;
import com.audiospot.DataLayer.Model.BookDetailsResponse;
import com.audiospot.DataLayer.Model.CategoriesListResponse;
import com.audiospot.DataLayer.Model.HomepageRepsonse;
import com.audiospotapp.DataLayer.Model.*;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RemoteDataSourceUsingRetrofit {

    private static RemoteDataSourceUsingRetrofit INSTANCE;

    private RemoteDataSourceUsingRetrofit() {
    }

    public static RemoteDataSourceUsingRetrofit getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RemoteDataSourceUsingRetrofit();
        return INSTANCE;
    }

    public void login(String api_key,
                      String lang,
                      String device_key,
                      String email,
                      String password,
                      RetrofitCallbacks.AuthResponseCallback callback) {

        RestClient.getRetrofitService(api_key, lang, device_key)
                .login(email, password)
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void social_login(String api_key,
                             String lang,
                             String device_key,
                             String first_name,
                             String last_name,
                             String email,
                             String social_source,
                             String social_id,
                             RetrofitCallbacks.AuthResponseCallback callback) {

        RestClient.getRetrofitService(api_key, lang, device_key)
                .loginSocial(first_name, last_name, email, social_source, social_id)
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void register(String api_key, String lang, String device_key, String first_name, String last_name, String email, String phone, String password, RetrofitCallbacks.AuthResponseCallback callback) {
        RestClient.getRetrofitService(api_key, lang, device_key)
                .register(first_name, last_name, email, phone, password)
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void resetPassword(String api_key,
                              String lang,
                              String device_key,
                              String email, RetrofitCallbacks.AuthResponseCallback callback) {

        RestClient.getRetrofitService(api_key, lang, device_key)
                .resetPassword(email)
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void getHomepage(String api_key,
                            String lang,
                            String device_key,
                            RetrofitCallbacks.HomepageResponseCallback callback) {

        RestClient.getRetrofitService(api_key, lang, device_key)
                .getHomepage()
                .enqueue(new Callback<HomepageRepsonse>() {
                    @Override
                    public void onResponse(Call<HomepageRepsonse> call, Response<HomepageRepsonse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<HomepageRepsonse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void getAllCategories(String api_key,
                                 String lang,
                                 String device_key,
                                 RetrofitCallbacks.CategoriesListCallback callback) {
        RestClient.getRetrofitService(api_key, lang, device_key)
                .getAllCategories()
                .enqueue(new Callback<CategoriesListResponse>() {
                    @Override
                    public void onResponse(Call<CategoriesListResponse> call, Response<CategoriesListResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<CategoriesListResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void getAllAuthors(String api_key,
                              String lang,
                              String device_key,
                              RetrofitCallbacks.AuthorsResponseCallback callback) {
        RestClient.getRetrofitService(api_key, lang, device_key)
                .getAllAuthors()
                .enqueue(new Callback<AuthorsResponse>() {
                    @Override
                    public void onResponse(Call<AuthorsResponse> call, Response<AuthorsResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<AuthorsResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void getAllPublishers(String api_key,
                                 String lang,
                                 String device_key,
                                 RetrofitCallbacks.PublishersResponseCallback callback) {
        RestClient.getRetrofitService(api_key, lang, device_key)
                .getAllPublishers()
                .enqueue(new Callback<PublishersResponse>() {
                    @Override
                    public void onResponse(Call<PublishersResponse> call, Response<PublishersResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<PublishersResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void getBooks(String apiKey, String lang, String deviceToken, String book,
                         String sort, int author, int publisher, int narrator, int category,
                         RetrofitCallbacks.BookListCallback callback) {
        RestClient.getRetrofitService(apiKey, lang, deviceToken)
                .getBooks(book, sort, author, publisher, narrator, category)
                .enqueue(new Callback<BookListResponse>() {
                    @Override
                    public void onResponse(Call<BookListResponse> call, Response<BookListResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<BookListResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void signOut(String apiKey, String lang, String device_key,
                        AuthResponse response,
                        RetrofitCallbacks.LogoutAuthResponseCallback callback) {

        RestClient.getRetrofitService(response.getData().getToken(), apiKey, lang, device_key)
                .logout()
                .enqueue(new Callback<LogoutAuthResponse>() {
                    @Override
                    public void onResponse(Call<LogoutAuthResponse> call, Response<LogoutAuthResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<LogoutAuthResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void getProfile(String token, String apiKey, String lang, String device_key, RetrofitCallbacks.ProfileResponseCallback profileResponseCallback) {
        RestClient.getRetrofitService(token, apiKey, lang, device_key)
                .getProfile()
                .enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(Call<ProfileResponse> call, Response<ProfileResponse> response) {
                        profileResponseCallback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<ProfileResponse> call, Throwable t) {
                        profileResponseCallback.onFailure(call, t);
                    }
                });
    }

    public void updateProfile(String token, String apiKey, String lang, String device_key,
                              String first_name, String last_name, String email, String mobile_phone,
                              RetrofitCallbacks.AuthResponseCallback callback) {
        RestClient.getRetrofitService(token, apiKey, lang, device_key)
                .updateProfile(first_name, last_name, email, mobile_phone)
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void updatePassword(String token, String apiKey, String lang, String device_key, String old_password, String new_password, String confirm_password, RetrofitCallbacks.AuthResponseCallback callback) {
        RestClient.getRetrofitService(token, apiKey, lang, device_key)
                .updatePassword(old_password, new_password, confirm_password)
                .enqueue(new Callback<AuthResponse>() {
                    @Override
                    public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void getBookDetails(String apiKey, String lang, String device_key, Integer bookId, RetrofitCallbacks.BookDetailsResponseCallback callback) {
        RestClient.getRetrofitService(apiKey, lang, device_key)
                .getBookDetails(bookId)
                .enqueue(new Callback<BookDetailsResponse>() {
                    @Override
                    public void onResponse(Call<BookDetailsResponse> call, Response<BookDetailsResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<BookDetailsResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void getBookReviews(String apiKey, String lang, String device_key, Integer bookId,
                               RetrofitCallbacks.ReviewListResponseCallback callback) {
        RestClient.getRetrofitService(apiKey, lang, device_key)
                .getBookReviews(bookId)
                .enqueue(new Callback<ReviewListResponse>() {
                    @Override
                    public void onResponse(Call<ReviewListResponse> call, Response<ReviewListResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<ReviewListResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });

    }

    public void addToCart(String token, String apiKey, String lang, String deviceToken, int id, RetrofitCallbacks.ResponseCallback responseCallback) {
        RestClient.getRetrofitService(token, apiKey, lang, deviceToken)
                .addToCart(id)
                .enqueue(new Callback<com.audiospotapp.DataLayer.Model.Response>() {
                    @Override
                    public void onResponse(Call<com.audiospotapp.DataLayer.Model.Response> call, Response<com.audiospotapp.DataLayer.Model.Response> response) {
                        responseCallback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<com.audiospotapp.DataLayer.Model.Response> call, Throwable t) {
                        responseCallback.onFailure(call, t);
                    }
                });
    }

    public void getBookChapters(String token, String apiKey, String lang, String deviceToken, int id, RetrofitCallbacks.ChaptersResponseCallback callback) {
        RestClient.getRetrofitService(token, apiKey, lang, deviceToken)
                .getBooksChapters(id)
                .enqueue(new Callback<ChaptersResponse>() {
                    @Override
                    public void onResponse(Call<ChaptersResponse> call, Response<ChaptersResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<ChaptersResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void addToFavorites(String token, String apiKey, String lang, String deviceToken, int id, RetrofitCallbacks.ResponseCallback responseCallback) {
        RestClient.getRetrofitService(token, apiKey, lang, deviceToken)
                .addToFavorites(id)
                .enqueue(new Callback<com.audiospotapp.DataLayer.Model.Response>() {
                    @Override
                    public void onResponse(Call<com.audiospotapp.DataLayer.Model.Response> call, Response<com.audiospotapp.DataLayer.Model.Response> response) {
                        responseCallback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<com.audiospotapp.DataLayer.Model.Response> call, Throwable t) {
                        responseCallback.onFailure(call, t);
                    }
                });
    }

    public void sendGift(String token, String apiKey, String lang, String deviceToken, String email, int id, RetrofitCallbacks.ResponseCallback responseCallback) {
        RestClient.getRetrofitService(token, apiKey, lang, deviceToken)
                .sendGift(id, email)
                .enqueue(new Callback<com.audiospotapp.DataLayer.Model.Response>() {
                    @Override
                    public void onResponse(Call<com.audiospotapp.DataLayer.Model.Response> call, Response<com.audiospotapp.DataLayer.Model.Response> response) {
                        responseCallback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<com.audiospotapp.DataLayer.Model.Response> call, Throwable t) {
                        responseCallback.onFailure(call, t);
                    }
                });
    }

    public void getMyBooks(String token, String apiKey, String lang, String deviceToken, RetrofitCallbacks.BookListCallback callback) {
        RestClient.getRetrofitService(token, apiKey, lang, deviceToken)
                .getMyBooks()
                .enqueue(new Callback<BookListResponse>() {
                    @Override
                    public void onResponse(Call<BookListResponse> call, Response<BookListResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<BookListResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void getMyFavouriteBooks(String token, String apiKey, String lang, String deviceToken,
                                    RetrofitCallbacks.BookListCallback callback) {
        RestClient.getRetrofitService(token, apiKey, lang, deviceToken)
                .getMyFavouriteBooks()
                .enqueue(new Callback<BookListResponse>() {
                    @Override
                    public void onResponse(Call<BookListResponse> call, Response<BookListResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<BookListResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void getMyCart(String token, String apiKey, String lang, String deviceToken, RetrofitCallbacks.BookListCallback callback) {
        RestClient.getRetrofitService(token, apiKey, lang, deviceToken)
                .getMyCart()
                .enqueue(new Callback<BookListResponse>() {
                    @Override
                    public void onResponse(Call<BookListResponse> call, Response<BookListResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<BookListResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void removeBookFromCart(String token, String apiKey, String lang, String deviceToken, Integer book_id, RetrofitCallbacks.ResponseCallback callback) {
        RestClient.getRetrofitService(token, apiKey, lang, deviceToken)
                .removeBookFromCart(book_id)
                .enqueue(new Callback<com.audiospotapp.DataLayer.Model.Response>() {
                    @Override
                    public void onResponse(Call<com.audiospotapp.DataLayer.Model.Response> call, Response<com.audiospotapp.DataLayer.Model.Response> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<com.audiospotapp.DataLayer.Model.Response> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void removeBookFromFavorites(String token, String apiKey, String lang, String deviceToken, Integer book_id, RetrofitCallbacks.ResponseCallback callback) {
        RestClient.getRetrofitService(token, apiKey, lang, deviceToken)
                .removeFavouriteBook(book_id)
                .enqueue(new Callback<com.audiospotapp.DataLayer.Model.Response>() {
                    @Override
                    public void onResponse(Call<com.audiospotapp.DataLayer.Model.Response> call, Response<com.audiospotapp.DataLayer.Model.Response> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<com.audiospotapp.DataLayer.Model.Response> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void addBookmark(String token, String apiKey, String lang, String deviceToken, Integer book_id,
                            int chapter_id,
                            String time,
                            String comment,
                            RetrofitCallbacks.ResponseCallback callback) {

        RestClient.getRetrofitService(token, apiKey, lang, deviceToken)
                .addBookmark(book_id, chapter_id, time, comment)
                .enqueue(new Callback<com.audiospotapp.DataLayer.Model.Response>() {
                    @Override
                    public void onResponse(Call<com.audiospotapp.DataLayer.Model.Response> call, Response<com.audiospotapp.DataLayer.Model.Response> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<com.audiospotapp.DataLayer.Model.Response> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void myBookmarks(String token, String apiKey, String lang, String deviceToken,
                            RetrofitCallbacks.MyBookmarkResponseCallback callback) {

        RestClient.getRetrofitService(token, apiKey, lang, deviceToken)
                .myBookmarks()
                .enqueue(new Callback<MyBookmarksResponse>() {
                    @Override
                    public void onResponse(Call<MyBookmarksResponse> call, Response<MyBookmarksResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<MyBookmarksResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    public void contactUs(String token, String apiKey, String lang, String deviceToken, String message,
                          RetrofitCallbacks.ContactUsResponseCallback callback) {

        RestClient.getRetrofitService(token, apiKey, lang, deviceToken)
                .contactUs(message)
                .enqueue(new Callback<ContactUsResponse>() {
                    @Override
                    public void onResponse(Call<ContactUsResponse> call, Response<ContactUsResponse> response) {
                        callback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<ContactUsResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }


    public void rateBook(String token, String apiKey, String lang, String deviceToken, int bookId, int rate, String comment, RetrofitCallbacks.ResponseCallback responseCallback) {
        RestClient.getRetrofitService(token, apiKey, lang, deviceToken)
                .rateBook(bookId, rate, comment)
                .enqueue(new Callback<com.audiospotapp.DataLayer.Model.Response>() {
                    @Override
                    public void onResponse(Call<com.audiospotapp.DataLayer.Model.Response> call, Response<com.audiospotapp.DataLayer.Model.Response> response) {
                        responseCallback.onSuccess(response.body());
                    }

                    @Override
                    public void onFailure(Call<com.audiospotapp.DataLayer.Model.Response> call, Throwable t) {
                        responseCallback.onFailure(call, t);
                    }
                });
    }
}