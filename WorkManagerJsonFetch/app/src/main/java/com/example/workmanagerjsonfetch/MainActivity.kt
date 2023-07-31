package com.example.workmanagerjsonfetch


import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fetchButton = findViewById<Button>(R.id.fetchButton)
        val jsonTextView = findViewById<TextView>(R.id.jsonTextView)

        fetchButton.setOnClickListener {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.UNMETERED)
                .build()

            val fetchJsonWorkRequest =
                OneTimeWorkRequest.Builder(FetchJsonWorker::class.java)
                    .setConstraints(constraints)
                    .setInitialDelay(5, TimeUnit.SECONDS)
                    .build()

            // Enqueue the FetchJsonWorker
            WorkManager.getInstance(this).enqueue(fetchJsonWorkRequest)

            // Observe the work status and retrieve the fetched JSON data
            val workManager = WorkManager.getInstance(this)
            workManager.getWorkInfoByIdLiveData(fetchJsonWorkRequest.id)
                .observe(this, Observer { workInfo ->
                    if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                        // Retrieve the fetched JSON data from the output data
                        val jsonData = workInfo.outputData.getString("jsonData")

                        if (!jsonData.isNullOrEmpty()) {
                            // Display the fetched JSON in the TextView
                            jsonTextView.text = jsonData
                        } else {
                            // Handle the case when jsonData is null or empty
                            jsonTextView.text = "Failed to fetch JSON data"
                        }
                    }
                })
        }
    }
}




