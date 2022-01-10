package com.fintech.finalwork.presentation.people

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fintech.finalwork.R
import com.fintech.finalwork.network.GlideProvider
import com.fintech.finalwork.network.GlideProviderImpl
import javax.inject.Inject

class PeopleListAdapter(
    private val dataSet: MutableList<PersonItem>,
    private val listener: OnPersonClickListener
) : RecyclerView.Adapter<PeopleListAdapter.ViewHolder>() {

    class ViewHolder(view: View, private val listener: OnPersonClickListener) :
        RecyclerView.ViewHolder(view) {

        var glide: GlideProvider = GlideProviderImpl()
            @Inject set
        private val viewPerson = view
        private val userNameView = viewPerson.findViewById<TextView>(R.id.userName)
        private val emailView = viewPerson.findViewById<TextView>(R.id.email)
        private val userImage = viewPerson.findViewById<ImageView>(R.id.userImage)

        fun bind(personItem: PersonItem) {
            userNameView.text = personItem.name
            emailView.text = personItem.email
            viewPerson.setOnClickListener { listener.onPersonClick(personItem) }
            glide.loadImage(personItem.avatarUrl, userImage, true)
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.person_layout, viewGroup, false)
        return ViewHolder(view, listener)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position])
    }

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