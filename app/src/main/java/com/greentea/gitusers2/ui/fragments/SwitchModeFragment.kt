package com.greentea.gitusers2.ui.fragments

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.switchmaterial.SwitchMaterial
import com.greentea.gitusers2.R
import com.greentea.gitusers2.databinding.FragmentSwitchModeBinding
import com.greentea.gitusers2.viewmodels.SwitchModeViewModel
import com.greentea.gitusers2.viewmodels.factory.SwitchModeViewModelFactory
import com.greentea.gitusers2.viewmodels.preferences.SettingPreferences

class SwitchModeFragment : Fragment() {
    private var _binding: FragmentSwitchModeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSwitchModeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Switch Mode"

        val switchTheme = getView()?.findViewById<SwitchMaterial>(R.id.switch_theme)

        val dataStore = context?.dataStore
        val pref = SettingPreferences.getInstance(dataStore!!)
        val switchModeViewModel = ViewModelProvider(
            this, SwitchModeViewModelFactory(pref))[SwitchModeViewModel::class.java]
        switchModeViewModel.getThemeSettings().observe(viewLifecycleOwner
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme?.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme?.isChecked = false
            }
        }

        switchTheme?.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            switchModeViewModel.saveThemeSetting(isChecked)
        }
    }
}