package com.audiospotapplication.DataLayer

import android.content.Context
import android.support.v4.media.MediaBrowserCompat
import android.support.v4.media.MediaMetadataCompat
import android.util.Log

import com.audiospot.DataLayer.Model.*
import com.audiospotapplication.DataLayer.BusinessInterceptors.authors.AuthorItemInterceptor
import com.audiospotapplication.DataLayer.BusinessInterceptors.authors.AuthorItemUseCase
import com.audiospotapplication.DataLayer.BusinessInterceptors.books.BookItemInterceptor
import com.audiospotapplication.DataLayer.BusinessInterceptors.books.BookItemUseCase
import com.audiospotapplication.DataLayer.BusinessInterceptors.categories.CategoryListInterceptor
import com.audiospotapplication.DataLayer.BusinessInterceptors.categories.CategoryListUseCase
import com.audiospotapplication.DataLayer.BusinessInterceptors.publishers.PublisherItemInterceptor
import com.audiospotapplication.DataLayer.BusinessInterceptors.publishers.PublisherItemUseCase
import com.audiospotapplication.DataLayer.Cache.CacheDataSource
import com.audiospotapplication.DataLayer.Cache.cacheDataSourceUsingSharedPreferences
import com.audiospotapplication.DataLayer.Model.*
import com.audiospotapplication.DataLayer.Retrofit.GlobalKeys
import com.audiospotapplication.DataLayer.Retrofit.RemoteDataSourceUsingRetrofit
import com.audiospotapplication.DataLayer.Retrofit.RetrofitCallbacks
import com.audiospotapplication.UI.ActiveTab
import com.audiospotapplication.UI.giftSelection.GiftSelection
import com.visionvalley.letuno.DataLayer.RepositorySource
import retrofit2.Call
import java.util.*
import kotlin.collections.ArrayList

class DataRepository private constructor(context: Context) : RepositorySource {

    override fun getCurrentBookChapters(): List<ChaptersData> {
        return mBookItemInterceptor.getBookChapters()
    }

    private val mMediaItems = java.util.ArrayList<MediaBrowserCompat.MediaItem>()
    private val mTreeMap = TreeMap<String, MediaMetadataCompat>()

    private var isToPlayFirstChapter: Boolean = false
    private val TAG = javaClass.simpleName
    private val mRetrofitService: RemoteDataSourceUsingRetrofit
    private val mCacheDataSource: CacheDataSource
    internal var lang = ""
    private val mAuthorItemInterceptor: AuthorItemInterceptor
    private val mCategoryListInterceptor: CategoryListInterceptor
    private val mPublisherListInterceptor: PublisherItemInterceptor
    private val mBookItemInterceptor: BookItemInterceptor
    private var authResponse: AuthResponse? = null
    private var myBooks: ArrayList<Book>? = null
    private var bookmarkBody: BookmarkBody? = null
    private var bookmark: Bookmark? = null
    private var promoCode = ""
    private var promoCodeResponse: PromoCodeResponse? = null
    private var voucherBook: Book? = null
    private var activeTab: ActiveTab? = null

    lateinit var giftSelection: GiftSelection
    internal var quantity = 0

    override fun createOrder(
        orderBody: CreateOrderBody,
        callback: RetrofitCallbacks.CreateOrderResponseCallback
    ) {
        mRetrofitService.createOrder(authResponse!!.data.token,
            GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            orderBody,
            object : RetrofitCallbacks.CreateOrderResponseCallback {
                override fun onSuccess(result: CreateOrderResponse?) {
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<CreateOrderResponse>?, t: Throwable?) {
                    callback.onFailure(call, t)
                }
            })
    }

    override fun clear() {
        reset()
    }

    override fun reset() {
        INSTANCE = null
        mCacheDataSource.clear()
    }

    override fun getIsPlayFirstChapter(): Boolean {
        return this.isToPlayFirstChapter
    }

    override fun setIsPlayFirstChapter(isToPlayFirstChapter: Boolean) {
        this.isToPlayFirstChapter = isToPlayFirstChapter
    }

    override fun setCurrentLanguage(lang: String) {
        this.lang = lang
        mCacheDataSource.setStringIntoCache(GlobalKeys.Language.LANGUAGE_KEY, lang)
    }

