package com.greentea.gitusers2.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.greentea.gitusers2.R
import com.greentea.gitusers2.databinding.FragmentListUsersBinding
import com.greentea.gitusers2.services.models.db.UsersDB
import com.greentea.gitusers2.services.models.repositories.UsersRepository
import com.greentea.gitusers2.ui.adapters.ListUsersAdapter
import com.greentea.gitusers2.utils.Resources
import com.greentea.gitusers2.viewmodels.UsersViewModelFragment
import com.greentea.gitusers2.viewmodels.factory.UsersViewModelProviderFactoryFragment
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ListUsersFragment : Fragment() {
    private var _binding: FragmentListUsersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val tag1 = "ListUsersFragment"
    private lateinit var usersViewModelFragment: UsersViewModelFragment
    private lateinit var listUsersAdapter: ListUsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentListUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "List Users"

        //CREATE CONNECTION ->
        val usersRepository = UsersRepository(UsersDB(requireActivity().applicationContext))
        val usersViewModelFactoryFragment =
            UsersViewModelProviderFactoryFragment(usersRepository)
        usersViewModelFragment = ViewModelProvider(
            this, usersViewModelFactoryFragment
        )[UsersViewModelFragment::class.java]

        configRecyclerView()

        listUsersAdapter.setOnItemClickListener {
            val bundle = Bundle().apply { putSerializable("user", it) }
            findNavController().navigate(R.id.action_listUsersFragment_to_userDetailFragment, bundle)
        }

        var job: Job? = null
        binding.svSearchUser.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                job?.cancel()
                job = MainScope().launch {
                    delay(500L)
                    p0?.let {
                        if (p0.isNotEmpty()) {
                            usersViewModelFragment.getResultSearch(p0)
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                observeListUser()
                return false
            }
        })

        observeListUser()
        observeSearchUser()
    }

    private fun observeListUser() {
        usersViewModelFragment.listUsers.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    hideProgressBar()
                    response.data?.let { userResponse ->
                        listUsersAdapter.differAsync.submitList(userResponse)
                    }
                }
                is Resources.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(tag1, "An error occurred: $message")
                    }
                }
                is Resources.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun observeSearchUser() {
        usersViewModelFragment.searchUser.observe(viewLifecycleOwner) { response ->
            when (response) {
                is Resources.Success -> {
                    hideProgressBar()
                    response.data?.let { userResponse ->
                        listUsersAdapter.differAsync.submitList(userResponse.items)
                    }
                }
                is Resources.Error -> {
                    hideProgressBar()
                    response.message?.let { message ->
                        Log.e(tag1, "An error occurred: $message")
                    }
                }
                is Resources.Loading -> {
                    showProgressBar()
                }
            }
        }
    }

    private fun configRecyclerView() {
        listUsersAdapter = ListUsersAdapter(requireContext())
        binding.rvListUser.apply {
            adapter = listUsersAdapter
            layoutManager =
                if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(activity, 2)
                } else {
                    LinearLayoutManager(activity)
                }
        }
    }

    private fun showProgressBar() {
        binding.pbListUser.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.pbListUser.visibility = View.INVISIBLE
    }
}