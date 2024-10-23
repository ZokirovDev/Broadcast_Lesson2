package uz.gita.broadcast_lesson2

import android.Manifest
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import uz.gita.broadcast_lesson2.ui.theme.Broadcast_Lesson2Theme
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Broadcast_Lesson2Theme {
                val context = LocalContext.current
                val notificationPermission =
                    rememberPermissionState(permission = Manifest.permission.POST_NOTIFICATIONS)
                LaunchedEffect(key1 = Unit) {
                    if (notificationPermission.status.isGranted) {
                        val workManager = WorkManager.getInstance(context)
                        val muxlisaOneTime = OneTimeWorkRequestBuilder<MuxlisaWorker>().build()

//                            PeriodicWorkRequestBuilder<MuxlisaWorker>(
//                            repeatIntervalTimeUnit = TimeUnit.MINUTES,
//                            repeatInterval = 16,
//                            flexTimeInterval = 10,
//                            flexTimeIntervalUnit = TimeUnit.MINUTES
//                        ).build()
//                        workManager.enqueueUniquePeriodicWork(
//                            "Muxlisa",
//                            ExistingPeriodicWorkPolicy.KEEP,
//                            muxlisaPeriodic
//                        )

                        workManager.enqueue(muxlisaOneTime)
                    } else {
                        notificationPermission.launchPermissionRequest()
                    }

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

