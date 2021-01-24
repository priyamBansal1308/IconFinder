package com.priyam.squareboatapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.priyam.squareboatapplication.R
import com.priyam.squareboatapplication.`interface`.IconsSetListRecyclerClickListener
import com.priyam.squareboatapplication.model.Iconset

class IconSetAdapter(val iconsSetList: ArrayList<Iconset>, val iconSetClickLister: IconsSetListRecyclerClickListener ): RecyclerView.Adapter<IconSetAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.recycler_view_item, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(iconsSetList[position], iconSetClickLister)
    }

    override fun getItemCount(): Int {
        return iconsSetList.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        fun bindItems(icon: Iconset, iconSetClickLister: IconsSetListRecyclerClickListener){
            var iconName = itemView.findViewById(R.id.info_text) as TextView

            var viewImage = itemView.findViewById(R.id.viewImage)as TextView
            var tagImage = itemView.findViewById(R.id.tagImageView) as ImageView
            var freeTagImage = itemView.findViewById(R.id.freeTagImageView) as ImageView
            var count  = itemView.findViewById(R.id.iconCount)as TextView
            if(icon.is_premium == true){
                    tagImage.visibility = View.VISIBLE
                freeTagImage.visibility =View.GONE
            }else{freeTagImage.visibility = View.VISIBLE
            tagImage.visibility = View.GONE}
            count.text = icon.icons_count
            iconName.text = icon.name
            viewImage.setOnClickListener { iconSetClickLister.onDetailButtonClick(adapterPosition) }
        }
    }
}