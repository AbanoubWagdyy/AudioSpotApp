package com.visionvalley.letuno.DataLayer

import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospot.DataLayer.Model.Book
import com.audiospot.DataLayer.Model.CategoriesListData
import com.audiospotapp.DataLayer.Cache.CacheDataSource
import com.audiospotapp.DataLayer.Model.*
import com.audiospotapp.DataLayer.Retrofit.RetrofitCallbacks

interface RepositorySource : CacheDataSource {

    fun login(username: String, password: String, callback: RetrofitCallbacks.AuthResponseCallback)

    fun socialLogin(
        first_name: String,
        last_name: String,
        email: String,
        social_source: String,
        social_id: String,
        callback: RetrofitCallbacks.AuthResponseCallback
    )

    fun register(
        first_name: String,
        last_name: String,
        email: String,
        mobile_phone: String,
        password: String,
        callback: RetrofitCallbacks.AuthResponseCallback
    )

    fun resetPassword(email: String, callback: RetrofitCallbacks.AuthResponseCallback)

    fun getHomepage(callback: RetrofitCallbacks.HomepageResponseCallback)

    fun getAllCategories(callback: RetrofitCallbacks.CategoriesListCallback)

    fun getAllAuthors(callback: RetrofitCallbacks.AuthorsResponseCallback)

    fun getAllPublishers(callback: RetrofitCallbacks.PublishersResponseCallback)

    fun clearBookFilters()

    fun getBooks(
        book: String,
        sort: String,
        author: Int,
        publisher: Int,
        narrator: Int,
        category: Int,
        bookListCallback: RetrofitCallbacks.BookListCallback
    )

    fun getAuthResponse(): AuthResponse?

    fun saveCategoryItem(categoryListData: CategoriesListData)

    fun getCurrentCategoryItem(): CategoriesListData

    fun clearCategoryItem()

    fun saveAuthorItem(authorsData: AuthorsData)

    fun getAuthorItem(): AuthorsData

    fun clearAuthorItem()

    fun savePublisherItem(authorsData: PublishersResponseData)

    fun getPublisherItem(): PublishersResponseData

    fun clearPublisherItem()

    fun saveBook(book: Book)

    fun getSavedBook(): Book

    fun clearSavedBook()

    fun signOut(callback: RetrofitCallbacks.LogoutAuthResponseCallback)

    fun getProfile(callback: RetrofitCallbacks.ProfileResponseCallback)
    fun updateProfile(
        first_name: String,
        last_name: String,
        email: String,
        mobile_phone: String,
        authResponseCallback: RetrofitCallbacks.AuthResponseCallback
    )

    fun updatePassword(
        old_password: String,
        new_password: String,
        confirm_password: String,
        authResponseCallback: RetrofitCallbacks.AuthResponseCallback
    )

    fun getBookDetails(callback: RetrofitCallbacks.BookDetailsResponseCallback)

    fun getBookDetailsWithId(bookId: Int, callback: RetrofitCallbacks.BookDetailsResponseCallback)

    fun getMyBooks(callback: RetrofitCallbacks.BookListCallback)

    fun getMyCart(callback: RetrofitCallbacks.BookListCallback)

    fun getMyFavouriteBooks(callback: RetrofitCallbacks.BookListCallback)

    fun sendGift(email: String, responseCallback: RetrofitCallbacks.ResponseCallback)

    fun getBookReviews(callback: RetrofitCallbacks.ReviewListResponseCallback)

    fun addBookToCart(callback: RetrofitCallbacks.ResponseCallback)

    fun addBookToFavorites(callback: RetrofitCallbacks.ResponseCallback)

    fun isBookMine(): Boolean

    fun getMyBooks(): List<Book>

    fun getCurrentBookReviews(): List<Review>

    fun removeBookFromCart(book_id: Int, callback: RetrofitCallbacks.ResponseCallback)

    fun removeBookFromFavorites(book_id: Int, callback: RetrofitCallbacks.ResponseCallback)

    fun getBookChapters(callback: RetrofitCallbacks.ChaptersResponseCallback)

    fun setBookmarkData(bookmarkBody: BookmarkBody)

    fun getBookmarkData(): BookmarkBody

    fun addBookmark(bookmarkData: BookmarkBody, callback: RetrofitCallbacks.ResponseCallback)

    fun myBookmarks(callback: RetrofitCallbacks.MyBookmarkResponseCallback)

    fun contactUs(message: String, callback: RetrofitCallbacks.ContactUsResponseCallback)

    fun saveBookmark(bookmark: Bookmark?)

    fun getBookmark(): Bookmark?
}