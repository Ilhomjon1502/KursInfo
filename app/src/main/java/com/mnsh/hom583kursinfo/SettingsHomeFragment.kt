package com.mnsh.hom583kursinfo

import Adapters.RvSettingsKursAdapter
import Database.AppDatabase
import Entity.Kurs
import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.github.florent37.runtimepermission.kotlin.askPermission
import com.mnsh.hom583kursinfo.databinding.FragmentSettingsHomeBinding
import com.mnsh.hom583kursinfo.databinding.ItemDialogDeleteBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SettingsHomeFragment : Fragment() {
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

    lateinit var binding:FragmentSettingsHomeBinding
    lateinit var appDatabase: AppDatabase
    lateinit var rvSettingsKursAdapter: RvSettingsKursAdapter
    var absolutePath = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsHomeBinding.inflate(LayoutInflater.from(context))

        appDatabase = AppDatabase.getInstance(context)

        binding.imageAdd.setOnClickListener {
            askPermission(Manifest.permission.READ_EXTERNAL_STORAGE) {
                //all permissions already granted or just granted
                getImageContent.launch("image/*")
            }.onDeclined { e ->
                if (e.hasDenied()) {

                    AlertDialog.Builder(context)
                        .setMessage("Please accept our permissions")
                        .setPositiveButton("yes") { dialog, which ->
                            e.askAgain();
                        } //ask again
                        .setNegativeButton("no") { dialog, which ->
                            dialog.dismiss();
                        }
                        .show();
                }

                if (e.hasForeverDenied()) {
                    //the list of forever denied permissions, user has check 'never ask again'

                    // you need to open setting manually if you really need it
                    e.goToSettings();
                }
            }
        }

        binding.btnAddSave.setOnClickListener {
            val kurs = Kurs()
            kurs.name = binding.edtName.text.toString().trim()
            kurs.imagePath = absolutePath

            val listKurs = ArrayList<Kurs>()
            listKurs.addAll(appDatabase.kursDao().getAllKurs())
            var hasK = true
            for (k in listKurs) {
                if (k.name == kurs.name){
                    hasK = false
                    Toast.makeText(context, "Nomni boshqa tanlang...", Toast.LENGTH_SHORT).show()
                    break
                }
            }
            if (hasK && kurs.name != "" && kurs.imagePath !=""){
                appDatabase.kursDao().addKurs(kurs)
                onResume()
                binding.imageAdd.setImageResource(R.drawable.ic_add_image)
                binding.edtName.text.clear()
                absolutePath = ""
                Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show()
            }else{
                Toast.makeText(context, "Ma'lumtlar yetarli emas", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private val getImageContent =
        registerForActivityResult(ActivityResultContracts.GetContent()){ uri: Uri? ->
            uri ?: return@registerForActivityResult
            binding.imageAdd.setImageURI(uri)
            val inputStream = activity?.contentResolver?.openInputStream(uri)
            val format = SimpleDateFormat("yyMMddhhmmss").format(Date())
            val file = File(activity?.filesDir, "${format}image.jpg")
            val fileOutputStream = FileOutputStream(file)
            inputStream?.copyTo(fileOutputStream)
            inputStream?.close()
            fileOutputStream.close()
            absolutePath = file.absolutePath
            onResume()
        }

    override fun onResume() {
        super.onResume()
        val list = ArrayList<Kurs>()
        list.addAll(appDatabase.kursDao().getAllKurs())
        rvSettingsKursAdapter = RvSettingsKursAdapter(
            context,
            object : RvSettingsKursAdapter.RvClick {
                override fun itemlick(kurs: Kurs, position: Int) {
                    kurs.id = appDatabase.kursDao().getKursById(kurs.name!!)
                    findNavController().navigate(R.id.addModulFrsgment, bundleOf("keyKurs" to kurs))
                }

                override fun deleteItem(kurs: Kurs, position: Int) {

                    val id = appDatabase.kursDao().getKursById(kurs.name!!)
                    kurs.id = id


                    var hasModul = false

                    for (modul in appDatabase.modulDao().getAllModul()) {
                        if (modul.kursId==kurs.id) {
                            hasModul = true
                            break
                        }
                    }

                    if (hasModul) {

                        val dialog = AlertDialog.Builder(context, R.style.NewDialog).create()
                        val itemDialogDeleteBinding =
                            ItemDialogDeleteBinding.inflate(LayoutInflater.from(context))
                        dialog.setView(itemDialogDeleteBinding.root)
                        itemDialogDeleteBinding.txtHa.setOnClickListener {
                            appDatabase.kursDao().deleteKurs(kurs)
                            for (modul in appDatabase.modulDao().getAllModul()) {
                                if (modul.kursId==kurs.id) {
                                    modul.id = appDatabase.modulDao().getModulById(modul.name!!)
                                    appDatabase.modulDao().deleteModul(modul)
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
                        appDatabase.kursDao().deleteKurs(kurs)
                        onResume()
                    }
                }

                override fun editItem(kurs: Kurs, position: Int) {
                    kurs.id = appDatabase.kursDao().getKursById(kurs.name!!)
                    findNavController().navigate(
                        R.id.kursInfoSettings,
                        bundleOf("keyKurs" to kurs)
                    )
                }
            })
        rvSettingsKursAdapter.submitList(list)
        binding.rvSettingsHome.adapter = rvSettingsKursAdapter
        rvSettingsKursAdapter.notifyDataSetChanged()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingsHomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}