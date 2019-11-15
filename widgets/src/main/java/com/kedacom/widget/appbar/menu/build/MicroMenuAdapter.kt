package com.kedacom.widget.appbar.menu.build

import android.content.Context
import android.support.annotation.RestrictTo
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.AppCompatTextView
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.PopupWindow
import com.kedacom.widget.R
import com.kedacom.widget.appbar.BiswitchAppBar

/**
 * 小程序风格菜单适配
 */
@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class MicroMenuAdapter(private val context: Context, private val data: List<MenuItem>,
                                private val cascadeListener: BiswitchAppBar.OnMenuItemClickListener?,
                                private val popup: PopupWindow) : RecyclerView.Adapter<MicroViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MicroViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.menu_popup_micro_appbar, parent, false)
        return MicroViewHolder(view)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: MicroViewHolder, position: Int) {
        val itemData = data[position]
        holder.imageIcon.setImageDrawable(itemData.icon)
        holder.textTitle.text = itemData.title
        holder.itemView.setOnClickListener {
            popup.dismiss()
            cascadeListener?.onMenuItemClick(itemData)
        }
    }
}

@RestrictTo(RestrictTo.Scope.LIBRARY)
internal class MicroViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var textTitle: AppCompatTextView = itemView.findViewById(R.id.pop_menu_title)
    var imageIcon: AppCompatImageView = itemView.findViewById(R.id.pop_menu_icon)
}