    override fun getCurrentLanguage(): String {
        return lang
    }

    override fun setMyBooks(listMyBooks: ArrayList<Book>) {
        myBooks = ArrayList()
        myBooks = listMyBooks
    }

    init {
        mCacheDataSource = cacheDataSourceUsingSharedPreferences.getINSTANCE(context)
        mRetrofitService = RemoteDataSourceUsingRetrofit.getInstance()
        mCategoryListInterceptor = CategoryListUseCase()
        mAuthorItemInterceptor = AuthorItemUseCase()
        mPublisherListInterceptor = PublisherItemUseCase()
        mBookItemInterceptor = BookItemUseCase()
        authResponse = mCacheDataSource.loggedInUser
        lang = mCacheDataSource.getStringFromCache(GlobalKeys.Language.LANGUAGE_KEY, "en")
    }

    override fun setStringIntoCache(key: String, value: String) {
        mCacheDataSource.setStringIntoCache(key, value)
    }

    override fun getStringFromCache(key: String, defaultValue: String): String {
        return mCacheDataSource.getStringFromCache(key, defaultValue)
    }

    override fun setBooleanIntoCache(key: String, value: Boolean?) {
        mCacheDataSource.setBooleanIntoCache(key, value)
    }

    override fun getBooleanFromCache(key: String, defaultValue: Boolean?): Boolean? {
        return mCacheDataSource.getBooleanFromCache(key, defaultValue)
    }

    override fun setLoggedInUser(b: Any) {
        mCacheDataSource.setLoggedInUser(b)
    }

    override fun getLoggedInUser(): AuthResponse {
        return mCacheDataSource.loggedInUser
    }

    override fun login(
        username: String,
        password: String,
        callback: RetrofitCallbacks.AuthResponseCallback
    ) {
        mRetrofitService.login(GlobalKeys.API_KEY, lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            username,
            password,
            object : RetrofitCallbacks.AuthResponseCallback {
                override fun onSuccess(result: AuthResponse) {
                    if (result.data != null) {
                        result.data.Password = password
                        mCacheDataSource.setBooleanIntoCache(GlobalKeys.StoreData.IS_LOGGED, true)
                        mCacheDataSource.setLoggedInUser(result)
                        this@DataRepository.authResponse = result
                    }
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    callback.onFailure(call, t)
                }
            })
    }

    override fun socialLogin(
        first_name: String, last_name: String, email: String, social_source: String,
        social_id: String, callback: RetrofitCallbacks.AuthResponseCallback
    ) {

        mRetrofitService.social_login(GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            first_name,
            last_name,
            email,
            social_source,
            social_id,
            object : RetrofitCallbacks.AuthResponseCallback {
                override fun onSuccess(result: AuthResponse) {
                    if (result.data != null) {
                        result.data.Password = ""
                        mCacheDataSource.setBooleanIntoCache(GlobalKeys.StoreData.IS_LOGGED, true)
                        mCacheDataSource.setLoggedInUser(result)
                        this@DataRepository.authResponse = result
                    }
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    callback.onFailure(call, t)
                }
            })
    }

    override fun register(
        first_name: String,
        last_name: String,
        email: String,
        mobile_phone: String,
        password: String,
        callback: RetrofitCallbacks.AuthResponseCallback
    ) {
        mRetrofitService.register(GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            first_name,
            last_name,
            email,
            mobile_phone,
            password,
            object : RetrofitCallbacks.AuthResponseCallback {

                override fun onSuccess(result: AuthResponse) {
                    if (result.data != null) {
                        result.data.Password = password
                        mCacheDataSource.setBooleanIntoCache(GlobalKeys.StoreData.IS_LOGGED, true)
                        mCacheDataSource.setLoggedInUser(result)
                        this@DataRepository.authResponse = result
                    }
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    callback.onFailure(call, t)
                }
            })
    }

