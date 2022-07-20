package com.greentea.gitusers2.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.greentea.gitusers2.R
import com.greentea.gitusers2.databinding.ActivityUsersBinding
import com.greentea.gitusers2.services.models.db.UsersDB
import com.greentea.gitusers2.services.models.repositories.UsersRepository
import com.greentea.gitusers2.viewmodels.UsersViewModel
import com.greentea.gitusers2.viewmodels.factory.UsersViewModelProviderFactory

class UsersActivity : AppCompatActivity() {
    private lateinit var bindingUser: ActivityUsersBinding
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var usersViewModel: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        bindingUser = ActivityUsersBinding.inflate(layoutInflater)
        val view = bindingUser.root
        setContentView(view)

        supportActionBar?.title = "Git User's"

        //CONNECT BOTTOM NAVIGATION TO THIS ACTIVITY
        bottomNavigationView = bindingUser.bnvActionMenu
        val navController = findNavController(R.id.fm_nav_host_users)
        bottomNavigationView.setupWithNavController(navController)

        val usersRepository = UsersRepository(UsersDB(this))
        val usersViewModelFactory = UsersViewModelProviderFactory(usersRepository)
        usersViewModel = ViewModelProvider(
            this, usersViewModelFactory
        )[UsersViewModel::class.java]
    }
}