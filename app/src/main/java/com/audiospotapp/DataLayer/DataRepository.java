package com.audiospotapp.DataLayer;

import android.content.Context;


import com.audiospot.DataLayer.Model.*;
import com.audiospotapp.DataLayer.BusinessInterceptors.authors.AuthorItemInterceptor;
import com.audiospotapp.DataLayer.BusinessInterceptors.authors.AuthorItemUseCase;
import com.audiospotapp.DataLayer.BusinessInterceptors.books.BookItemInterceptor;
import com.audiospotapp.DataLayer.BusinessInterceptors.books.BookItemUseCase;
import com.audiospotapp.DataLayer.BusinessInterceptors.categories.CategoryListInterceptor;
import com.audiospotapp.DataLayer.BusinessInterceptors.categories.CategoryListUseCase;
import com.audiospotapp.DataLayer.BusinessInterceptors.publishers.PublisherItemInterceptor;
import com.audiospotapp.DataLayer.BusinessInterceptors.publishers.PublisherItemUseCase;
import com.audiospotapp.DataLayer.Cache.CacheDataSource;
import com.audiospotapp.DataLayer.Cache.cacheDataSourceUsingSharedPreferences;
import com.audiospotapp.DataLayer.Model.*;
import com.audiospotapp.DataLayer.Retrofit.GlobalKeys;
import com.audiospotapp.DataLayer.Retrofit.RemoteDataSourceUsingRetrofit;
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks;
import com.visionvalley.letuno.DataLayer.RepositorySource;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;

import java.util.ArrayList;
import java.util.List;

public class DataRepository implements RepositorySource {

    private static DataRepository INSTANCE;
    private final String TAG = getClass().getSimpleName();
    private RemoteDataSourceUsingRetrofit mRetrofitService;
    private CacheDataSource mCacheDataSource;
    String lang = "en";
    private AuthorItemInterceptor mAuthorItemInterceptor;
    private CategoryListInterceptor mCategoryListInterceptor;
    private PublisherItemInterceptor mPublisherListInterceptor;
    private BookItemInterceptor mBookItemInterceptor;
    private AuthResponse authResponse;
    private List<Book> myBooks;
    private BookmarkBody bookmarkBody;
    private Bookmark bookmark;
    //endregion

    private DataRepository(Context context) {
        mCacheDataSource = cacheDataSourceUsingSharedPreferences.getINSTANCE(context);
        mRetrofitService = RemoteDataSourceUsingRetrofit.getInstance();
        mCategoryListInterceptor = new CategoryListUseCase();
        mAuthorItemInterceptor = new AuthorItemUseCase();
        mPublisherListInterceptor = new PublisherItemUseCase();
        mBookItemInterceptor = new BookItemUseCase();
        authResponse = mCacheDataSource.getLoggedInUser();
    }

