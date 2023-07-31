package com.example.workmanagerjsonfetch

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.WorkerParameters
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL

class FetchJsonWorker(context: Context, params: WorkerParameters) :
    CoroutineWorker(context, params) {

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        try {
            // Fetch the JSON data from the URL. Replace with your own URL.
            val jsonData = fetchJsonFromUrl(" https://www.zyasin.com/jsonexample.json")

            // Create the output data to pass the fetched JSON data
            val outputData = Data.Builder()
                .putString("jsonData", jsonData)
                .build()

            // Return the result with success and the output data
            Result.success(outputData)
        } catch (e: Exception) {
            e.printStackTrace()
            // Return the result with failure
            Result.failure()
        }
    }

    // Helper method to fetch JSON from the URL
    private fun fetchJsonFromUrl(urlString: String): String {
        return try {
            val url = URL(urlString)
            val connection = url.openConnection()
            connection.connectTimeout = 5000 // Set a timeout value for the connection if needed
            val inputStream = connection.getInputStream()
            val buffer = ByteArray(4096)
            val stringBuilder = StringBuilder()
            var bytesRead: Int
            while (inputStream.read(buffer).also { bytesRead = it } != -1) {
                stringBuilder.append(String(buffer, 0, bytesRead))
            }
            inputStream.close()
            stringBuilder.toString()
        } catch (e: IOException) {
            e.printStackTrace()
            ""
        }
    }
}
