package com.greentea.gitusers2.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.greentea.gitusers2.databinding.ItemRowUserBinding
import com.greentea.gitusers2.services.models.ListUsersResponseItem

class ListUsersAdapter(
    val context: Context
    ) : RecyclerView.Adapter<ListUsersAdapter.ListUsersViewHolder>() {

    private val differCallback = object: DiffUtil.ItemCallback<ListUsersResponseItem>(){
        override fun areItemsTheSame(
            oldItem: ListUsersResponseItem,
            newItem: ListUsersResponseItem
        ): Boolean {
            //COMPARE ID BECAUSE IT'S UNIQUE
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(
            oldItem: ListUsersResponseItem,
            newItem: ListUsersResponseItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differAsync = AsyncListDiffer(this, differCallback)
    private var onItemClickListener: ((ListUsersResponseItem) -> Unit)? = null

    inner class ListUsersViewHolder(var bindingListUsersAdapter: ItemRowUserBinding): RecyclerView
    .ViewHolder(bindingListUsersAdapter.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListUsersViewHolder {
        val bindingListUsersAdapter = ItemRowUserBinding
            .inflate(LayoutInflater.from(parent.context),
                parent,
                false)
        return ListUsersViewHolder(bindingListUsersAdapter)
    }

    override fun onBindViewHolder(holder: ListUsersViewHolder, position: Int) {
        val user = differAsync.currentList[position]

        holder.itemView.apply {
            Glide.with(this)
                .load(user.avatar_url)
                .into(holder.bindingListUsersAdapter.imgItemPhoto)
            holder.bindingListUsersAdapter.tvItemUsername.text = user.login
            holder.bindingListUsersAdapter.tvItemGithubLink.text = user.html_url
            holder.bindingListUsersAdapter.tvItemTypeUser.text = user.type

            setOnClickListener {
                onItemClickListener?.let {
                    it(user) }
            }
        }
    }

    override fun getItemCount(): Int {
        return differAsync.currentList.size
    }

    fun setOnItemClickListener(listener: (ListUsersResponseItem) -> Unit){
        onItemClickListener = listener
    }

}