package Adapters

import Database.AppDatabase
import Entity.Dars
import Entity.Modul
import android.content.Context
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mnsh.hom583kursinfo.databinding.ItemRvSettingsBinding

class RvSettingsDarsAdapter(val context: Context?, val modul: Modul, var rvClick:RvClick):
    ListAdapter<Dars, RvSettingsDarsAdapter.Vh>(MyDifUtil()) {

    inner class Vh(var itemRv: ItemRvSettingsBinding): RecyclerView.ViewHolder(itemRv.root){
        fun onBind(dars: Dars, position: Int){
            val appDatabase = AppDatabase.getInstance(context)
            var imagePath = ""
            appDatabase.kursDao().getAllKurs().forEach {
                it.id = appDatabase.kursDao().getKursById(it.name!!)
                if (it.id == modul.kursId){
                    imagePath = it.imagePath!!
                }
            }
            val bm = BitmapFactory.decodeFile(imagePath)
            itemRv.imageRvSettings.setImageBitmap(bm)
            itemRv.txtNameRvSettings.text = dars.name
            itemRv.txtNumberRvSettings.text = dars.number.toString()
            itemRv.root.setOnClickListener {
                rvClick.itemlick(dars, position)
            }
            itemRv.edit.setOnClickListener {
                rvClick.editItem(dars, position)
            }
            itemRv.delete.setOnClickListener {
                rvClick.deleteItem(dars, position)
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
        return Vh(ItemRvSettingsBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position), position)
    }

    interface RvClick{
        fun itemlick(dars: Dars, position: Int)
        fun deleteItem(dars: Dars, position: Int)
        fun editItem(dars: Dars, position: Int)
    }

}