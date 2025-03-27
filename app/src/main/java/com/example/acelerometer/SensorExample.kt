package com.example.acelerometer

import android.graphics.Color
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class SensorExample : AppCompatActivity(), SensorEventListener {
    private var sensorManager: SensorManager? = null
    private var isColor = false
    private lateinit var view: View
    private var lastUpdate: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sensor_example)

        view = findViewById(R.id.txtdisp)
        view.setBackgroundColor(Color.GREEN)

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager?
        lastUpdate = System.currentTimeMillis()
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event?.sensor?.type == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event)
        }
    }

    private fun getAccelerometer(event: SensorEvent) {
        val values: FloatArray = event.values

        val x = values[0]
        val y = values[1]
        val z = values[2]

        val accelerationSquareRoot: Float =
            (x * x + y * y + z * z) / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH)

        val actualTime = System.currentTimeMillis()

        Toast.makeText(
            applicationContext,
            "$accelerationSquareRoot --- ${SensorManager.GRAVITY_EARTH}",
            Toast.LENGTH_SHORT
        ).show()

        if (accelerationSquareRoot >= 2) {
            if (actualTime - lastUpdate < 200) {
                return
            }

            lastUpdate = actualTime
            if (isColor) {
                view.setBackgroundColor(Color.YELLOW)
            } else {
                view.setBackgroundColor(Color.RED)
            }
            isColor = !isColor
        }
    }

    override fun onResume() {
        super.onResume()
        sensorManager?.registerListener(
            this,
            sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }


}
