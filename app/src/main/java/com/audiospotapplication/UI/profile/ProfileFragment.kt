package com.audiospotapplication.UI.profile


import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospotapplication.BaseFragment

import com.audiospotapplication.R
import com.audiospotapplication.utils.ImageUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : BaseFragment(), ProfileContract.View {
    override fun setUserImage(profile_photo: String?) {
        ImageUtils.setImageFromUrlIntoImageViewUsingGlide(
            profile_photo, context, profile_image
            , false
        )
    }

    lateinit var mPresenter: ProfileContract.Presenter

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    override fun setFullName(full_name: String) {
        if (full_name != null)
            tvFullName.text = full_name
    }

    override fun setEmail(email: String?) {
        if (email != null)
            tvEmail.text = email
    }

    override fun setMobilePhone(phone: String?) {
        if (phone != null)
            tvMobilePhone.text = phone
    }

    override fun showErrorMessage(s: String?) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), s!!, Snackbar.LENGTH_SHORT).show()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var content = SpannableString("Preferred Categories")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        tvPreferredCategories.text = content

        mPresenter = ProfilePresenter(this)
        mPresenter.start()
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            ProfileFragment()
    }
}