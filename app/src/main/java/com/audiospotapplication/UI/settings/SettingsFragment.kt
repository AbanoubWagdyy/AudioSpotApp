package com.audiospotapplication.UI.settings

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.audiospotapplication.BaseFragment
import com.audiospotapplication.DataLayer.Retrofit.GlobalKeys

import com.audiospotapplication.R
import com.audiospotapplication.UI.splash.SplashActivity
import com.audiospotapplication.utils.ApplicationLanguageHelper
import kotlinx.android.synthetic.main.fragment_settings.*
import java.util.*

class SettingsFragment : BaseFragment(), SettingsContract.View {

    override fun restartAppWithDifferentLanguage(language: String) {

        ApplicationLanguageHelper.wrap(activity!!.applicationContext, language)

        val intent = Intent(activity!!, SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        activity!!.finish()
    }

    override fun showLanguageListDialog() {

        val builderSingle = AlertDialog.Builder(activity!!)
        builderSingle.setIcon(R.mipmap.ic_launcher)
        builderSingle.setTitle("Select Language:-")

        val arrayAdapter = ArrayAdapter<String>(activity!!, android.R.layout.simple_list_item_1)
        arrayAdapter.add(GlobalKeys.Language.ENGLISH)
        arrayAdapter.add(GlobalKeys.Language.ARABIC)

        builderSingle.setAdapter(
            arrayAdapter
        ) { p0, p1 ->
            val strName = arrayAdapter.getItem(p1)
            mPresenter.handleLanguageSelection(strName)
        }

        builderSingle.show()
    }

    override fun getAppContext(): Context? {
        return activity!!.applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter = SettingsPresenter(this)
        mPresenter.start()

        relativeDuration.setOnClickListener {
            mPresenter.handleDurationPressed()
        }

        relativeAppLanguage.setOnClickListener {
            mPresenter.handleAppLanguagePressed()
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            SettingsFragment()
    }

    lateinit var mPresenter: SettingsContract.Presenter
}
