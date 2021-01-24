package com.priyam.squareboatapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.priyam.squareboatapplication.R
import com.priyam.squareboatapplication.`interface`.IconsListRecyclerClickListener
import com.priyam.squareboatapplication.model.Icon

class IconListAdapter(
    val iconsList: ArrayList<Icon>,
    val iconClickListener: IconsListRecyclerClickListener
): RecyclerView.Adapter<IconListAdapter.ViewHolder>() {

     lateinit var context: Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
        val v = LayoutInflater.from(parent.context).inflate(
            R.layout.icon_recycler_view_item,
            parent,
            false
        )
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(iconsList[position], iconClickListener)
    }

    override fun getItemCount(): Int {
        return iconsList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItems(icon: Icon, iconClickListener: IconsListRecyclerClickListener){
            var iconName = itemView.findViewById(R.id.iconName) as TextView
            var previewImage = itemView.findViewById(R.id.imageView) as ImageView
            var downloadImage = itemView.findViewById(R.id.downloadImage)as ImageView
            var tagImage = itemView.findViewById(R.id.tagImageView) as ImageView
            var freeTagImage = itemView.findViewById(R.id.freeTagImageView) as ImageView
            var price  = itemView.findViewById(R.id.iconPrice)as TextView
            if (icon.is_premium == true) {

                tagImage.visibility = View.VISIBLE
                price.visibility = View.VISIBLE
                freeTagImage.visibility =View.GONE
                price.text=
                    icon.prices?.get(0)?.currency.toString() + " " + icon.prices?.get(0)
                        ?.price

            } else {
                freeTagImage.visibility =View.VISIBLE
                tagImage.visibility = View.GONE
                price.visibility = View.GONE
            }

            var context = itemView.context
            Glide.with(context).load(
                icon.raster_Sizes?.get(icon.raster_Sizes!!.size - 1)?.formats?.get(0)
                    ?.preview_url
            )
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.error)
                .into(previewImage);

            iconName.text = icon.icon_id
            downloadImage.setOnClickListener { iconClickListener.onDetailButtonClick(adapterPosition) }
        }
    }
}