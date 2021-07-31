package Adapters

import Entity.Dars
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mnsh.hom583kursinfo.databinding.ItemDarslistRvBinding

class RvDarsListAdapter(val context: Context?, val rvHomeClick: RvHomeClick):
    ListAdapter<Dars, RvDarsListAdapter.Vh>(MyDifUtil()) {

    inner class Vh(var itemRv: ItemDarslistRvBinding): RecyclerView.ViewHolder(itemRv.root){
        fun onBind(dars: Dars, position: Int){
           itemRv.txtNumberDarsList.text = dars.number.toString()
            itemRv.cardDars.setOnClickListener {
                rvHomeClick.itemClick(dars, position)
            }
        }
    }

    class MyDifUtil: DiffUtil.ItemCallback<Dars>(){
        override fun areItemsTheSame(oldItem: Dars, newItem: Dars): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Dars, newItem: Dars): Boolean {
            return oldItem.equals(newItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemDarslistRvBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position), position)
    }

    interface RvHomeClick{
        fun itemClick(dars: Dars, position: Int)
    }
}