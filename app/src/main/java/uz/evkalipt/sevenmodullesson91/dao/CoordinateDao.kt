package uz.evkalipt.sevenmodullesson91.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import uz.evkalipt.sevenmodullesson91.entities.Coordinates

@Dao
interface CoordinateDao {

    @Insert
    fun addCoordinate(coordinates: Coordinates)

    @Query("select * from coordinates")
    fun getAllCoordinates():List<Coordinates>


}