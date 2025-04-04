package com.example.acelerometer

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class StepCountExample : AppCompatActivity(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private var running = false
    private var totalSteps = 0f
    private var previousTotalSteps = 0f
    private lateinit var tvStepsTaken: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        tvStepsTaken = findViewById(R.id.tv_stepsTaken)

        loadData()
        resetSteps()

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        if (stepSensor == null) {
            Toast.makeText(this, "No sensor detected on this device", Toast.LENGTH_LONG).show()
        } else {
            sensorManager.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this) // Unregister listener to save battery
    }

    override fun onSensorChanged(event: SensorEvent?) {
        val steps = event?.values?.get(0) ?: return
        if (running) {
            totalSteps = steps
            val currentSteps = (totalSteps - previousTotalSteps).toInt()
            tvStepsTaken.text = currentSteps.toString()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    private fun resetSteps() {
        tvStepsTaken.setOnClickListener {
            Toast.makeText(this, "Long tap to reset steps", Toast.LENGTH_SHORT).show()
        }
        tvStepsTaken.setOnLongClickListener {
            previousTotalSteps = totalSteps
            tvStepsTaken.text = "0"
            saveData()
            true
        }
    }

    private fun saveData() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putFloat("key1", previousTotalSteps) // Corrected key name
            apply()
        }
    }

    private fun loadData() {
        val sharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        previousTotalSteps = sharedPreferences.getFloat("key1", 0f) // Corrected key name
        Log.d("MainActivity", "Loaded steps: $previousTotalSteps")
    }
}
