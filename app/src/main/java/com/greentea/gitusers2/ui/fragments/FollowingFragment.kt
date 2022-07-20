package com.greentea.gitusers2.ui.fragments

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.greentea.gitusers2.R
import com.greentea.gitusers2.databinding.FragmentFollowingBinding
import com.greentea.gitusers2.services.models.db.UsersDB
import com.greentea.gitusers2.services.models.repositories.UsersRepository
import com.greentea.gitusers2.ui.adapters.ListUsersAdapter
import com.greentea.gitusers2.utils.Resources
import com.greentea.gitusers2.viewmodels.UsersViewModelFragment
import com.greentea.gitusers2.viewmodels.factory.UsersViewModelProviderFactoryFragment

class FollowingFragment : Fragment() {
    private var _binding: FragmentFollowingBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val tag1 = "FollowersFragment"
    private lateinit var usersViewModelFragment: UsersViewModelFragment
    private lateinit var listUsersAdapter: ListUsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val username = arguments?.getString(EXTRA_USER)

        //CREATE CONNECTION ->
        val usersRepository = UsersRepository(UsersDB(requireActivity().applicationContext))
        val usersViewModelFactoryFragment =
            UsersViewModelProviderFactoryFragment(usersRepository)
        usersViewModelFragment = ViewModelProvider(
            this, usersViewModelFactoryFragment
        )[UsersViewModelFragment::class.java]

        configRecyclerView()

        listUsersAdapter.setOnItemClickListener {
            Toast.makeText(requireContext(), it.login, Toast.LENGTH_SHORT).show()
            val bundle = Bundle().apply { putSerializable("user", it) }
            findNavController().navigate(R.id.action_userDetailFragment_self, bundle)
        }

        usersViewModelFragment.getFollowingUser(username!!)
        observeFollowingUser()
    }

    private fun observeFollowingUser() {
        usersViewModelFragment.followingUser.observe(viewLifecycleOwner) { response ->
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

    private fun configRecyclerView() {
        listUsersAdapter = ListUsersAdapter(requireContext())
        binding.rvListFollowing.apply {
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
        binding.pbListFollowing.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding.pbListFollowing.visibility = View.INVISIBLE
    }

    companion object{
        const val EXTRA_USER = "extra_user"

        @JvmStatic
        fun newInstance(index: Int, username: String) = FollowingFragment().apply {
            arguments = Bundle().apply {
                putString(EXTRA_USER, username)
            }
        }
    }
}