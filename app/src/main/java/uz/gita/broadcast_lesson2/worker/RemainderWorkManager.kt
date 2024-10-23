package uz.gita.broadcast_lesson2.worker

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
import kotlinx.coroutines.delay
import uz.gita.broadcast_lesson2.R

class RemainderWorkManager(context: Context, workerParameters: WorkerParameters) :
    CoroutineWorker(context, workerParameters) {

    private val channelId = "Water_remainder"
    private val notificationID = 2611
    private val title = "Water remainder"
    private val desc = "Drink water and be healthy!"
    override suspend fun doWork(): Result {
        //android 12+ da notification chiqishi un quidgi funni chaqirish kk
        try {
            setForeground(createForegroundInfo(applicationContext))
        }catch (e:Exception){
            return Result.failure()
        }
        delay(10000)
        Log.d("TTT", "doWork: Success")
        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return createForegroundInfo(applicationContext)
    }

    private fun createForegroundInfo(context: Context): ForegroundInfo {
        val notification = NotificationCompat.Builder(applicationContext, channelId)
            .setContentTitle(title)
            .setTicker(title)
            .setContentText(desc)
            .setSmallIcon(R.drawable.ic_water)
            .setOngoing(true)
            .setAutoCancel(true)

        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel =
                NotificationChannel(channelId, title, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            ForegroundInfo(
                notificationID,
                notification.build(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_DATA_SYNC
            )
        } else {
            ForegroundInfo(notificationID, notification.build())
        }

    }


}