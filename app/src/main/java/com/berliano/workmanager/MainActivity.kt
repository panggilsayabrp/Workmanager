package com.berliano.workmanager

import android.os.Build
import android.os.Bundle
import android.os.StrictMode
import androidx.appcompat.app.AppCompatActivity
import androidx.work.Data
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import com.berliano.workmanager.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var workManager : WorkManager
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

      /*  if (Build.VERSION.SDK_INT > 28) {
            val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
            StrictMode.setThreadPolicy(policy)
        }*/

        workManager = WorkManager.getInstance(this)
        binding.btnOneTask.setOnClickListener { StartOneTimeTask() }
    }

    private fun StartOneTimeTask() {
        binding.textView2.text = getString(R.string.status)
        val data = Data.Builder()
            .putString(MyWorker.EXTRA_CITY, binding.editText.text.toString())
            .build()

        val oneTimeWorkRequest = OneTimeWorkRequest.Builder(MyWorker::class.java)
            .setInputData(data)
            .build()

        workManager.enqueue(oneTimeWorkRequest)
        workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
            .observe(this, { workInfo ->
                val status = workInfo.state.name
                binding.textView2.append("\n" + status)
            })
    }
}