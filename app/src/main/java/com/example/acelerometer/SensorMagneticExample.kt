package com.example.acelerometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt

class SensorMagneticExample : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var magneticFieldSensor: Sensor? = null
    private lateinit var compassTextView: TextView
    private var geomagneticvalues = FloatArray(3)
    private var gravityValues = FloatArray(3)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sensor_magnetic_example)

        compassTextView = findViewById(R.id.compassTextView)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        magneticFieldSensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) // FIXED

        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)

        magneticFieldSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            when (it.sensor.type) {
                Sensor.TYPE_ACCELEROMETER -> gravityValues = event.values.clone()
                Sensor.TYPE_MAGNETIC_FIELD -> geomagneticvalues = event.values.clone()
            }

            if (gravityValues.isNotEmpty() && geomagneticvalues.isNotEmpty()) {
                val rotationMatrix = FloatArray(9)
                val orientation = FloatArray(3)

                if (SensorManager.getRotationMatrix(rotationMatrix, null, gravityValues, geomagneticvalues)) {
                    SensorManager.getOrientation(rotationMatrix, orientation)

                    val azimuth = Math.toDegrees(orientation[0].toDouble()).roundToInt()
                    val direction = when (azimuth) {
                        in 0..45 -> "N"
                        in 46..135 -> "E"
                        in 136..225 -> "S"
                        in 226..315 -> "W"
                        else -> "N"
                    }

                    compassTextView.text = "Azimuth: $azimuth°\nDirection: $direction"
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onResume() { // FIXED
        super.onResume()
        magneticFieldSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }
}