    override fun resetPassword(email: String, callback: RetrofitCallbacks.ResponseCallback) {
        mRetrofitService.resetPassword(
            GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), email, callback
        )
    }

    override fun getHomepage(callback: RetrofitCallbacks.HomepageResponseCallback) {
        mRetrofitService.getHomepage(GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            object : RetrofitCallbacks.HomepageResponseCallback {
                override fun onSuccess(result: HomepageRepsonse) {
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<HomepageRepsonse>, t: Throwable) {
                    callback.onFailure(call, t)
                }
            })
    }

    override fun getAllCategories(callback: RetrofitCallbacks.CategoriesListCallback) {
        mRetrofitService.getAllCategories(
            GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), callback
        )
    }

    override fun getAllAuthors(callback: RetrofitCallbacks.AuthorsResponseCallback) {
        mRetrofitService.getAllAuthors(
            GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), callback
        )
    }

    override fun getAllPublishers(callback: RetrofitCallbacks.PublishersResponseCallback) {
        mRetrofitService.getAllPublishers(
            GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), callback
        )
    }

    override fun clearBookFilters() {
        mCategoryListInterceptor.clearSavedCategoryData()
    }

    override fun getBooks(
        book: String,
        sort: String,
        author: Int,
        publisher: Int,
        narrator: Int,
        category: Int,
        callback: RetrofitCallbacks.BookListCallback
    ) {
        mRetrofitService.getBooks(
            GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            book, sort, author, publisher, narrator, category, callback
        )
    }

    override fun saveAuthorItem(authorsData: AuthorsData) {
        mAuthorItemInterceptor.saveAuthorItem(authorsData)
    }

    override fun getAuthorItem(): AuthorsData? {
        return mAuthorItemInterceptor.getCurrentAuthorData()
    }

    override fun saveCategoryItem(categoryListData: CategoriesListData) {
        mCategoryListInterceptor.saveCategoryItem(categoryListData)
    }

    override fun getCurrentCategoryItem(): CategoriesListData? {
        return mCategoryListInterceptor.getCurrentCategoryData()
    }

    override fun clearCategoryItem() {
        mCategoryListInterceptor.clearSavedCategoryData()
    }

    override fun clearAuthorItem() {
        mAuthorItemInterceptor.clearSavedAuthorData()
    }

    override fun savePublisherItem(authorsData: PublishersResponseData) {
        mPublisherListInterceptor.savePublisherItem(authorsData)
    }

    override fun getPublisherItem(): PublishersResponseData? {
        return mPublisherListInterceptor.getCurrentPublisherData()
    }

    override fun clearPublisherItem() {
        mPublisherListInterceptor.clearSavedPublisherData()
    }

    override fun saveBook(book: Book?) {
        mBookItemInterceptor.saveBook(book)
    }

    override fun getSavedBook(): Book? {
        return mBookItemInterceptor.getSavedBook()
    }

    override fun clearSavedBook() {
        mBookItemInterceptor.clearSavedBook()
    }

    override fun signOut(callback: RetrofitCallbacks.LogoutAuthResponseCallback) {
        mRetrofitService.signOut(GlobalKeys.API_KEY, lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), authResponse,
            object : RetrofitCallbacks.LogoutAuthResponseCallback {
                override fun onSuccess(result: LogoutAuthResponse) {
                    if (result.status == 1) {
                        mCacheDataSource.setBooleanIntoCache(GlobalKeys.StoreData.IS_LOGGED, false)
                        mCacheDataSource.setLoggedInUser(null)
                    }
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<LogoutAuthResponse>, t: Throwable) {
                    callback.onFailure(call, t)
                }
            })
    }

    override fun getProfile(callback: RetrofitCallbacks.ProfileResponseCallback) {
        mRetrofitService.getProfile(authResponse!!.data.token, GlobalKeys.API_KEY, lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            object : RetrofitCallbacks.ProfileResponseCallback {
                override fun onSuccess(result: ProfileResponse) {
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                    callback.onFailure(call, t)
                }
            })
    }

    override fun getAuthResponse(): AuthResponse? {
        return authResponse
    }

    override fun updateProfile(
        first_name: String,
        last_name: String,
        email: String,
        mobile_phone: String,
        authResponseCallback: RetrofitCallbacks.AuthResponseCallback
    ) {
        mRetrofitService.updateProfile(authResponse!!.data.token,
            GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            first_name,
            last_name,
            email,
            mobile_phone,
            object : RetrofitCallbacks.AuthResponseCallback {
                override fun onSuccess(result: AuthResponse) {
                    if (result.data != null && result.status == 1) {
                        authResponse = result
                    }
                    authResponseCallback.onSuccess(result)
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    authResponseCallback.onFailure(call, t)
                }
            })
    }

    override fun updatePassword(
        old_password: String,
        new_password: String,
        confirm_password: String,
        authResponseCallback: RetrofitCallbacks.AuthResponseCallback
    ) {
        mRetrofitService.updatePassword(authResponse!!.data.token,
            GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            old_password,
            new_password,
            confirm_password,
            object : RetrofitCallbacks.AuthResponseCallback {
                override fun onSuccess(result: AuthResponse) {
                    authResponseCallback.onSuccess(result)
                }

                override fun onFailure(call: Call<AuthResponse>, t: Throwable) {
                    authResponseCallback.onFailure(call, t)
                }
            })
    }

    override fun getBookDetails(callback: RetrofitCallbacks.BookDetailsResponseCallback) {
        mRetrofitService.getBookDetails(GlobalKeys.API_KEY, lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            mBookItemInterceptor.getSavedBook()!!.id,
            object : RetrofitCallbacks.BookDetailsResponseCallback {
                override fun onSuccess(result: BookDetailsResponse) {
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<BookDetailsResponse>, t: Throwable) {
                    callback.onFailure(call, t)
                }
            })
    }

    override fun getBookDetailsWithId(
        bookId: Int,
        callback: RetrofitCallbacks.BookDetailsResponseCallback
    ) {
        mRetrofitService.getBookDetails(GlobalKeys.API_KEY, lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            bookId,
            object : RetrofitCallbacks.BookDetailsResponseCallback {
                override fun onSuccess(result: BookDetailsResponse) {
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<BookDetailsResponse>, t: Throwable) {
                    callback.onFailure(call, t)
                }
            })
    }

    override fun getBookReviews(callback: RetrofitCallbacks.ReviewListResponseCallback) {
        mRetrofitService.getBookReviews(GlobalKeys.API_KEY, lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            mBookItemInterceptor.getSavedBook()!!.id,
            object : RetrofitCallbacks.ReviewListResponseCallback {
                override fun onSuccess(result: ReviewListResponse) {
                    mBookItemInterceptor.saveBookReviews(result.data)
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<ReviewListResponse>, t: Throwable) {
                    callback.onFailure(call, t)
                }
            })
    }

    override fun addBookToCart(responseCallback: RetrofitCallbacks.ResponseCallback) {
        if (authResponse == null) {
            responseCallback.onAuthFailure()
        } else {
            mRetrofitService.addToCart(authResponse!!.data.token, GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                mBookItemInterceptor.getSavedBook()!!.id,
                object : RetrofitCallbacks.ResponseCallback {
                    override fun onSuccess(result: Response) {
                        responseCallback.onSuccess(result)
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        responseCallback.onFailure(call, t)
                    }

                    override fun onAuthFailure() {

                    }
                })
        }
    }

    override fun addBookToFavorites(callback: RetrofitCallbacks.ResponseCallback) {
        if (authResponse == null) {
            callback.onAuthFailure()
        } else {
            mRetrofitService.addToFavorites(authResponse!!.data.token, GlobalKeys.API_KEY, lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                mBookItemInterceptor.getSavedBook()!!.id,
                object : RetrofitCallbacks.ResponseCallback {
                    override fun onSuccess(result: Response) {
                        callback.onSuccess(result)
                    }

                    override fun onFailure(call: Call<Response>, t: Throwable) {
                        callback.onFailure(call, t)
                    }

                    override fun onAuthFailure() {

                    }
                })
        }
    }

    override fun getMyBooks(callback: RetrofitCallbacks.BookListCallback) {
        if (authResponse != null)
            mRetrofitService.getMyBooks(authResponse!!.data.token,
                GlobalKeys.API_KEY,
                lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                object : RetrofitCallbacks.BookListCallback {
                    override fun onSuccess(result: BookListResponse) {
                        myBooks = ArrayList(result.data)
                        callback.onSuccess(result)
                    }

                    override fun onFailure(call: Call<BookListResponse>, t: Throwable) {
                        callback.onFailure(call, t)
                    }
                })
    }

    override fun isBookMine(): Boolean {
        var is_Found: Boolean? = false
        if (myBooks == null)
            return false

        if (mBookItemInterceptor.getSavedBook() != null &&
            mBookItemInterceptor.getSavedBook()?.price == 0
        ) {
            return true
        }

        if (myBooks!!.isEmpty()) {
            return is_Found!!
        } else {
            for ((_, _, _, _, id) in myBooks!!) {
                if (mBookItemInterceptor.getSavedBook()!!.id == id) {
                    is_Found = true
                    break
                }
            }
        }
        return is_Found!!
    }

    override fun isBookMine(id2: Int): Boolean {
        var is_Found: Boolean? = false
        if (myBooks == null)
            return false
        if (myBooks!!.isEmpty()) {
            return is_Found!!
        } else {
            for ((_, _, _, _, id) in myBooks!!) {
                if (id2 == id) {
                    is_Found = true
                    break
                }
            }
        }
        return is_Found!!
    }

    override fun getMyCart(callback: RetrofitCallbacks.BookListCallback) {
        if (authResponse != null)
            mRetrofitService.getMyCart(authResponse!!.data.token,
                GlobalKeys.API_KEY,
                lang,
                mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                object : RetrofitCallbacks.BookListCallback {
                    override fun onSuccess(result: BookListResponse) {
                        callback.onSuccess(result)
                    }

                    override fun onFailure(call: Call<BookListResponse>, t: Throwable) {
                        callback.onFailure(call, t)
                    }
                })
    }

    override fun getMyBooks(): List<Book>? {
        return myBooks
    }

    override fun getMyFavouriteBooks(callback: RetrofitCallbacks.BookListCallback) {
        mRetrofitService.getMyFavouriteBooks(authResponse!!.data.token,
            GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            object : RetrofitCallbacks.BookListCallback {
                override fun onSuccess(result: BookListResponse) {
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<BookListResponse>, t: Throwable) {
                    callback.onFailure(call, t)
                }
            })
    }

    override fun getCurrentBookReviews(): List<Review> {
        return mBookItemInterceptor.getBookReviews()
    }

    override fun removeBookFromCart(book_id: Int, callback: RetrofitCallbacks.ResponseCallback) {
        mRetrofitService.removeBookFromCart(authResponse!!.data.token, GlobalKeys.API_KEY, lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            book_id, object : RetrofitCallbacks.ResponseCallback {
                override fun onSuccess(result: Response) {
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    callback.onFailure(call, t)
                }

                override fun onAuthFailure() {

                }
            })
    }

    override fun removeBookFromFavorites(
        book_id: Int,
        callback: RetrofitCallbacks.ResponseCallback
    ) {
        mRetrofitService.removeBookFromFavorites(authResponse!!.data.token,
            GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            book_id,
            object : RetrofitCallbacks.ResponseCallback {
                override fun onSuccess(result: Response) {
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    callback.onFailure(call, t)
                }

                override fun onAuthFailure() {

                }
            })
    }

    override fun getBookChapters(callback: RetrofitCallbacks.ChaptersResponseCallback) {
        mRetrofitService.getBookChapters(authResponse!!.data.token,
            GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            mBookItemInterceptor.getSavedBook()!!.id,
            object : RetrofitCallbacks.ChaptersResponseCallback {
                override fun onSuccess(result: ChaptersResponse) {
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<ChaptersResponse>, t: Throwable) {
                    callback.onFailure(call, t)
                }
            })
    }

    override fun setBookmarkData(bookmarkBody: BookmarkBody) {
        this.bookmarkBody = bookmarkBody
    }

    override fun getBookmarkData(): BookmarkBody? {
        return this.bookmarkBody
    }

    override fun addBookmark(
        bookmarkData: BookmarkBody,
        callback: RetrofitCallbacks.ResponseCallback
    ) {
        mRetrofitService.addBookmark(authResponse!!.data.token, GlobalKeys.API_KEY, lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            bookmarkData.bookId,
            bookmarkData.chapter_id,
            bookmarkData.bookmarkTime,
            bookmarkData.comment,
            object : RetrofitCallbacks.ResponseCallback {
                override fun onSuccess(result: Response) {
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<Response>, t: Throwable) {
                    callback.onFailure(call, t)
                }

                override fun onAuthFailure() {

                }
            })
    }

    override fun myBookmarks(callback: RetrofitCallbacks.MyBookmarkResponseCallback) {
        mRetrofitService.myBookmarks(authResponse!!.data.token,
            GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            object : RetrofitCallbacks.MyBookmarkResponseCallback {
                override fun onSuccess(result: MyBookmarksResponse) {
                    callback.onSuccess(result)
                }

                override fun onFailure(call: Call<MyBookmarksResponse>, t: Throwable) {
                    callback.onFailure(call, t)
                }
            })
    }

    override fun saveBookmark(bookmark: Bookmark?) {
        this.bookmark = bookmark
    }

    override fun getBookmark(): Bookmark? {
        return this.bookmark
    }

    override fun contactUs(message: String, callback: RetrofitCallbacks.ContactUsResponseCallback) {
        mRetrofitService.contactUs(
            authResponse!!.data.token, GlobalKeys.API_KEY, lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), message, callback
        )
    }

    override fun rateBook(
        rate: Int,
        comment: String,
        responseCallback: RetrofitCallbacks.ResponseCallback
    ) {
        mRetrofitService.rateBook(
            authResponse!!.data.token, GlobalKeys.API_KEY, lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            mBookItemInterceptor.getSavedBook()!!.id, rate, comment, responseCallback
        )
    }

    override fun submitGiftProperities(giftSelection: GiftSelection, quantity: Int) {
        this.giftSelection = giftSelection
        this.quantity = quantity
    }

    //Not Used
    override fun sendAsVoucher(
        email: String,
        responseCallback: RetrofitCallbacks.ResponseCallback
    ) {
        mRetrofitService.sendAsVoucher(
            authResponse!!.data.token, GlobalKeys.API_KEY, lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), email,
            mBookItemInterceptor.getSavedBook()!!.id,
            responseCallback
        )
    }

    override fun sendAsGift(
        email1: String,
        email2: String,
        email3: String,
        email4: String,
        email5: String,
        responseCallback: RetrofitCallbacks.ResponseCallback
    ) {
        mRetrofitService.sendAsGift(
            authResponse!!.data.token, GlobalKeys.API_KEY, lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            email1,
            email2,
            email3,
            email4,
            email5,
            mBookItemInterceptor.getSavedBook()!!.id,
            responseCallback
        )
    }

    override fun getDeviceToken(): String {
        return mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null)
    }

    override fun getVoucher(): Int {
        return quantity
    }

    override fun addPromoCode(
        promoCode: String,
        responseCallback: RetrofitCallbacks.PromoCodeResponseCallback
    ) {
        mRetrofitService.addPromoCode(authResponse!!.data.token,
            GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            promoCode,
            object : RetrofitCallbacks.PromoCodeResponseCallback {
                override fun onSuccess(result: PromoCodeResponse) {
                    this@DataRepository.promoCode = promoCode
                    this@DataRepository.promoCodeResponse = result
                    responseCallback.onSuccess(result)
                }

                override fun onFailure(call: Call<PromoCodeResponse>, t: Throwable) {
                    responseCallback.onFailure(call, t)
                }
            })
    }

    override fun getPromoCode(): String {
        return promoCode
    }

    override fun getPromoCodeResponse(): PromoCodeResponse? {
        return promoCodeResponse
    }

    override fun receiveBook(
        voucher: String,
        callback: RetrofitCallbacks.BookDetailsResponseCallback
    ) {
        mRetrofitService.receiveBook(
            authResponse!!.data.token, GlobalKeys.API_KEY, lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null), voucher, callback
        )
    }

    override fun saveVoucherBook(data: Book?) {
        this.voucherBook = data
    }

    override fun getVoucherBook(): Book? {
        return voucherBook
    }

    override fun setActiveTab(tabActive: ActiveTab) {
        this.activeTab = tabActive
    }

    override fun getActiveTab(): ActiveTab? {
        return activeTab
    }

    override fun getPaypalArguments(callback: RetrofitCallbacks.PaypalArgumentCallback) {
        var paypalArguments = PaypalArguments()

        mRetrofitService.getPaypalStatus(authResponse!!.data.token,
            GlobalKeys.API_KEY,
            lang,
            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
            object : RetrofitCallbacks.PaypalStatusResponseCallback {
                override fun onSuccess(result: PaypalStatusResponse?) {
                    if (result?.status?.status == 1) {
                        mRetrofitService.getPaypalKey(authResponse!!.data.token,
                            GlobalKeys.API_KEY,
                            lang,
                            mCacheDataSource.getStringFromCache(GlobalKeys.StoreData.TOKEN, null),
                            object : RetrofitCallbacks.PaypalStatusResponseCallback {
                                override fun onSuccess(result: PaypalStatusResponse?) {
                                    if (result?.status?.status == 1) {
                                        paypalArguments.key = result?.data
                                        mRetrofitService.getPaypalAmount(authResponse!!.data.token,
                                            GlobalKeys.API_KEY,
                                            lang,
                                            mCacheDataSource.getStringFromCache(
                                                GlobalKeys.StoreData.TOKEN,
                                                null
                                            ),
                                            object :
                                                RetrofitCallbacks.PaypalStatusResponseCallback {
                                                override fun onSuccess(result: PaypalStatusResponse?) {
                                                    if (result?.status?.status == 1) {
                                                        paypalArguments.dollarPrice =
                                                            result?.data.toDouble()
                                                        callback.onSuccess(paypalArguments)
                                                    } else {
                                                        callback.onSuccess(paypalArguments)
                                                    }
                                                }

                                                override fun onFailure(
                                                    call: Call<PaypalStatusResponse>?,
                                                    t: Throwable?
                                                ) {
                                                    callback.onSuccess(paypalArguments)
                                                }
                                            })
                                    } else {
                                        callback.onSuccess(paypalArguments)
                                    }
                                }

                                override fun onFailure(
                                    call: Call<PaypalStatusResponse>?,
                                    t: Throwable?
                                ) {
                                    callback.onSuccess(paypalArguments)
                                }
                            })
                    } else {
                        callback.onSuccess(paypalArguments)
                    }
                }

                override fun onFailure(call: Call<PaypalStatusResponse>?, t: Throwable?) {
                    callback.onSuccess(null)
                }
            })
    }

    override fun getMediaItems(): List<MediaBrowserCompat.MediaItem> {
        return mMediaItems
    }

    override fun getTreeMap(): TreeMap<String, MediaMetadataCompat> {
        return mTreeMap
    }

    override fun setMediaItems(mediaItems: List<ChaptersData>) {
        mMediaItems.clear()
        mBookItemInterceptor.saveBookChapters(mediaItems)
        for (item in mediaItems) {
            Log.d(TAG, "setMediaItems: called: adding media item: " + item.title)
            val convertedItem = getMediaItemFromChapterData(item)
            convertedItem?.let {
                mMediaItems.add(
                    MediaBrowserCompat.MediaItem(
                        it.description, MediaBrowserCompat.MediaItem.FLAG_PLAYABLE
                    )
                )
                mTreeMap[it.description.mediaId.toString()] = it
            }
        }

        Log.d("MediaItems", mMediaItems.toString())
        Log.d("Map", mTreeMap.toString())
    }

    private fun getMediaItemFromChapterData(data: ChaptersData): MediaMetadataCompat? {

        val book = getSavedBook()

        book?.let {
            return MediaMetadataCompat.Builder()
                .putString(
                    MediaMetadataCompat.METADATA_KEY_MEDIA_ID,
                    data.id.toString()
                )
                .putString(
                    MediaMetadataCompat.METADATA_KEY_ARTIST,
                    it.author
                )
                .putString(
                    MediaMetadataCompat.METADATA_KEY_TITLE,
                    data.title
                )
                .putString(
                    MediaMetadataCompat.METADATA_KEY_MEDIA_URI,
                    data.sound_file
                )
                .putString(
                    MediaMetadataCompat.METADATA_KEY_DISPLAY_DESCRIPTION,
                    ""
                )
                .putString(
                    MediaMetadataCompat.METADATA_KEY_DATE,
                    it.release_date
                )
                .putString(MediaMetadataCompat.METADATA_KEY_DISPLAY_ICON_URI, it.cover)
                .build()
        }

        return null
    }

    fun getMediaItem(mediaId: String): MediaMetadataCompat? {
        return mTreeMap[mediaId]
    }

    companion object {

        private var INSTANCE: DataRepository? = null

        fun getInstance(context: Context): DataRepository {
            if (INSTANCE == null) {
                INSTANCE = DataRepository(context)
            }
            return INSTANCE as DataRepository
        }
    }
}