package com.salwa.salwa.homepage.ui.account

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.salwa.salwa.databinding.ItemSoldBinding

class ShopAdapter : RecyclerView.Adapter<ShopAdapter.ViewHolder>() {

    private val listSoldProduct = ArrayList<ShopModel>()
    fun setData(items: ArrayList<ShopModel>) {
        listSoldProduct.clear()
        listSoldProduct.addAll(items)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSoldBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listSoldProduct[position])
    }

    override fun getItemCount(): Int = listSoldProduct.size

    inner class ViewHolder(private val binding: ItemSoldBinding) : RecyclerView.ViewHolder (binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(model: ShopModel) {
            with(binding) {
                nameAndQty.text = model.name + " sold " + model.quantity + " product"
            }
        }

    }
}