    public static DataRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new DataRepository(context);
        }
        return INSTANCE;
    }

    @Override
    public void setStringIntoCache(String key, String value) {
        mCacheDataSource.setStringIntoCache(key, value);
    }

    @Override
    public String getStringFromCache(String key, String defaultValue) {
        return mCacheDataSource.getStringFromCache(key, defaultValue);
    }

    @Override
    public void setBooleanIntoCache(String key, Boolean value) {
        mCacheDataSource.setBooleanIntoCache(key, value);
    }

    @Override
    public Boolean getBooleanFromCache(String key, Boolean defaultValue) {
        return mCacheDataSource.getBooleanFromCache(key, defaultValue);
    }

    @Override
    public void setLoggedInUser(Object b) {
        mCacheDataSource.setLoggedInUser(b);
    }

    @Override
    public AuthResponse getLoggedInUser() {
        return mCacheDataSource.getLoggedInUser();
    }

    @Override
    public void login(@NotNull String username, @NotNull String password, @NotNull RetrofitCallbacks.AuthResponseCallback callback) {
        mRetrofitService.login(GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                username,
                password,
                new RetrofitCallbacks.AuthResponseCallback() {
                    @Override
                    public void onSuccess(AuthResponse result) {
                        if (result.getData() != null) {
                            result.getData().setPassword(password);
                            mCacheDataSource.setBooleanIntoCache(GlobalKeys.StoreData.IS_LOGGED, true);
                            mCacheDataSource.setLoggedInUser(result);
                            DataRepository.this.authResponse = result;
                        }
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    @Override
    public void socialLogin(@NotNull String first_name, @NotNull String last_name, @NotNull String email, @NotNull String social_source,
                            @NotNull String social_id, @NotNull RetrofitCallbacks.AuthResponseCallback callback) {

        mRetrofitService.social_login(GlobalKeys.API_KEY,
                lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                first_name,
                last_name,
                email,
                social_source,
                social_id,
                new RetrofitCallbacks.AuthResponseCallback() {
                    @Override
                    public void onSuccess(AuthResponse result) {
                        if (result.getData() != null) {
                            result.getData().setPassword("");
                            mCacheDataSource.setBooleanIntoCache(GlobalKeys.StoreData.IS_LOGGED, true);
                            mCacheDataSource.setLoggedInUser(result);
                            DataRepository.this.authResponse = result;
                        }
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    @Override
    public void register(@NotNull String first_name, @NotNull String last_name, @NotNull String email, @NotNull String mobile_phone, @NotNull String password, @NotNull RetrofitCallbacks.AuthResponseCallback callback) {
        mRetrofitService.register(GlobalKeys.API_KEY,
                lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                first_name, last_name, email, mobile_phone, password, new RetrofitCallbacks.AuthResponseCallback() {

                    @Override
                    public void onSuccess(AuthResponse result) {
                        if (result.getData() != null) {
                            result.getData().setPassword(password);
                            mCacheDataSource.setBooleanIntoCache(GlobalKeys.StoreData.IS_LOGGED, true);
                            mCacheDataSource.setLoggedInUser(result);
                            DataRepository.this.authResponse = result;
                        }
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    @Override
    public void resetPassword(@NotNull String email, @NotNull RetrofitCallbacks.AuthResponseCallback callback) {
        mRetrofitService.resetPassword(GlobalKeys.API_KEY,
                lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), email, new RetrofitCallbacks.AuthResponseCallback() {
                    @Override
                    public void onSuccess(AuthResponse result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {

                    }
                });
    }

    @Override
    public void getHomepage(@NotNull RetrofitCallbacks.HomepageResponseCallback callback) {
        mRetrofitService.getHomepage(GlobalKeys.API_KEY,
                lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), new RetrofitCallbacks.HomepageResponseCallback() {
                    @Override
                    public void onSuccess(HomepageRepsonse result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<HomepageRepsonse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    @Override
    public void getAllCategories(@NotNull RetrofitCallbacks.CategoriesListCallback callback) {
        mRetrofitService.getAllCategories(GlobalKeys.API_KEY,
                lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), callback);
    }

    @Override
    public void getAllAuthors(@NotNull RetrofitCallbacks.AuthorsResponseCallback callback) {
        mRetrofitService.getAllAuthors(GlobalKeys.API_KEY,
                lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), callback);
    }

    @Override
    public void getAllPublishers(@NotNull RetrofitCallbacks.PublishersResponseCallback callback) {
        mRetrofitService.getAllPublishers(GlobalKeys.API_KEY,
                lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), callback);
    }

    @Override
    public void clearBookFilters() {
        mCategoryListInterceptor.clearSavedCategoryData();
    }

    @Override
    public void getBooks(@NotNull String book, @NotNull String sort, int author, int publisher, int narrator, int category, @NotNull RetrofitCallbacks.BookListCallback callback) {
        mRetrofitService.getBooks(GlobalKeys.API_KEY,
                lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                book, sort, author, publisher, narrator, category, callback);
    }

    @Override
    public void saveAuthorItem(@NotNull AuthorsData authorsData) {
        mAuthorItemInterceptor.saveAuthorItem(authorsData);
    }

    @NotNull
    @Override
    public AuthorsData getAuthorItem() {
        return mAuthorItemInterceptor.getCurrentAuthorData();
    }

    @Override
    public void saveCategoryItem(@NotNull CategoriesListData categoryListData) {
        mCategoryListInterceptor.saveCategoryItem(categoryListData);
    }

    @NotNull
    @Override
    public CategoriesListData getCurrentCategoryItem() {
        return mCategoryListInterceptor.getCurrentCategoryData();
    }

    @Override
    public void clearCategoryItem() {
        mCategoryListInterceptor.clearSavedCategoryData();
    }

    @Override
    public void clearAuthorItem() {
        mAuthorItemInterceptor.clearSavedAuthorData();
    }

    @Override
    public void savePublisherItem(@NotNull PublishersResponseData authorsData) {
        mPublisherListInterceptor.savePublisherItem(authorsData);
    }

    @NotNull
    @Override
    public PublishersResponseData getPublisherItem() {
        return mPublisherListInterceptor.getCurrentPublisherData();
    }

    @Override
    public void clearPublisherItem() {
        mPublisherListInterceptor.clearSavedPublisherData();
    }

    @Override
    public void saveBook(Book book) {
        mBookItemInterceptor.saveBook(book);
    }

    @NotNull
    @Override
    public Book getSavedBook() {
        return mBookItemInterceptor.getSavedBook();
    }

    @Override
    public void clearSavedBook() {
        mBookItemInterceptor.clearSavedBook();
    }

    @Override
    public void signOut(@NotNull RetrofitCallbacks.LogoutAuthResponseCallback callback) {
        mRetrofitService.signOut(GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), authResponse,
                new RetrofitCallbacks.LogoutAuthResponseCallback() {
                    @Override
                    public void onSuccess(LogoutAuthResponse result) {
                        if (result.getStatus() == 1) {
                            mCacheDataSource.setBooleanIntoCache(GlobalKeys.StoreData.IS_LOGGED, false);
                            mCacheDataSource.setLoggedInUser(null);
                        }
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<LogoutAuthResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    @Override
    public void getProfile(@NotNull RetrofitCallbacks.ProfileResponseCallback callback) {
        mRetrofitService.getProfile(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                new RetrofitCallbacks.ProfileResponseCallback() {
                    @Override
                    public void onSuccess(ProfileResponse result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<ProfileResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    @Override
    public AuthResponse getAuthResponse() {
        return authResponse;
    }

    @Override
    public void updateProfile(@NotNull String first_name, @NotNull String last_name, @NotNull String email, @NotNull String mobile_phone, @NotNull RetrofitCallbacks.AuthResponseCallback authResponseCallback) {
        mRetrofitService.updateProfile(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                first_name, last_name, email, mobile_phone, new RetrofitCallbacks.AuthResponseCallback() {
                    @Override
                    public void onSuccess(AuthResponse result) {
                        if (result.getData() != null && result.getStatus() == 1) {
                            authResponse = result;
                        }
                        authResponseCallback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        authResponseCallback.onFailure(call, t);
                    }
                });
    }

    @Override
    public void updatePassword(@NotNull String old_password,
                               @NotNull String new_password,
                               @NotNull String confirm_password,
                               @NotNull RetrofitCallbacks.AuthResponseCallback authResponseCallback) {
        mRetrofitService.updatePassword(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                old_password, new_password, confirm_password, new RetrofitCallbacks.AuthResponseCallback() {
                    @Override
                    public void onSuccess(AuthResponse result) {
                        authResponseCallback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<AuthResponse> call, Throwable t) {
                        authResponseCallback.onFailure(call, t);
                    }
                });
    }

    @Override
    public void getBookDetails(@NotNull RetrofitCallbacks.BookDetailsResponseCallback callback) {
        mRetrofitService.getBookDetails(GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                mBookItemInterceptor.getSavedBook().getId(),
                new RetrofitCallbacks.BookDetailsResponseCallback() {
                    @Override
                    public void onSuccess(BookDetailsResponse result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<BookDetailsResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    @Override
    public void getBookDetailsWithId(int bookId, @NotNull RetrofitCallbacks.BookDetailsResponseCallback callback) {
        mRetrofitService.getBookDetails(GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                bookId,
                new RetrofitCallbacks.BookDetailsResponseCallback() {
                    @Override
                    public void onSuccess(BookDetailsResponse result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<BookDetailsResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    @Override
    public void getBookReviews(@NotNull RetrofitCallbacks.ReviewListResponseCallback callback) {
        mRetrofitService.getBookReviews(GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                mBookItemInterceptor.getSavedBook().getId(),
                new RetrofitCallbacks.ReviewListResponseCallback() {
                    @Override
                    public void onSuccess(ReviewListResponse result) {
                        mBookItemInterceptor.saveBookReviews(result.getData());
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<ReviewListResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    @Override
    public void addBookToCart(RetrofitCallbacks.ResponseCallback responseCallback) {
        if (authResponse == null) {
            responseCallback.onAuthFailure();
        } else {
            mRetrofitService.addToCart(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                    mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                    mBookItemInterceptor.getSavedBook().getId(),
                    new RetrofitCallbacks.ResponseCallback() {
                        @Override
                        public void onSuccess(Response result) {
                            responseCallback.onSuccess(result);
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            responseCallback.onFailure(call, t);
                        }

                        @Override
                        public void onAuthFailure() {

                        }
                    });
        }
    }

    @Override
    public void addBookToFavorites(@NotNull RetrofitCallbacks.ResponseCallback callback) {
        if (authResponse == null) {
            callback.onAuthFailure();
        } else {
            mRetrofitService.addToFavorites(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                    mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                    mBookItemInterceptor.getSavedBook().getId(),
                    new RetrofitCallbacks.ResponseCallback() {
                        @Override
                        public void onSuccess(Response result) {
                            callback.onSuccess(result);
                        }

                        @Override
                        public void onFailure(Call<Response> call, Throwable t) {
                            callback.onFailure(call, t);
                        }

                        @Override
                        public void onAuthFailure() {

                        }
                    });
        }
    }

    @Override
    public void sendGift(@NotNull String email, @NotNull RetrofitCallbacks.ResponseCallback responseCallback) {
        mRetrofitService.sendGift(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                email,
                mBookItemInterceptor.getSavedBook().getId(),
                new RetrofitCallbacks.ResponseCallback() {
                    @Override
                    public void onSuccess(Response result) {
                        responseCallback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        responseCallback.onFailure(call, t);
                    }

                    @Override
                    public void onAuthFailure() {

                    }
                });
    }

    @Override
    public void getMyBooks(@NotNull RetrofitCallbacks.BookListCallback callback) {
        if (authResponse != null)
            mRetrofitService.getMyBooks(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                    mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), new RetrofitCallbacks.BookListCallback() {
                        @Override
                        public void onSuccess(BookListResponse result) {
                            myBooks = result.getData();
                            callback.onSuccess(result);
                        }

                        @Override
                        public void onFailure(Call<BookListResponse> call, Throwable t) {
                            callback.onFailure(call, t);
                        }
                    });
    }

    @Override
    public boolean isBookMine() {
        Boolean is_Found = false;
        if (myBooks == null)
            return false;
        if (myBooks.size() == 0) {
            return is_Found;
        } else {
            for (Book book : myBooks) {
                if (mBookItemInterceptor.getSavedBook().getId() == book.getId()) {
                    is_Found = true;
                    break;
                }
            }
        }
        return is_Found;
    }

    @Override
    public void getMyCart(@NotNull RetrofitCallbacks.BookListCallback callback) {
        if (authResponse != null)
            mRetrofitService.getMyCart(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                    mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), new RetrofitCallbacks.BookListCallback() {
                        @Override
                        public void onSuccess(BookListResponse result) {
                            callback.onSuccess(result);
                        }

                        @Override
                        public void onFailure(Call<BookListResponse> call, Throwable t) {
                            callback.onFailure(call, t);
                        }
                    });
    }

    @NotNull
    @Override
    public List<Book> getMyBooks() {
        return myBooks;
    }

    @NotNull
    @Override
    public void getMyFavouriteBooks(@NotNull RetrofitCallbacks.BookListCallback callback) {
        mRetrofitService.getMyFavouriteBooks(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), new RetrofitCallbacks.BookListCallback() {
                    @Override
                    public void onSuccess(BookListResponse result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<BookListResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    @NotNull
    @Override
    public List<Review> getCurrentBookReviews() {
        return mBookItemInterceptor.getBookReviews();
    }

    @Override
    public void removeBookFromCart(int book_id, @NotNull RetrofitCallbacks.ResponseCallback callback) {
        mRetrofitService.removeBookFromCart(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                book_id, new RetrofitCallbacks.ResponseCallback() {
                    @Override
                    public void onSuccess(Response result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        callback.onFailure(call, t);
                    }

                    @Override
                    public void onAuthFailure() {

                    }
                });
    }

    @Override
    public void removeBookFromFavorites(int book_id, @NotNull RetrofitCallbacks.ResponseCallback callback) {
        mRetrofitService.removeBookFromFavorites(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                book_id, new RetrofitCallbacks.ResponseCallback() {
                    @Override
                    public void onSuccess(Response result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        callback.onFailure(call, t);
                    }

                    @Override
                    public void onAuthFailure() {

                    }
                });
    }

    @Override
    public void getBookChapters(@NotNull RetrofitCallbacks.ChaptersResponseCallback callback) {
        mRetrofitService.getBookChapters(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                mBookItemInterceptor.getSavedBook().getId(), new RetrofitCallbacks.ChaptersResponseCallback() {
                    @Override
                    public void onSuccess(ChaptersResponse result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<ChaptersResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    @Override
    public void setBookmarkData(@NotNull BookmarkBody bookmarkBody) {
        this.bookmarkBody = bookmarkBody;
    }

    @NotNull
    @Override
    public BookmarkBody getBookmarkData() {
        return this.bookmarkBody;
    }

    @Override
    public void addBookmark(@NotNull BookmarkBody bookmarkData, RetrofitCallbacks.ResponseCallback callback) {
        mRetrofitService.addBookmark(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                bookmarkData.getBookId(),
                bookmarkData.getChapter_id(),
                bookmarkData.getBookmarkTime(),
                bookmarkData.getComment(),
                new RetrofitCallbacks.ResponseCallback() {
                    @Override
                    public void onSuccess(Response result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<Response> call, Throwable t) {
                        callback.onFailure(call, t);
                    }

                    @Override
                    public void onAuthFailure() {

                    }
                });
    }

    @Override
    public void myBookmarks(@NotNull RetrofitCallbacks.MyBookmarkResponseCallback callback) {
        mRetrofitService.myBookmarks(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), new RetrofitCallbacks.MyBookmarkResponseCallback() {
                    @Override
                    public void onSuccess(MyBookmarksResponse result) {
                        callback.onSuccess(result);
                    }

                    @Override
                    public void onFailure(Call<MyBookmarksResponse> call, Throwable t) {
                        callback.onFailure(call, t);
                    }
                });
    }

    @Override
    public void saveBookmark(Bookmark bookmark) {
        this.bookmark = bookmark;
    }

    @Override
    public Bookmark getBookmark() {
        return this.bookmark;
    }

    @Override
    public void contactUs(@NotNull String message, @NotNull RetrofitCallbacks.ContactUsResponseCallback callback) {
        mRetrofitService.contactUs(authResponse.getData().getToken(), GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), message, callback);
    }
}