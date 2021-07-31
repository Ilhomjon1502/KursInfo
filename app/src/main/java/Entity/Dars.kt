package Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Dars :Serializable{

    @PrimaryKey(autoGenerate = true)
    var id:Int? = null
    var name:String? = null
    var info:String? = null
    var number:Int? = null
    var modulId:Int? = null
}