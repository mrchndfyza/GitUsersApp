package com.greentea.gitusers2.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.greentea.gitusers2.services.models.repositories.UsersRepository
import com.greentea.gitusers2.viewmodels.UsersViewModelFragment

@Suppress("UNCHECKED_CAST")
class UsersViewModelProviderFactoryFragment(
    private val usersRepository: UsersRepository
    ): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UsersViewModelFragment(usersRepository) as T
    }
}