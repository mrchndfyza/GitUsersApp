package com.greentea.gitusers2.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.greentea.gitusers2.R
import com.greentea.gitusers2.databinding.FragmentUserDetailBinding
import com.greentea.gitusers2.services.models.db.UsersDB
import com.greentea.gitusers2.services.models.repositories.UsersRepository
import com.greentea.gitusers2.ui.adapters.SectionsPagerAdapter
import com.greentea.gitusers2.utils.Resources
import com.greentea.gitusers2.viewmodels.UsersViewModelFragment
import com.greentea.gitusers2.viewmodels.factory.UsersViewModelProviderFactoryFragment


class UserDetailFragment : Fragment() {
    private var _binding: FragmentUserDetailBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val tag1 = "UserDetailFragment"
    private lateinit var usersViewModelFragment: UsersViewModelFragment

    private val args: UserDetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentUserDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.title = "Detail User"

        val usersRepository = UsersRepository(UsersDB(requireActivity().applicationContext))
        val usersViewModelFactoryFragment =
            UsersViewModelProviderFactoryFragment(usersRepository)
        usersViewModelFragment = ViewModelProvider(
            this, usersViewModelFactoryFragment
        )[UsersViewModelFragment::class.java]

        val user = args.user

        //GET USERNAME FROM ARGUMENT
        val username = user.login

        usersViewModelFragment.getUserDetail(username)
        observeUserDetail()

        //CONFIGURATION FOR TAB LAYOUT AND VIEW PAGER
        val sectionsPagerAdapter = SectionsPagerAdapter(activity as AppCompatActivity)
        val viewPager: ViewPager2 = binding.viewPager
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = binding.tabUser
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
//        supportActionBar?.elevation = 0f
        sectionsPagerAdapter.username = username

        //CONFIGURATION FOR FAVORITE USER
        binding.fabFavorite.setOnClickListener {
            usersViewModelFragment.insertFavoriteUser(user)
            Toast.makeText(
                activity, "User successfully added to favorite!", Toast.LENGTH_SHORT
            ).show()
        }

        val gitHubLink = (user.html_url)
        println(gitHubLink)
        //CONFIGURATION FOR SHARE BUTTON
        binding.btnShare.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, gitHubLink)
                type = "text/plain"
            }

            val shareIntent = Intent.createChooser(sendIntent, null)
            startActivity(shareIntent)
        }
    }

    private fun observeUserDetail() {
        usersViewModelFragment.detailUserItem.observe(viewLifecycleOwner) { response ->
            if (response is Resources.Success) {
                response.data?.let { userResponse ->
                    binding.userDetail.visibility = View.VISIBLE
                    binding.tvItemName.text = userResponse.name.toString()
                    binding.tvItemUsername.text = userResponse.login.toString()
                    binding.tvItemCompany.text = userResponse.company.toString()
                    binding.tvItemLocation.text = userResponse.location.toString()
                    Glide.with(this)
                        .load(userResponse.avatar_url)
                        .into(binding.imgItemPhoto)
                    binding.tvFollowerNumber.text =
                        getString(R.string.followers, userResponse.followers.toString())
                    binding.tvFollowingNumber.text =
                        getString(R.string.following, userResponse.following.toString())
                    binding.tvRepositories.text =
                        getString(R.string.repositories, userResponse.public_repos.toString())
                }
            } else if (response is Resources.Error) {
                response.message?.let { message ->
                    Log.e(tag1, "An error occurred: $message")
                }
            }
        }
    }

    companion object{
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }
}