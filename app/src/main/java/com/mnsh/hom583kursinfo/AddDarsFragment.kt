package com.mnsh.hom583kursinfo

import Adapters.RvSettingsDarsAdapter
import Database.AppDatabase
import Entity.Dars
import Entity.Modul
import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.mnsh.hom583kursinfo.databinding.FragmentAddDarsBinding
import com.mnsh.hom583kursinfo.databinding.ItemDialogDeleteBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class AddDarsFragment : Fragment() {
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

    lateinit var binding:FragmentAddDarsBinding
    lateinit var modul:Modul
    lateinit var appDatabase: AppDatabase
    lateinit var rvSettingsDarsAdapter: RvSettingsDarsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddDarsBinding.inflate(LayoutInflater.from(context))

        appDatabase = AppDatabase.getInstance(context)
        modul = arguments?.getSerializable("keyModul") as Modul
        binding.txtTitle.text = modul.name

        binding.btnAddSave.setOnClickListener {
            val dars = Dars()
            dars.modulId = modul.id
            dars.name = binding.edtName.text.toString().trim()
            dars.info = binding.edtInfo.text.toString().trim()
            if (binding.edtNumber.text.isNotEmpty())
            dars.number = binding.edtNumber.text.toString().toInt()

            var has = true
            for (dar in appDatabase.darsDao().getAllDars()) {
                if (dar.name == dars.name){
                    has = false
                    break
                }
                if (dar.modulId == modul.id && dar.number == dars.number){
                    has = false
                    break
                }
            }
            if (has && dars.name!="" && dars.info!="" && dars.number!=null){
                appDatabase.darsDao().addDars(dars)
                onResume()
                binding.edtName.text.clear()
                binding.edtInfo.text.clear()
                binding.edtNumber.text.clear()
                Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Xatolik, ma'lumotlar to'liq va to'g'ri kiritilishi va ushbu nomldagi dars avval yaratilmagan bo'lisi kerak", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        rvSettingsDarsAdapter = RvSettingsDarsAdapter(context, modul, object : RvSettingsDarsAdapter.RvClick{
            override fun itemlick(dars: Dars, position: Int) {

            }

            override fun deleteItem(dars: Dars, position: Int) {
                val dialog = AlertDialog.Builder(context, R.style.NewDialog).create()
                val itemDialogDeleteBinding =
                    ItemDialogDeleteBinding.inflate(LayoutInflater.from(context))
                itemDialogDeleteBinding.txtMassage.text = "Dars o’chishiga rozimisiz?\n"
                dialog.setView(itemDialogDeleteBinding.root)
                itemDialogDeleteBinding.txtHa.setOnClickListener {
                    val id = appDatabase.darsDao().getDarsById(dars.name!!)
                    dars.id = id
                    appDatabase.darsDao().deleteDars(dars)

                    onResume()
                    dialog.cancel()
                    Toast.makeText(context, "$id o'chirildi", Toast.LENGTH_SHORT).show()
                }
                itemDialogDeleteBinding.txtYoq.setOnClickListener {
                    dialog.cancel()
                }
                dialog.show()
            }

            override fun editItem(dars: Dars, position: Int) {
                dars.id = appDatabase.darsDao().getDarsById(dars.name!!)
                findNavController().navigate(R.id.editDarsFragment, bundleOf("keyDars" to dars))
            }
        })
        val darsList = ArrayList<Dars>()
        appDatabase.darsDao().getAllDars().forEach {
            if (it.modulId == modul.id){
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
        rvSettingsDarsAdapter.submitList(darsList2)
        binding.rvSettingsHome.adapter = rvSettingsDarsAdapter
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddDarsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddDarsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}