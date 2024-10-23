package uz.gita.broadcast_lesson2

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import uz.gita.broadcast_lesson2.ui.theme.Broadcast_Lesson2Theme
import uz.gita.broadcast_lesson2.worker.RemainderWorkManager
import java.util.concurrent.TimeUnit

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()


        setContent {
            val context = LocalContext.current
            //Bir martalik workManager ishlatmoqchi bolsak quidagicha request yataib ishga tushiramiz
            val remainderWorkManager = OneTimeWorkRequestBuilder<RemainderWorkManager>()
                //agar workerimiz qanchadur vaqtdan so`ng ishlashini hohlaskak, quidagicha berishimiz mn
                .setInitialDelay(10, TimeUnit.SECONDS)
                //agar workerdan Result.rety() javobi qaytsa, keyingi urinishni qanchadan keyin boshlashini belgilashimiz ham mn
                .setBackoffCriteria(
                    //Bu yerda BackoffPolicy 2xil bolishi mn. Linear va exponential.
                    //Linear berilgan vaqtdan keyin urinishlar ham shu tartibda saqlanadi
                    //Exponentialda esa arifmetik oshib ketaveradi. mn: 10,20,40,80 va h.k
                    BackoffPolicy.LINEAR,
                    10, TimeUnit.SECONDS
                )
                .build()
            WorkManager.getInstance(context).enqueue(remainderWorkManager)

            Broadcast_Lesson2Theme {
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

