package com.example.acelerometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AllSensor : AppCompatActivity() {
    private lateinit var sn: SensorManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_all_sennsor)

        sn = getSystemService(Context.SENSOR_SERVICE) as SensorManager // ✅ Removed extra closing parenthesis

        val dv: List<Sensor> = sn.getSensorList(Sensor.TYPE_ALL)
        Log.v("SENSOR_LOG", "Total sensors: ${dv.size}") // ✅ Corrected Log.v() usage

        dv.forEach {
            Toast.makeText(applicationContext, it.toString(), Toast.LENGTH_SHORT).show()
            Log.v("SENSOR_LOG", "Sensor name: $it") // ✅ Proper string interpolation
        }

        if (sn.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE) != null) {
            Log.v("SENSOR_LOG", "Success: Ambient Temperature Sensor Found") // ✅ Clearer log messages
        } else {
            Log.v("SENSOR_LOG", "Fail: No Ambient Temperature Sensor Found")
        }
    }
}
