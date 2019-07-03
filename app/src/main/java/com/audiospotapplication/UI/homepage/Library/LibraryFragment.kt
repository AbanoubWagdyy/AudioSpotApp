package com.audiospotapplication.UI.homepage.Library

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.audiospotapplication.R
import com.audiospotapplication.UI.authors.AuthorsActivity
import com.audiospotapplication.UI.books.BooksActivity
import com.audiospotapplication.UI.categories.CategoriesActivity
import com.audiospotapplication.UI.publishers.PublishersActivity
import kotlinx.android.synthetic.main.fragment_my_library.*

class LibraryFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_library, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        relativeBooks.setOnClickListener {
            val intent = Intent(activity, BooksActivity::class.java)
            startActivity(intent)

        }

        relativeAuthors.setOnClickListener {
            val intent = Intent(activity, AuthorsActivity::class.java)
            startActivity(intent)
        }

        relativeCategories.setOnClickListener {
            val intent = Intent(activity, CategoriesActivity::class.java)
            startActivity(intent)
        }

        relativePublishers.setOnClickListener {
            val intent = Intent(activity, PublishersActivity::class.java)
            startActivity(intent)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LibraryFragment()
    }
}