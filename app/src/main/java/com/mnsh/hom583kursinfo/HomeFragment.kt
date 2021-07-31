package com.mnsh.hom583kursinfo

import Adapters.RvHomeAdapter
import Adapters.RvHomeItemAdapter
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
import com.mnsh.hom583kursinfo.databinding.FragmentHomeBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class HomeFragment : Fragment() {
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

    lateinit var binding:FragmentHomeBinding
    lateinit var rvHomeAdapter: RvHomeAdapter
    lateinit var appDatabase: AppDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(LayoutInflater.from(context))

        appDatabase = AppDatabase.getInstance(context)

        binding.btnSetings.setOnClickListener {
            findNavController().navigate(R.id.settingsHomeFragment)
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        rvHomeAdapter = RvHomeAdapter(context, object : RvHomeItemAdapter.RvItemClick{
            override fun itemClick(modul: Modul) {
                modul.id = appDatabase.modulDao().getModulById(modul.name!!)
                findNavController().navigate(R.id.darsListFragment, bundleOf("keyModul" to modul))
            }
        }, object :RvHomeAdapter.RvHomeClick{
            override fun itemClick(kurs: Kurs, position: Int) {
                kurs.id = appDatabase.kursDao().getKursById(kurs.name!!)
                findNavController().navigate(R.id.kursFragment, bundleOf("keyKurs" to kurs))
            }
        })
        var listKurs = ArrayList<Kurs>()
        listKurs.addAll(appDatabase.kursDao().getAllKurs())
        rvHomeAdapter.submitList(listKurs)
        binding.rvHome.adapter = rvHomeAdapter

        appDatabase.kursDao().getAllKurs()
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : Consumer<List<Kurs>>{
//                override fun accept(t: List<Kurs>?) {
//                    rvHomeAdapter.submitList(t)
////                    binding.progressbar.visibility = View.GONE
//                }
//            }, object : Consumer<Throwable> {
//                override fun accept(t: Throwable?) {
//
//                }
//            }, object : Action{
//                override fun run() {
//                    Toast.makeText(context, "Success", Toast.LENGTH_SHORT).show()
//                }
//            })
    }

}