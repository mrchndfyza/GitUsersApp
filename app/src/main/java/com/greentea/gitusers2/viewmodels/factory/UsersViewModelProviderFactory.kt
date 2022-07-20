package com.greentea.gitusers2.viewmodels.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.greentea.gitusers2.services.models.repositories.UsersRepository
import com.greentea.gitusers2.viewmodels.UsersViewModel

@Suppress("UNCHECKED_CAST")
class UsersViewModelProviderFactory(
    private val usersRepository: UsersRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return UsersViewModel(usersRepository) as T
    }
}