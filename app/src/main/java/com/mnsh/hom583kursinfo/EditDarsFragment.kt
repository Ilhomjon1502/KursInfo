package com.mnsh.hom583kursinfo

import Database.AppDatabase
import Entity.Dars
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.mnsh.hom583kursinfo.databinding.FragmentEditDarsBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditDarsFragment : Fragment() {
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

    lateinit var binding:FragmentEditDarsBinding
    lateinit var appDatabase: AppDatabase
    lateinit var dars: Dars

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentEditDarsBinding.inflate(LayoutInflater.from(context))

        appDatabase = AppDatabase.getInstance(context)
        dars = arguments?.getSerializable("keyDars") as Dars

        binding.txtNumberDars.text = dars.name+" - dars"
        binding.edtName.setText(dars.name)
        binding.edtInfo.setText(dars.info)
        binding.edtNumber.setText(dars.number.toString())

        binding.btnAddSave.setOnClickListener {
            dars.name = binding.edtName.text.toString()
            if (binding.edtNumber.text.isNotEmpty())
            dars.number = binding.edtNumber.text.toString().toInt()
            dars.info = binding.edtInfo.text.toString()

            var has = true
            for (d in appDatabase.darsDao().getAllDars()) {
                if (d.name == dars.name){
                    has = false
                    break
                }
            }
            if (has && dars.name!="" && dars.info!=""){
                appDatabase.darsDao().editDars(dars)
                findNavController().popBackStack()
                Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Xatolik qayta urinib ko'ring", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditDarsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}