package com.greentea.gitusers2.ui.fragments

import android.annotation.SuppressLint
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.greentea.gitusers2.R
import com.greentea.gitusers2.databinding.FragmentFavoriteUsersBinding
import com.greentea.gitusers2.services.models.db.UsersDB
import com.greentea.gitusers2.services.models.repositories.UsersRepository
import com.greentea.gitusers2.ui.adapters.ListUsersAdapter
import com.greentea.gitusers2.viewmodels.UsersViewModelFragment
import com.greentea.gitusers2.viewmodels.factory.UsersViewModelProviderFactoryFragment

class FavoriteUsersFragment : Fragment() {
    private var _binding: FragmentFavoriteUsersBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var usersViewModelFragment: UsersViewModelFragment
    private lateinit var listUsersAdapter: ListUsersAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentFavoriteUsersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.title = "Favorite Users"

        Toast.makeText(activity, "Swipe to delete user", Toast.LENGTH_SHORT).show()

        val usersRepository = UsersRepository(UsersDB(requireActivity().applicationContext))
        val usersViewModelFactoryFragment =
            UsersViewModelProviderFactoryFragment(usersRepository)
        usersViewModelFragment = ViewModelProvider(
            this, usersViewModelFactoryFragment
        )[UsersViewModelFragment::class.java]

        configRecyclerView()

        listUsersAdapter.setOnItemClickListener {
            val bundle = Bundle().apply { putSerializable("user", it) }
            findNavController()
                .navigate(R.id.action_favoriteUsersFragment_to_userDetailFragment, bundle)
        }

        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val article = listUsersAdapter.differAsync.currentList[position]
                usersViewModelFragment.deleteFavoriteUser(article)
                Toast.makeText(activity, "User successfully delete from favorite!", Toast.LENGTH_SHORT).show()
            }
        }

        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.rvListUserFavorite)
        }

        usersViewModelFragment.getFavoriteUser().observe(viewLifecycleOwner) { response ->
            listUsersAdapter.differAsync.submitList(response)
        }

        observeFavoriteUser()
    }

    private fun observeFavoriteUser(){
        usersViewModelFragment.getFavoriteUser().observe(viewLifecycleOwner) {response ->
            listUsersAdapter.differAsync.submitList(response)
        }
    }

    private fun configRecyclerView() {
        listUsersAdapter = ListUsersAdapter(requireContext())
        binding.rvListUserFavorite.apply {
            adapter = listUsersAdapter
            layoutManager =
                if (requireActivity().resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    GridLayoutManager(activity, 2)
                } else {
                    LinearLayoutManager(activity)
                }
        }
    }

//    private fun deleteFavoriteUser(){
//        val listPosition = holder.adapterPosition
//        val user = listUsersAdapter.differAsync.currentList[listPosition]
//        usersViewModelFragment.deleteFavoriteUser(user)
//        Toast.makeText(
//            activity, "User successfully delete from favorite!", Toast.LENGTH_SHORT
//        ).show()
//    }
}