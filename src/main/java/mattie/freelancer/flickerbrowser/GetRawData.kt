package mattie.freelancer.flickerbrowser

import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL

/**Adding an enum to hold the download statues**/
enum class DownloadStatus {
    OK, IDLE, NOT_INITIALISED, FAILED_OR_EMPTY, PERMISSIONS_ERROR, ERROR
}

class GetRawData(private val listener: OnDownloadComplete) : AsyncTask<String, Void, String>() {
    @Suppress("PrivatePropertyName")
    private val TAG = "GetRawData"
    private var downloadStatus = DownloadStatus.IDLE

    /*
    private var listener: MainActivity? = null

    fun setDownloadCompleteListener(callback:MainActivity){
        listener = callback
    }
     */

    interface OnDownloadComplete {
        fun onDownloadComplete(data: String, status: DownloadStatus) {

        }
    }

    override fun onPostExecute(result: String) {
        Log.d(TAG, "onPostExecute: called, with data $result")
        listener.onDownloadComplete(result, downloadStatus)
    }

    override fun doInBackground(vararg params: String?): String {
        Log.d(TAG, "doInBackground: called")
        val rawData: String

        if (params[0] == null) {
            downloadStatus = DownloadStatus.NOT_INITIALISED
            return "No URL specified"
        }
        try {
            downloadStatus = DownloadStatus.OK
            rawData = URL(params[0]).readText()
        } catch (e: Exception) {
            val errorMessage = when (e) {
                is MalformedURLException -> {
                    downloadStatus = DownloadStatus.NOT_INITIALISED
                    "doInBackground: Invalid URL ${e.message}"
                }
                is IOException -> {
                    downloadStatus = DownloadStatus.FAILED_OR_EMPTY
                    "doInBackground: IO Exception reading data ${e.message}"
                }
                is SecurityException -> {
                    downloadStatus = DownloadStatus.PERMISSIONS_ERROR
                    "doInBackground: Securty exception: Needs permission? ${e.message}"
                }
                else -> {
                    downloadStatus = DownloadStatus.ERROR
                    "Unknown error ${e.message}"
                }
            }
            Log.e(TAG, "doInBackground: $errorMessage")
            return errorMessage
        }

        Log.d(TAG, "doInBackground: done, with data $rawData")
        return rawData
    }
}