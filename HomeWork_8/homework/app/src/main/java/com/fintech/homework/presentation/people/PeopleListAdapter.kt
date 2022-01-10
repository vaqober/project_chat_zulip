package com.fintech.homework.presentation.people

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fintech.homework.R

class PeopleListAdapter(
    private val dataSet: MutableList<PersonItem>,
    private val listener: OnPersonClickListener
) : RecyclerView.Adapter<PeopleListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val listener: OnPersonClickListener) :
        RecyclerView.ViewHolder(view) {
        private val viewPerson = view
        private val userNameView = viewPerson.findViewById<TextView>(R.id.userName)
        private val emailView = viewPerson.findViewById<TextView>(R.id.email)

        fun bind(personItem: PersonItem) {
            userNameView.text = personItem.name
            emailView.text = personItem.email
            viewPerson.setOnClickListener { listener.onPersonClick(personItem) }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // Create a new view, which defines the UI of the list item
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.person_layout, viewGroup, false)
        return ViewHolder(view, listener)
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element
        viewHolder.bind(dataSet[position])
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

    fun update(newList: List<PersonItem>) {
        val channelDiffUtilCallback = PeopleItemDiffUtilCallback(dataSet, newList)
        DiffUtil.calculateDiff(channelDiffUtilCallback)
            .dispatchUpdatesTo(this)
        dataSet.clear()
        dataSet.addAll(newList)
    }

    class PeopleItemDiffUtilCallback(
        private val mOldList: List<PersonItem>,
        private val mNewList: List<PersonItem>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int = mOldList.size

        override fun getNewListSize(): Int = mNewList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            mOldList[oldItemPosition] == mNewList[newItemPosition]

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
            mOldList[oldItemPosition].id == mNewList[newItemPosition].id
                    && mOldList[oldItemPosition].name == mNewList[newItemPosition].name
                    && mOldList[oldItemPosition].email == mNewList[newItemPosition].email
    }
}