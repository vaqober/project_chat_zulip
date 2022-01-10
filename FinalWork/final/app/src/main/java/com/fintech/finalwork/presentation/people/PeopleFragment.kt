package com.fintech.finalwork.presentation.people

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.fintech.finalwork.R
import com.fintech.finalwork.databinding.FragmentPeopleBinding
import com.fintech.finalwork.presentation.MainFragment
import com.fintech.finalwork.presentation.profile.UserFragment

class PeopleFragment : Fragment(), OnPersonClickListener {
    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val peopleList: MutableList<PersonItem> = mutableListOf()
    private val peopleAdapter = PeopleListAdapter(peopleList, this)
    private val viewModelPeople: PeopleViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUi()
        if (savedInstanceState == null) viewModelPeople.searchPeople(PeopleViewModel.INITIAL_QUERY)
    }

    private fun initUi() {
        binding.peopleRecycler
        binding.peopleRecycler.adapter = peopleAdapter
        viewModelPeople.peopleScreenState.observe(viewLifecycleOwner) { processPeopleScreenState(it) }
        binding.searchInput.doAfterTextChanged {
            viewModelPeople.searchPeople(it.toString())
        }
    }

    private fun processPeopleScreenState(it: PeopleScreenState?) {
        when (it) {
            is PeopleScreenState.Result -> {
                val newItems = it.items
                peopleAdapter.update(newItems)
                binding.loadingProgress.isVisible = false
            }
            PeopleScreenState.Loading -> {
                binding.loadingProgress.isVisible = true
            }
            is PeopleScreenState.Error -> {
                Toast.makeText(this.context, it.error.message, Toast.LENGTH_SHORT).show()
                binding.loadingProgress.isVisible = false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onPersonClick(person: PersonItem) {
        requireActivity().supportFragmentManager.beginTransaction()
            .add(R.id.mainFragmentContainer, UserFragment.newInstance(person.id), null)
            .hide(MainFragment())
            .show(UserFragment())
            .addToBackStack("main")
            .commitAllowingStateLoss()
    }
}