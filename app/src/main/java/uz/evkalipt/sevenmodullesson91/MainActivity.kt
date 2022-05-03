package uz.evkalipt.sevenmodullesson91

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.squareup.picasso.Picasso
import uz.evkalipt.sevenmodullesson91.databinding.ActivityMainBinding
import uz.evkalipt.sevenmodullesson91.db.MyDatabase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var myDatabase: MyDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.start.setOnClickListener {
            var intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        Picasso.with(this).load("http://openweathermap.org/img/w/01n.png").into(binding.image)

        myDatabase = MyDatabase.getInstance(this)
        val allCoordinates = myDatabase.coordinateDao().getAllCoordinates()
        for (allCoordinate in allCoordinates) {
            binding.textview.text = "${binding.textview.text}\n${allCoordinate.latitude} - ${allCoordinate.longitude}"
        }

    }
}