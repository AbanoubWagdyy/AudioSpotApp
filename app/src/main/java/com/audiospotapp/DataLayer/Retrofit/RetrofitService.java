package com.audiospotapp.DataLayer.Retrofit;

import com.audiospot.DataLayer.Model.AuthResponse;
import com.audiospot.DataLayer.Model.BookDetailsResponse;
import com.audiospot.DataLayer.Model.CategoriesListResponse;
import com.audiospot.DataLayer.Model.HomepageRepsonse;
import com.audiospotapp.DataLayer.Model.*;
import retrofit2.Call;
import retrofit2.http.*;

public interface RetrofitService {

    //Login
    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("login")
    Call<AuthResponse> login(
            @Query("email") String email,
            @Query("password") String password);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("social-auth")
    Call<AuthResponse> loginSocial(
            @Query("first_name") String first_name,
            @Query("last_name") String last_name,
            @Query("email") String email,
            @Query("social_source") String social_source,
            @Query("social_id") String social_id);

    //Register
    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("register")
    Call<AuthResponse> register(
            @Query("first_name") String first_name,
            @Query("last_name") String last_name,
            @Query("email") String email,
            @Query("phone") String phone,
            @Query("password") String password);

    //Reset Password
    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("reset-password")
    Call<AuthResponse> resetPassword(
            @Query("email") String email);

    //Logout
    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("logout")
    Call<LogoutAuthResponse> logout();

    //Get Profile
    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("profile")
    Call<ProfileResponse> getProfile();

    //Update Profile
    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("profile")
    Call<AuthResponse> updateProfile(
            @Query("first_name") String first_name,
            @Query("last_name") String last_name,
            @Query("email") String email,
            @Query("phone") String phone);

    //Update Password
    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("update-password")
    Call<AuthResponse> updatePassword(
            @Query("current_password") String current_password,
            @Query("new_password") String new_password,
            @Query("confirm_password") String confirm_password);

    //check response to validate

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("profile/my-books")
    Call<BookListResponse> getMyBooks();

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("profile/my-favorites")
    Call<BookListResponse> getMyFavouriteBooks();

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("profile/book-favorites-remove/{bookId}")
    Call<Response> removeFavouriteBook(
            @Path("bookId") int bookId);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("add-to-cart/{bookId}")
    Call<Response> addToCart(
            @Path("bookId") int bookId);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("profile/my-cart")
    Call<BookListResponse> getMyCart();

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("profile/book-cart-remove/{bookId}")
    Call<Response> removeBookFromCart(
            @Path("bookId") Integer bookId);

    @Headers({
            "Accept-Encoding: gzip, deflate",
            "Cache-Control: no-cache, private",
            "Connection: keep-alive"
    })
    @GET("home")
    Call<HomepageRepsonse> getHomepage();

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("categories-list")
    Call<CategoriesListResponse> getAllCategories();

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("books-list")
    Call<BookListResponse> getBooks(
            @Query("book") String book,
            @Query("sort") String sort,
            @Query("author") Integer author,
            @Query("publisher") Integer publisher,
            @Query("narrator") Integer narrator,
            @Query("category") Integer category
    );

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("book/{bookId}")
    Call<BookDetailsResponse> getBookDetails(@Path("bookId") int bookId);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("add-to-favorites/{bookId}")
    Call<Response> addToFavorites(
            @Path("bookId") int bookId);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("chapters/{bookId}")
    Call<ChaptersResponse> getBooksChapters(
            @Path("bookId") int bookId);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("book-rate/{bookId}")
    Call<Response> rateBook(
            @Path("bookId") int bookId,
            @Query("rate") int rate,
            @Query("comment") String comment);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("review-list/{bookId}")
    Call<ReviewListResponse> getBookReviews(
            @Path("bookId") int bookId);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("add-promo-code")
    Call<BookDetailsResponse> addPromoCode(
            @Query("promo_code") String promo_code);


    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("profile/my-bookmarks")
    Call<MyBookmarksResponse> myBookmarks();

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("add-bookmark/{bookId}")
    Call<Response> addBookmark(
            @Path("bookId") int bookId,
            @Query("chapter_id") int chapter_id,
            @Query("time") String time,
            @Query("comment") String comment
    );

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @POST("send-as-gift/{giftId}")
    Call<Response> sendGift(
            @Path("giftId") int giftId,
            @Query("email") String email);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("languages")
    Call<BookDetailsResponse> getAllLanguages();

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("countries")
    Call<BookDetailsResponse> getAllCountries();

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("cities/{cityId}")
    Call<BookDetailsResponse> getAllCities(@Path("cityId") int cityId);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("cities/{areaId}")
    Call<BookDetailsResponse> getAllAreas(@Path("areaId") int areaId);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("authors")
    Call<AuthorsResponse> getAllAuthors();

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("publishers")
    Call<PublishersResponse> getAllPublishers();

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("narrators?name={name}")
    Call<BookDetailsResponse> getAllNarrators(@Path("name") String name);

    @Headers({
            "Accept: application/json",
            "Content-type: application/json"
    })
    @GET("narrator/{narratorId}")
    Call<BookDetailsResponse> getNarratorsProfile(@Path("narratorId") int narratorId);
}