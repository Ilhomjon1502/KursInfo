package Adapters

import Entity.Modul
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mnsh.hom583kursinfo.databinding.ItemRvHomeItemBinding

class RvHomeItemAdapter(val context: Context?, val rvItemClick:RvItemClick)
    :ListAdapter<Modul, RvHomeItemAdapter.Vh>(MyDifUtil()){

    inner class Vh(var itemRv:ItemRvHomeItemBinding):RecyclerView.ViewHolder(itemRv.root){
        fun onBind(modul: Modul, position: Int){
            itemRv.txtModulName.text = modul.name
            itemRv.itemCard.setOnClickListener {
                rvItemClick.itemClick(modul)
            }
        }
    }

    class MyDifUtil: DiffUtil.ItemCallback<Modul>(){
        override fun areItemsTheSame(oldItem: Modul, newItem: Modul): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Modul, newItem: Modul): Boolean {
            return oldItem.equals(newItem)
        }

    }
    interface RvItemClick{
        fun itemClick(modul: Modul)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvHomeItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position), position)
    }
}