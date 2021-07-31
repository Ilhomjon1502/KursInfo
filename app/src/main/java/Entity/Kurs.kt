package Entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
class Kurs :Serializable{

    @PrimaryKey(autoGenerate = true)
    var id:Int? = null
    var name:String? = null
    var imagePath:String? = null
}