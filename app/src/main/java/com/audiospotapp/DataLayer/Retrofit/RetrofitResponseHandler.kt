package com.audiospotapp.DataLayer.Retrofit

import com.audiospot.DataLayer.Model.AuthResponse
import com.audiospot.DataLayer.Model.BookDetailsResponse
import com.audiospot.DataLayer.Model.CategoriesListResponse
import com.audiospot.DataLayer.Model.HomepageRepsonse
import com.audiospotapp.DataLayer.Model.*

class RetrofitResponseHandler {

    companion object {
        fun validateAuthResponseStatus(result: AuthResponse?): Status {
            when (result!!.status) {
                1 -> {
                    return Status.VALID
                }
                else -> {
                    return Status.INVALID
                }
            }
        }

        fun validateAuthResponseStatus(result: LogoutAuthResponse?): Status {
            when (result!!.status) {
                1 -> {
                    return Status.VALID
                }
                else -> {
                    return Status.INVALID
                }
            }
        }

        fun validateAuthResponseStatus(result: HomepageRepsonse?): Status {
            when (result!!.status) {
                1 -> {
                    return Status.VALID
                }
                else -> {
                    return Status.INVALID
                }
            }
        }

        fun validateAuthResponseStatus(result: CategoriesListResponse?): Status {
            when (result!!.status) {
                1 -> {
                    return Status.VALID
                }
                else -> {
                    return Status.INVALID
                }
            }
        }

        fun validateAuthResponseStatus(result: BookListResponse?): Status {
            when (result!!.status) {
                1 -> {
                    return Status.VALID
                }
                else -> {
                    return Status.INVALID
                }
            }
        }

        fun validateAuthResponseStatus(result: AuthorsResponse?): Status {
            when (result!!.status) {
                1 -> {
                    return Status.VALID
                }
                else -> {
                    return Status.INVALID
                }
            }
        }

        fun validateAuthResponseStatus(result: PublishersResponse?): Status {
            when (result!!.status) {
                1 -> {
                    return Status.VALID
                }
                else -> {
                    return Status.INVALID
                }
            }
        }

        fun validateAuthResponseStatus(result: BookDetailsResponse): Status {
            when (result!!.status) {
                1 -> {
                    return Status.VALID
                }
                else -> {
                    return Status.INVALID
                }
            }
        }

        fun validateAuthResponseStatus(result: ReviewListResponse): Status {
            when (result!!.status) {
                1 -> {
                    return Status.VALID
                }
                else -> {
                    return Status.INVALID
                }
            }
        }

        fun validateAuthResponseStatus(result: Response?): Status {
            when (result!!.status) {
                1 -> {
                    return Status.VALID
                }
                else -> {
                    return Status.INVALID
                }
            }
        }

        fun validateResponseStatus(result: ChaptersResponse?): Status {
            when (result!!.status) {
                1 -> {
                    return Status.VALID
                }
                else -> {
                    return Status.INVALID
                }
            }
        }

        fun validateAuthResponseStatus(result: MyBookmarksResponse?): Status {
            return when (result!!.status) {
                1 -> {
                    Status.VALID
                }
                else -> {
                    Status.INVALID
                }
            }
        }

        fun validateAuthResponseStatus(result: ContactUsResponse?): Status {
            return when (result!!.status.status) {
                1 -> {
                    Status.VALID
                }
                else -> {
                    Status.INVALID
                }
            }
        }

        fun validateAuthResponseStatus(result: PromoCodeResponse?): Status {
            return when (result!!.status) {
                1 -> {
                    Status.VALID
                }
                else -> {
                    Status.INVALID
                }
            }
        }

        enum class Status {
            VALID, INVALID
        }
    }
}