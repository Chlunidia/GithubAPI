package com.example.githubapi.ui.detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.githubapi.R
import com.example.githubapi.databinding.FragmentFolowBinding
import com.example.githubapi.adapter.UserAdapter
import com.example.githubapi.viewmodel.FollowingViewModel

class FollowingFragment : Fragment(R.layout.fragment_folow) {

    private var _binding: FragmentFolowBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: FollowingViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var username: String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)
        val args = requireArguments()

        username = args.getString(DetailUserActivity.EXTRA_USERNAME).toString()
        _binding = FragmentFolowBinding.bind(view)
        adapter = UserAdapter()

        binding.rvUser.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FollowingFragment.adapter
        }

        viewModel = ViewModelProvider(this).get(FollowingViewModel::class.java)
        viewModel.setListFollowing(username)

        viewModel.getListFollowing().observe(viewLifecycleOwner) { followings ->
            if (followings != null) {
                adapter.setUserList(followings)
                binding.progressBar.visibility = View.GONE
            } else {
                binding.progressBar.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
