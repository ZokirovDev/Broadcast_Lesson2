package uz.gita.broadcast_lesson2

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import uz.gita.broadcast_lesson2.ui.theme.Broadcast_Lesson2Theme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Broadcast_Lesson2Theme {
                val context = LocalContext.current
                LaunchedEffect(key1 = Unit) {
                    val workManager = WorkManager.getInstance(context)
//                    val muxlisaRequest = OneTimeWorkRequestBuilder<MuxlisaWorker>()
//                        .addTag("Muxlisaxon")
////                        .setInitialDelay(Duration.ofSeconds(5))
//                        .setBackoffCriteria(backoffPolicy = BackoffPolicy.EXPONENTIAL, Duration.ofSeconds(3))
//                        .build()
//                    workManager.enqueue(muxlisaRequest)
//                    workManager.getWorkInfoByIdFlow(muxlisaRequest.id).onEach {
//
//                        Log.d("TTT", "onCreate: DATA->${it.progress.getString("Muxlis")}")
//                    }.launchIn(lifecycleScope)
//                    delay(10000)
//                    workManager.cancelAllWorkByTag("Muxlisaxon")

                    val muxlisaPeriodic = PeriodicWorkRequestBuilder<MuxlisaWorker>(
                        repeatIntervalTimeUnit = TimeUnit.MINUTES,
                        repeatInterval = 16,
                        flexTimeInterval = 10,
                        flexTimeIntervalUnit = TimeUnit.MINUTES
                    ).build()
                    workManager.enqueueUniquePeriodicWork(
                        "Muxlisa",
                        ExistingPeriodicWorkPolicy.KEEP,
                        muxlisaPeriodic
                    )
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Button(onClick = {
            Intent().also { intent ->
                intent.setAction("uz.gita.broadcast.CUSTOM_RECEIVER")
                intent.putExtra("data", "Salom dangasalar!")
                context.sendBroadcast(intent)
            }
        }) {
            Text(
                text = "Send broadcast",
                modifier = modifier
            )

        }
    }
}

