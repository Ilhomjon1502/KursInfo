package com.mnsh.hom583kursinfo

import Adapters.RvModulAdapter
import Database.AppDatabase
import Entity.Kurs
import Entity.Modul
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.mnsh.hom583kursinfo.databinding.FragmentKursBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class KursFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    lateinit var binding: FragmentKursBinding
    lateinit var appDatabase: AppDatabase
    lateinit var rvModulAdapter: RvModulAdapter
    lateinit var kurs: Kurs

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentKursBinding.inflate(LayoutInflater.from(context))

        appDatabase = AppDatabase.getInstance(context)
        kurs = arguments?.getSerializable("keyKurs") as Kurs
        binding.txtKursName1.text = kurs.name

        val listModul = ArrayList<Modul>()
        appDatabase.modulDao().getAllModul().forEach {
            if (it.kursId == kurs.id) {
                listModul.add(it)
            }
        }
        val modulList2 = ArrayList<Modul>()

        val numberList:ArrayList<Int> = ArrayList()
        listModul.forEach {
            numberList.add(it.number!!)
        }

        numberList.sort()

        for (i in numberList) {
            for (m in listModul){
                if (i == m.number){
                    modulList2.add(m)
                }
            }
        }
        rvModulAdapter = RvModulAdapter(context, kurs, object : RvModulAdapter.RvClick {
            override fun itemlick(modul: Modul, position: Int) {
                modul.id = appDatabase.modulDao().getModulById(modul.name!!)
                findNavController().navigate(R.id.darsListFragment, bundleOf("keyModul" to modul))
            }
        })
        rvModulAdapter.submitList(modulList2)
        binding.rvKurs.adapter = rvModulAdapter

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            KursFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}