package uz.gita.broadcast_lesson2

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.ServiceInfo
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

class MuxlisaWorker(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {
    override suspend fun doWork(): Result {
        repeat(5) {
            delay(1000)
            setProgress(workDataOf("Muxlis" to "Xolisbek"))
        }
        delay(8000)
        Log.d("TTT", "doWork: Result.success()")
        setForeground(getMyForegroundInfo(applicationContext))
        return Result.retry()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return getMyForegroundInfo(applicationContext)
    }

    private fun getMyForegroundInfo(context: Context): ForegroundInfo {
        val notificationId = 100
        val channelId = "MuxlisaID"
        val title = "Muxlisa"
        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText("Bu AI mohir devni qizi")
            .setSmallIcon(R.drawable.ic_water)
            .setOngoing(true)
            .setAutoCancel(true)
            .build()
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(notificationId, notification,ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC)
        } else {
            ForegroundInfo(notificationId, notification)
        }
    }
}