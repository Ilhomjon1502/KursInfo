package Database

import DAO.DarsDao
import DAO.KursDao
import DAO.ModulDao
import Entity.Dars
import Entity.Kurs
import Entity.Modul
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Kurs::class, Modul::class, Dars::class], version = 1)
abstract class AppDatabase : RoomDatabase(){

    abstract fun kursDao():KursDao
    abstract fun modulDao():ModulDao
    abstract fun darsDao():DarsDao

    companion object{
        private var instance:AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context?):AppDatabase{
            if (instance == null){
                instance = Room.databaseBuilder(context!!, AppDatabase::class.java, "markaz_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }
}