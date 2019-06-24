package com.audiospotapp.DataLayer.Retrofit;

import com.audiospot.DataLayer.Model.AuthResponse;
import com.audiospot.DataLayer.Model.BookDetailsResponse;
import com.audiospot.DataLayer.Model.CategoriesListResponse;
import com.audiospot.DataLayer.Model.HomepageRepsonse;
import com.audiospotapp.DataLayer.Model.*;
import retrofit2.Call;

public interface RetrofitCallbacks {

    interface AuthResponseCallback {
        void onSuccess(AuthResponse result);

        void onFailure(Call<AuthResponse> call, Throwable t);
    }

    interface LogoutAuthResponseCallback {
        void onSuccess(LogoutAuthResponse result);

        void onFailure(Call<LogoutAuthResponse> call, Throwable t);
    }

    interface HomepageResponseCallback {
        void onSuccess(HomepageRepsonse result);

        void onFailure(Call<HomepageRepsonse> call, Throwable t);
    }

    interface BookListCallback {
        void onSuccess(BookListResponse result);

        void onFailure(Call<BookListResponse> call, Throwable t);
    }


    interface CategoriesListCallback {
        void onSuccess(CategoriesListResponse result);

        void onFailure(Call<CategoriesListResponse> call, Throwable t);
    }

    interface AuthorsResponseCallback {
        void onSuccess(AuthorsResponse result);

        void onFailure(Call<AuthorsResponse> call, Throwable t);
    }

    interface PublishersResponseCallback {
        void onSuccess(PublishersResponse result);

        void onFailure(Call<PublishersResponse> call, Throwable t);
    }

    interface ProfileResponseCallback {
        void onSuccess(ProfileResponse result);

        void onFailure(Call<ProfileResponse> call, Throwable t);
    }

    interface BookDetailsResponseCallback {
        void onSuccess(BookDetailsResponse result);

        void onFailure(Call<BookDetailsResponse> call, Throwable t);
    }

    interface ReviewListResponseCallback {
        void onSuccess(ReviewListResponse result);

        void onFailure(Call<ReviewListResponse> call, Throwable t);
    }

    interface ResponseCallback {
        void onSuccess(Response result);

        void onFailure(Call<Response> call, Throwable t);

        void onAuthFailure();
    }

    interface ChaptersResponseCallback {
        void onSuccess(ChaptersResponse result);

        void onFailure(Call<ChaptersResponse> call, Throwable t);
    }

    interface MyBookmarkResponseCallback {
        void onSuccess(MyBookmarksResponse result);

        void onFailure(Call<MyBookmarksResponse> call, Throwable t);
    }

    interface ContactUsResponseCallback {
        void onSuccess(ContactUsResponse result);

        void onFailure(Call<ContactUsResponse> call, Throwable t);
    }
}
