package com.fintech.homework.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.fintech.homework.R
import com.fintech.homework.adapters.PeopleListAdapter
import com.fintech.homework.data.Person
import com.fintech.homework.databinding.FragmentPeopleBinding

class PeopleFragment : Fragment() {
    private var _binding: FragmentPeopleBinding? = null
    private val binding get() = _binding!!

    private val peopleList: MutableList<Person> = mutableListOf()
    private val recyclerAdapter = PeopleListAdapter(peopleList)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPeopleBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        for (i in 0..4) {
            peopleList.add(
                Person(
                    i,
                    getString(R.string.title_draft),
                    getString(R.string.mail_draft)
                )
            )
        }
        recyclerAdapter.notifyDataSetChanged()
        binding.peopleRecycler.adapter = recyclerAdapter
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}