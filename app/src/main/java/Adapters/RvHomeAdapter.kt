package Adapters

import Database.AppDatabase
import Entity.Kurs
import Entity.Modul
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mnsh.hom583kursinfo.databinding.ItemRvHomeBinding

class RvHomeAdapter(val context: Context?, val rvItemClick: RvHomeItemAdapter.RvItemClick, val rvHomeClick: RvHomeClick):ListAdapter<Kurs, RvHomeAdapter.Vh>(MyDifUtil()) {

    inner class Vh(var itemRv:ItemRvHomeBinding): RecyclerView.ViewHolder(itemRv.root){
        fun onBind(kurs: Kurs, position: Int){
            itemRv.txtKurrsName.text = kurs.name
            itemRv.allInfo.setOnClickListener {
                rvHomeClick.itemClick(kurs, position)
            }
            val appDatabase = AppDatabase.getInstance(context)

            val modulList = ArrayList<Modul>()
            for (m in appDatabase.modulDao().getAllModul()) {
                if (m.kursId == kurs.id) {
                    modulList.add(m)
                }
            }
            val modulList2 = ArrayList<Modul>()

            val numberList:ArrayList<Int> = ArrayList()
            modulList.forEach {
                numberList.add(it.number!!)
            }

            numberList.sort()

            for (i in numberList) {
                for (m in modulList){
                    if (i == m.number){
                        modulList2.add(m)
                    }
                }
            }
            val rvHomeItemAdapter = RvHomeItemAdapter(context, rvItemClick)
            rvHomeItemAdapter.submitList(modulList2)
            itemRv.rvHomeItem.adapter = rvHomeItemAdapter
        }
    }

    class MyDifUtil:DiffUtil.ItemCallback<Kurs>(){
        override fun areItemsTheSame(oldItem: Kurs, newItem: Kurs): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Kurs, newItem: Kurs): Boolean {
            return oldItem.equals(newItem)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Vh {
        return Vh(ItemRvHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Vh, position: Int) {
        holder.onBind(getItem(position), position)
    }

    interface RvHomeClick{
        fun itemClick(kurs: Kurs, position: Int)
    }
}