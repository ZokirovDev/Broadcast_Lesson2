package uz.gita.broadcast_lesson2

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.lifecycle.Observer
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import kotlinx.coroutines.delay
import uz.gita.broadcast_lesson2.ui.theme.Broadcast_Lesson2Theme
import uz.gita.broadcast_lesson2.worker.RemainderWorkManager
import java.time.Duration
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Broadcast_Lesson2Theme {
                val lifecycleOwner = LocalLifecycleOwner.current
                val postNotPermissio =
                    rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
                LaunchedEffect(key1 = Unit) {
                    val workManager = WorkManager.getInstance(applicationContext)

                    if (postNotPermissio.status.isGranted) {
//                        val workRequest =
//                            OneTimeWorkRequestBuilder<RemainderWorkManager>()
//                            .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
//                            .setBackoffCriteria(
//                                backoffPolicy = BackoffPolicy.LINEAR,
//                                duration = Duration.ofSeconds(15)
//                            )
//                            .build()
                        val periodicWorkReq = PeriodicWorkRequestBuilder<RemainderWorkManager>(
                            repeatInterval = 15,
                            repeatIntervalTimeUnit = TimeUnit.MINUTES
                        ).setBackoffCriteria(
                                backoffPolicy = BackoffPolicy.LINEAR,
                                duration = Duration.ofSeconds(15)
                            ).build()
                        workManager.enqueue(periodicWorkReq)
                    } else {
                        postNotPermissio.launchPermissionRequest()
                    }
                    val constraints = Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.UNMETERED)
                        .setRequiresCharging(true)
                        .build()
                    val progressWorkRequest = OneTimeWorkRequestBuilder<RemainderWorkManager>()
                        .addTag("progressWorkRequest")
//                        .setConstraints(constraints)
                        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
                        .setBackoffCriteria(
                            backoffPolicy = BackoffPolicy.LINEAR,
                            duration = Duration.ofSeconds(15)
                        )
                        .build()
                    workManager.enqueue(progressWorkRequest)

                    workManager.getWorkInfoByIdLiveData(progressWorkRequest.id)
                        .observe(lifecycleOwner, Observer { workInfo ->
                            Log.d("TTT", "onCreate: STATE->${workInfo.state}")
                        })
                    delay(5000)
                    workManager.cancelAllWorkByTag("progressWorkRequest")


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

