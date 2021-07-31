package Adapters

import Entity.Kurs
import Entity.Modul
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mnsh.hom583kursinfo.databinding.ItemRvSettingsBinding

class RvSettingModulAdapter(val context: Context?, val kurs: Kurs, var rvClick:RvClick):
    ListAdapter<Modul, RvSettingModulAdapter.Vh>(MyDifUtil()) {

    inner class Vh(var itemRv: ItemRvSettingsBinding): RecyclerView.ViewHolder(itemRv.root){
        fun onBind(modul: Modul, position: Int){
            val bm = BitmapFactory.decodeFile(kurs.imagePath)
            itemRv.imageRvSettings.setImageBitmap(bm)
            itemRv.txtNameRvSettings.text = modul.name
            itemRv.txtNumberRvSettings.text = modul.number.toString()
            itemRv.root.setOnClickListener {
                rvClick.itemlick(modul, position)
            }
            itemRv.edit.setOnClickListener {
                rvClick.editItem(modul, position)
            }
            itemRv.delete.setOnClickListener {
                rvClick.deleteItem(modul, position)
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position), position)
    }

    interface RvClick{
        fun itemlick(modul: Modul, position: Int)
        fun deleteItem(modul: Modul, position: Int)
        fun editItem(modul: Modul, position: Int)
    }

}