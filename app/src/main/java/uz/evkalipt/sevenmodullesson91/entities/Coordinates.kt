package uz.evkalipt.sevenmodullesson91.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Coordinates {

    @PrimaryKey(autoGenerate = true)
    var id:Int? = null
    var longitude:Double? = null
    var latitude:Double? = null

    constructor(longitude: Double?, latitude: Double?) {
        this.longitude = longitude
        this.latitude = latitude
    }

    constructor()


}