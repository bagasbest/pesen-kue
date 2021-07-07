package com.salwa.salwa.homepage.ui.account.admin

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.salwa.salwa.databinding.ItemListAdminBinding

class AdminListAdapter : RecyclerView.Adapter<AdminListAdapter.ViewHolder>() {

    private val listSeller = ArrayList<AdminListModel>()
    fun setData(items: ArrayList<AdminListModel>) {
        listSeller.clear()
        listSeller.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemListAdminBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listSeller[position])
    }

    override fun getItemCount(): Int = listSeller.size

    inner class ViewHolder(private val binding: ItemListAdminBinding) : RecyclerView.ViewHolder (binding.root){
        fun bind(model: AdminListModel) {
            with(binding) {
                shopName.text = model.name
                Glide.with(itemView.context)
                    .load(model.dp)
                    .into(circleImageView)

                view.setOnClickListener {
                    val intent = Intent(itemView.context, AdminListDetailActivity::class.java)
                    intent.putExtra(AdminListDetailActivity.EXTRA_ADMIN, model)
                    itemView.context.startActivity(intent)
                }
            }
        }

    }


}