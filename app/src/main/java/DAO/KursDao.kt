package DAO

import Entity.Kurs
import androidx.room.*

@Dao
interface KursDao {

    @Query("select * from Kurs")
    fun getAllKurs():List<Kurs>

    @Insert
    fun addKurs(kurs: Kurs)

    @Delete
    fun deleteKurs(kurs: Kurs)

    @Update
    fun editKurs(kurs: Kurs)

    @Query("select * from kurs where name=:name")
    fun getKursById(name:String):Int
}