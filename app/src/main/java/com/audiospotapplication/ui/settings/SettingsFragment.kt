package com.audiospotapplication.ui.settings

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import com.audiospotapplication.ui.BaseFragment
import com.audiospotapplication.data.retrofit.GlobalKeys

import com.audiospotapplication.R
import com.audiospotapplication.ui.splash.SplashActivity
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment(), SettingsContract.View {

    override fun restartAppWithDifferentLanguage(language: String) {

        val intent = Intent(requireActivity(), SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        startActivity(intent)
        requireActivity().finish()
    }

    override fun showLanguageListDialog() {

        val builderSingle = AlertDialog.Builder(requireActivity())
        builderSingle.setIcon(R.mipmap.ic_launcher)
        builderSingle.setTitle(requireContext().getString(R.string.select_language))

        val arrayAdapter = ArrayAdapter<String>(requireActivity(), android.R.layout.simple_list_item_1)
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
        return requireActivity().applicationContext
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
