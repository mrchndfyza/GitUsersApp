package com.greentea.gitusers2.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.greentea.gitusers2.viewmodels.SwitchModeViewModel
import com.greentea.gitusers2.viewmodels.preferences.SettingPreferences

class SwitchModeViewModelFactory(
    private val pref: SettingPreferences
    ) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SwitchModeViewModel::class.java)) {
            return SwitchModeViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}