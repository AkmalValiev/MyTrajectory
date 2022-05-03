package uz.evkalipt.sevenmodullesson91.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.evkalipt.sevenmodullesson91.dao.CoordinateDao
import uz.evkalipt.sevenmodullesson91.entities.Coordinates

@Database(entities = [Coordinates::class], version = 1)
abstract class MyDatabase :RoomDatabase(){

    abstract fun coordinateDao():CoordinateDao

    companion object{
        private var instance:MyDatabase? = null

        fun getInstance(context: Context):MyDatabase{
            if (instance == null){
                instance = Room.databaseBuilder(context, MyDatabase::class.java, "pdp")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return instance!!
        }
    }

}