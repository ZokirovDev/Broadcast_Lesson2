package uz.gita.broadcast_lesson2.worker

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters

class RemainderWorkManager(context: Context,workerParameters: WorkerParameters):Worker(context,workerParameters){
    override fun doWork(): Result {
        Log.d("TTT", "doWork: SUCCESS")
        return Result.retry()
    }

}