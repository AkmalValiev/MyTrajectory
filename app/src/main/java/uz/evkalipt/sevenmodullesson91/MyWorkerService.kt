package uz.evkalipt.sevenmodullesson91

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import androidx.core.app.ActivityCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import uz.evkalipt.sevenmodullesson91.db.MyDatabase
import uz.evkalipt.sevenmodullesson91.entities.Coordinates

class MyWorkerService(var context: Context, workerParameters: WorkerParameters):Worker(context, workerParameters) {
    lateinit var myDatabase: MyDatabase
    lateinit var lastLocation: Location
    lateinit var fusedLocationClient:FusedLocationProviderClient
    override fun doWork(): Result {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        myDatabase = MyDatabase.getInstance(context)

        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

        }
        fusedLocationClient.lastLocation.addOnSuccessListener { location->
            if (location!=null){
                lastLocation = location
                    myDatabase.coordinateDao()
                        .addCoordinate(Coordinates(location.longitude, location.latitude))
            }
        }

        return Result.success()
    }
}