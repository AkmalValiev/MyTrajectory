package uz.evkalipt.sevenmodullesson91

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.core.app.ActivityCompat
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import uz.evkalipt.sevenmodullesson91.databinding.ActivityMapsBinding
import uz.evkalipt.sevenmodullesson91.db.MyDatabase
import uz.evkalipt.sevenmodullesson91.entities.Coordinates
import java.util.concurrent.TimeUnit

@Suppress("DEPRECATION")
class MapsActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener,
    GoogleMap.OnPolylineClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var lastLocation: Location
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    lateinit var myDatabase: MyDatabase
    lateinit var coordinateList:ArrayList<LatLng>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        myDatabase = MyDatabase.getInstance(this)
        coordinateList = ArrayList()
        for (allCoordinate in myDatabase.coordinateDao().getAllCoordinates()) {
            coordinateList.add(LatLng(allCoordinate.latitude!!, allCoordinate.longitude!!))
        }

        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.uiSettings.isMyLocationButtonEnabled = true

        setUpMap()
        val polyLine1 = PolylineOptions().clickable(true)
        for (coordinates in coordinateList) {
            polyLine1.add(coordinates)
        }

        googleMap.addPolyline(polyLine1)


        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(-23.684, 133.983), 4f))

        googleMap.setOnPolylineClickListener(this)

        Handler().postDelayed(Runnable {
            val workRequest: WorkRequest = PeriodicWorkRequestBuilder<MyWorkerService>(15, TimeUnit.MINUTES).build()
            WorkManager.getInstance(this).enqueue(workRequest)
        }, 30000)


    }

    private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)
            return
        }
        mMap.isMyLocationEnabled = true
        mMap.setOnMarkerClickListener(this)
        fusedLocationClient.lastLocation.addOnSuccessListener {location->
            if (location!=null){
                lastLocation = location
                val currentLatLong = LatLng(location.latitude, location.longitude)
                placeMarkerOnMap(currentLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLong, 4f))
            }
        }
    }

    private fun placeMarkerOnMap(currentLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(currentLatLong)
        markerOptions.title("$currentLatLong")
        mMap.addMarker(markerOptions)
    }

    override fun onMarkerClick(p0: Marker): Boolean {
        return false
    }

    override fun onPolylineClick(p0: Polyline) {

    }
}