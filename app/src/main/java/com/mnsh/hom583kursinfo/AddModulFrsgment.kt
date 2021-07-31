package com.mnsh.hom583kursinfo

import Adapters.RvSettingModulAdapter
import Database.AppDatabase
import Entity.Kurs
import Entity.Modul
import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.mnsh.hom583kursinfo.databinding.FragmentAddModulFrsgmentBinding
import com.mnsh.hom583kursinfo.databinding.ItemDialogDeleteBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddModulFrsgment : Fragment() {
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

    lateinit var binding: FragmentAddModulFrsgmentBinding
    lateinit var kurs: Kurs
    lateinit var appDatabase: AppDatabase
    lateinit var rvSettingsModulAdapter: RvSettingModulAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddModulFrsgmentBinding.inflate(LayoutInflater.from(context))

        kurs = arguments?.getSerializable("keyKurs") as Kurs
        appDatabase = AppDatabase.getInstance(context)

        binding.txtTitle.text = kurs.name

        binding.btnAddSave.setOnClickListener {
            val modul = Modul()
            modul.kursId = kurs.id
            modul.name = binding.edtNameM.text.toString().trim()
            val number: String = binding.edtNimber.text.toString()
            Log.d("has", number)
            var n = 0
            if (binding.edtNimber.text.isNotEmpty()) {
                n = number.toInt()
            }
            modul.number = n
            val listModul = ArrayList<Modul>()
            listModul.addAll(appDatabase.modulDao().getAllModul())
            var hasNot = true
            for (m in listModul) {
                if (m.kursId == kurs.id || m.name == modul.name) {
                    if (m.name == modul.name || m.number == modul.number) {
                        hasNot = false
                        break
                    }
                }
            }
            if (hasNot && modul.name != "" && modul.number != null) {
                appDatabase.modulDao().addModul(modul)
                onResume()
                binding.edtNameM.text.clear()
                binding.edtNimber.text.clear()
                Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(context, "Xatolik ma'lumot yetarli emas", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
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

        rvSettingsModulAdapter = RvSettingModulAdapter(
            context,
            kurs,
            object : RvSettingModulAdapter.RvClick {
                override fun itemlick(modul: Modul, position: Int) {
                    modul.id = appDatabase.modulDao().getModulById(modul.name!!)
                    findNavController().navigate(
                        R.id.addDarsFragment,
                        bundleOf("keyModul" to modul)
                    )
                }

                override fun deleteItem(modul: Modul, position: Int) {
                    val id = appDatabase.modulDao().getModulById(modul.name!!)
                    modul.id = id

                    var hasDars = false
                    for (dars in appDatabase.darsDao().getAllDars()) {
                        if (dars.modulId == modul.id) {
                            hasDars = true
                            break
                        }
                    }

                    if (hasDars) {
                        val dialog = AlertDialog.Builder(context, R.style.NewDialog).create()
                        val itemDialogDeleteBinding =
                            ItemDialogDeleteBinding.inflate(LayoutInflater.from(context))
                        itemDialogDeleteBinding.txtMassage.text = "Bu modul ichida darslar kiritilgan. Darslar bilan birgalikda o’chib ketishiga rozimisiz?"
                        dialog.setView(itemDialogDeleteBinding.root)
                        itemDialogDeleteBinding.txtHa.setOnClickListener {
                            appDatabase.modulDao().deleteModul(modul)
                            for (dars in appDatabase.darsDao().getAllDars()) {
                                if (dars.modulId == modul.id) {
                                    dars.id = appDatabase.darsDao().getDarsById(dars.name!!)
                                    appDatabase.darsDao().deleteDars(dars)
                                }
                            }

                            onResume()
                            dialog.cancel()
                            Toast.makeText(context, "$id o'chirildi", Toast.LENGTH_SHORT).show()
                        }
                        itemDialogDeleteBinding.txtYoq.setOnClickListener {
                            dialog.cancel()
                        }
                        dialog.show()
                    }else{
                        appDatabase.modulDao().deleteModul(modul)
                        onResume()
                    }
                }

                override fun editItem(modul: Modul, position: Int) {
                    modul.id = appDatabase.modulDao().getModulById(modul.name!!)
                    findNavController().navigate(
                        R.id.editModulFragment,
                        bundleOf("keyModul" to modul)
                    )
                }
            })
        rvSettingsModulAdapter.submitList(modulList2)
        binding.rvSettingsHome.adapter = rvSettingsModulAdapter
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddModulFrsgment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}