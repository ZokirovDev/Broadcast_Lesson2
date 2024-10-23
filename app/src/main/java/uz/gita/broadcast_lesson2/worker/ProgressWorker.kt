package uz.gita.broadcast_lesson2.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay

class ProgressWorker(context: Context,workerParameters: WorkerParameters):CoroutineWorker(context,workerParameters) {
    companion object{
        private const val delayDuration = 100L
    }

    override suspend fun doWork(): Result {
        for (i in 0..100){
            setProgress(workDataOf("Progress" to i))
            delay(delayDuration)
        }
        return Result.success()
    }

}