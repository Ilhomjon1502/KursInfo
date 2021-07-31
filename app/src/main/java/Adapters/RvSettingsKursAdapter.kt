package Adapters

import Entity.Kurs
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mnsh.hom583kursinfo.databinding.ItemRvSettingsBinding

class RvSettingsKursAdapter(val context: Context?, var rvClick:RvClick):ListAdapter<Kurs, RvSettingsKursAdapter.Vh>(MyDifUtil()) {

    inner class Vh(var itemRv: ItemRvSettingsBinding): RecyclerView.ViewHolder(itemRv.root){
        fun onBind(kurs: Kurs, position: Int){
            val bm = BitmapFactory.decodeFile(kurs.imagePath)
            itemRv.imageRvSettings.setImageBitmap(bm)
            itemRv.txtNameRvSettings.text = kurs.name
            itemRv.cardModul.visibility = View.INVISIBLE
            itemRv.root.setOnClickListener {
                rvClick.itemlick(kurs, position)
            }
            itemRv.edit.setOnClickListener {
                rvClick.editItem(kurs, position)
            }
            itemRv.delete.setOnClickListener {
                rvClick.deleteItem(kurs, position)
            }
        }
    }

    class MyDifUtil: DiffUtil.ItemCallback<Kurs>(){
        override fun areItemsTheSame(oldItem: Kurs, newItem: Kurs): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Kurs, newItem: Kurs): Boolean {
            return oldItem.equals(newItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position), position)
    }

    interface RvClick{
        fun itemlick(kurs: Kurs, position: Int)
        fun deleteItem(kurs: Kurs, position: Int)
        fun editItem(kurs: Kurs, position: Int)
    }

}