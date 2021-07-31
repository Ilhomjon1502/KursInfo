package com.mnsh.hom583kursinfo

import Database.AppDatabase
import Entity.Kurs
import android.annotation.SuppressLint
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.navigation.fragment.findNavController
import com.mnsh.hom583kursinfo.databinding.FragmentKursInfoSettingsBinding
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class KursInfoSettings : Fragment() {
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

    lateinit var binding:FragmentKursInfoSettingsBinding
    lateinit var kurs: Kurs
    lateinit var appDatabase: AppDatabase
    var absolutePath = ""

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentKursInfoSettingsBinding.inflate(LayoutInflater.from(context))

        kurs = arguments?.getSerializable("keyKurs") as Kurs
        appDatabase = AppDatabase.getInstance(context)

        val bm = BitmapFactory.decodeFile(kurs.imagePath)
        binding.imageAdd.setImageBitmap(bm)
        binding.edtName.setText(kurs.name)
        absolutePath = kurs.imagePath!!

        binding.imageAdd.setOnClickListener {
            getImageContent.launch("image/*")
        }

        binding.btnAddSave.setOnClickListener {
            kurs.name = binding.edtName.text.toString().trim()
            kurs.imagePath = absolutePath

            val listKurs = ArrayList<Kurs>()
            listKurs.addAll(appDatabase.kursDao().getAllKurs())

            if (kurs.name != "" && kurs.imagePath !=""){
                appDatabase.kursDao().editKurs(kurs)
                Toast.makeText(context, "Save", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
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
            Toast.makeText(context, "$absolutePath", Toast.LENGTH_SHORT).show()
        }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                KursInfoSettings().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}