package com.audiospotapplication.UI.updateProfile


import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.text.style.UnderlineSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.audiospotapplication.BaseFragment
import com.audiospotapplication.R
import com.audiospotapplication.UI.changePassword.ChangePasswordActivity
import com.audiospotapplication.utils.DialogUtils
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_update_profile.*


class UpdateProfileFragment : BaseFragment(), UpdateProfileContract.View {
    override fun showLoading() {
        DialogUtils.showProgressDialog(activity!!,"Loading ...")
    }

    override fun dismissLoading() {
        DialogUtils.dismissProgressDialog()
    }

    override fun disableChangePasswordClick() {

        tvChangePassword.setTextColor(activity!!.resources.getColor(R.color.grey))
        tvChangePassword.setOnClickListener {
        }
    }

    override fun setChangePasswordClick() {
        val intent = Intent(activity!!.applicationContext, ChangePasswordActivity::class.java)
        startActivity(intent)
    }

    override fun showInvalidEmailMessage(s: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), s, Snackbar.LENGTH_SHORT).show()
    }

    override fun finalizeView() {
        activity!!.finish()
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(activity!!.findViewById(android.R.id.content), message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showCompleteYourFieldsMessage() {
        Snackbar.make(activity!!.findViewById(android.R.id.content), "Please Complete All Fields", Snackbar.LENGTH_SHORT).show()
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    lateinit var mPresenter: UpdateProfileContract.Presenter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_update_profile, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var content = SpannableString("Change Password")
        content.setSpan(UnderlineSpan(), 0, content.length, 0)
        tvChangePassword.text = content

        mPresenter = UpdateProfilePresenter(this)

        tvChangePassword.setOnClickListener {
            mPresenter.handleChangePasswordClicked()
        }
//        val dataset = LinkedList(asList("Gender", "Male", "Female"))
//        spinnerGender.attachDataSource<String>(dataset)
//
//        spinnerGender.onSpinnerItemSelectedListener =
//            OnSpinnerItemSelectedListener { parent, view, position, id ->
//                gender = parent!!.getItemAtPosition(position) as String
//            }

        btnUpdate.setOnClickListener {
            mPresenter.updateProfile(
                etFirstName.text.toString(), etLastName.text.toString(),
                etEmail.text.toString(),
                etMobilePhone.text.toString()
            )
        }
    }


    companion object {
        @JvmStatic
        fun newInstance() =
            UpdateProfileFragment()
    }
}