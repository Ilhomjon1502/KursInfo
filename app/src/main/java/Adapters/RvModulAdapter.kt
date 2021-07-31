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
import com.mnsh.hom583kursinfo.databinding.ItemKursBinding

class RvModulAdapter(val context: Context?, val kurs: Kurs, var rvClick:RvClick):
    ListAdapter<Modul, RvModulAdapter.Vh>(MyDifUtil()) {

    inner class Vh(var itemRv: ItemKursBinding): RecyclerView.ViewHolder(itemRv.root){
        fun onBind(modul: Modul, position: Int){
            val bm = BitmapFactory.decodeFile(kurs.imagePath)
            itemRv.imageItemKurs.setImageBitmap(bm)
            itemRv.txtModulNameItem.text = modul.name
            itemRv.txtKursName2.text = kurs.name
            itemRv.txtNumberDars.text = modul.number.toString()
            itemRv.root.setOnClickListener {
                rvClick.itemlick(modul, position)
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
        return Vh(ItemKursBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position), position)
    }

    interface RvClick{
        fun itemlick(modul: Modul, position: Int)
    }

}