package DAO

import Entity.Dars
import androidx.room.*

@Dao
interface DarsDao {
    @Query("select * from Dars")
    fun getAllDars():List<Dars>

    @Insert
    fun addDars(dars: Dars)

    @Delete
    fun deleteDars(dars: Dars)

    @Update
    fun editDars(dars: Dars)

    @Query("select * from Dars where name=:name")
    fun getDarsById(name:String):Int
}