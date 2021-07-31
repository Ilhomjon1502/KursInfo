package com.mnsh.hom583kursinfo

import Adapters.RvDarsListAdapter
import Database.AppDatabase
import Entity.Dars
import Entity.Modul
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.mnsh.hom583kursinfo.databinding.FragmentDarsListBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DarsListFragment : Fragment() {
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

    lateinit var binding:FragmentDarsListBinding
    lateinit var appDatabase: AppDatabase
    lateinit var modul:Modul
    lateinit var rvDasListAdapter: RvDarsListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDarsListBinding.inflate(LayoutInflater.from(context))

        modul = arguments?.getSerializable("keyModul") as Modul
        binding.txtNameDarslist.text = modul.name

        appDatabase = AppDatabase.getInstance(context)
        val darsList = ArrayList<Dars>()
        appDatabase.darsDao().getAllDars().forEach {
            if (modul.id == it.modulId){
                darsList.add(it)
            }
        }
        val darsList2 = ArrayList<Dars>()

        val numberList:ArrayList<Int> = ArrayList()
        darsList.forEach {
            numberList.add(it.number!!)
        }

        numberList.sort()

        for (i in numberList) {
            for (m in darsList){
                if (i == m.number){
                    darsList2.add(m)
                }
            }
        }

        rvDasListAdapter = RvDarsListAdapter(context, object : RvDarsListAdapter.RvHomeClick{
            override fun itemClick(dars: Dars, position: Int) {
                findNavController().navigate(R.id.darsInfoFragment, bundleOf("keyDars" to dars))
            }
        })
        rvDasListAdapter.submitList(darsList2)
        binding.rvDarsList.adapter = rvDasListAdapter

        return binding.root
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DarsListFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DarsListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}