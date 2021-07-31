package DAO

import Entity.Modul
import androidx.room.*

@Dao
interface ModulDao {
    @Query("select * from Modul")
    fun getAllModul():List<Modul>

    @Insert
    fun addModul(modul: Modul)

    @Delete
    fun deleteModul(modul: Modul)

    @Update
    fun editModul(modul: Modul)

    @Query("select * from modul where name=:name")
    fun getModulById(name:String):Int
}