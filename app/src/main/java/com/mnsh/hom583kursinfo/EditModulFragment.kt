package com.mnsh.hom583kursinfo

import Database.AppDatabase
import Entity.Modul
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.mnsh.hom583kursinfo.databinding.FragmentEditModulBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditModulFragment : Fragment() {
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

    lateinit var binding:FragmentEditModulBinding
    lateinit var appDatabase: AppDatabase
    lateinit var modul: Modul

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditModulBinding.inflate(LayoutInflater.from(context))

        appDatabase = AppDatabase.getInstance(context)

        modul = arguments?.getSerializable("keyModul") as Modul
        binding.edtName.setText(modul.name)
        binding.txtName.text = modul.name
        binding.edtNumber.setText(modul.number.toString())

        binding.btnAddSave.setOnClickListener {
            val modul2 = modul
            modul2.name = binding.edtName.text.toString().trim()
            modul2.number = binding.edtNumber.text.toString().toInt()

            var has = true
            for (m in appDatabase.modulDao().getAllModul()) {
                if (m.kursId == modul2.kursId){
                    if (modul2.number == m.number && modul.number != m.number){
                        has = false
                    }
                }
                    if (m.name == modul2.name && m.name != modul.name){
                    has = false
                }
            }
            if (has && modul2.name!="" && modul2.number!=null){
                appDatabase.modulDao().editModul(modul2)
                Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }else{
                Toast.makeText(context, "Xatolik, kiritilgan ma'lumtlarni teksiring. Bunday nomli modul avva yaratilgan bo'lishi mumkin", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditModulFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}