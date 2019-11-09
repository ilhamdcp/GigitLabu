package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.util.*
import android.os.PersistableBundle
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.receiver.NetworkStateChangedReceiver
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.receiver.ReminderReceiver


class MainActivity : AppCompatActivity() {
    private val projectFragment =
        ProjectFragment()
    private val commitFragment =
        CommitFragment()
    private val profileFragment =
        ProfileFragment()
    private var active: Fragment = projectFragment

    private lateinit var networkStateChangedReceiver: NetworkStateChangedReceiver
    private lateinit var navigation: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        navigation = findViewById(R.id.bottom_navigation)


        navigation.setOnNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.home_menu -> {
                    supportFragmentManager.beginTransaction().hide(active).show(projectFragment)
                        .commit()
                    active = projectFragment
                    true
                }
                R.id.commit_menu -> {
                    supportFragmentManager.beginTransaction().hide(active).show(commitFragment)
                        .commit()
                    active = commitFragment
                    true
                }
                R.id.profile_menu -> {
                    supportFragmentManager.beginTransaction().hide(active).show(profileFragment)
                        .commit()
                    active = profileFragment
                    true
                }
                else -> {
                    true
                }
            }
        }




        Log.d("active fragment", "${active}")



        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, profileFragment, "profile").hide(profileFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, commitFragment, "commit").hide(commitFragment)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragment_container, projectFragment, "project")
            .commit()
        supportFragmentManager.beginTransaction().show(active).commit()



        networkStateChangedReceiver =
            NetworkStateChangedReceiver(
                findViewById<TextView>(R.id.no_connection_textview)
            )
        registerReceiver(
            networkStateChangedReceiver, IntentFilter("android.net.conn.CONNECTIVITY_CHANGE")
        )

        createNotificationChannel()
        setReminder()

    }


    override fun onSaveInstanceState(outState: Bundle, outPersistentState: PersistableBundle) {
        super.onSaveInstanceState(outState, outPersistentState)
        outState.putInt("activeId", active.id)
        Log.d("ACTIVE ID", active.id.toString())
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        val activeId = savedInstanceState.getInt("activeId")
        if (activeId == profileFragment.id) {
            active = profileFragment
        } else if (activeId == commitFragment.id) {
            active = commitFragment
        } else {
            active = projectFragment
        }
    }

    // set daily reminder at 8 AM everyday
    private fun setReminder() {
        val currentIntent = Intent(this, ReminderReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(this, 0, currentIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Log.d("DEBUG ALARM", "alarm set")
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.HOUR_OF_DAY, 8)
            calendar.set(Calendar.MINUTE, 0)

            if (calendar.before(Calendar.getInstance())) {
                calendar.add(Calendar.DATE, 1)
            }
            alarmManager.setInexactRepeating(
                AlarmManager.RTC,
                calendar.timeInMillis,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        } else {
            val currentDate = Date()
            val date = Date(currentDate.year, currentDate.month, currentDate.date + 1, 8, 0)
            alarmManager.setInexactRepeating(
                AlarmManager.RTC,
                date.time,
                AlarmManager.INTERVAL_DAY,
                pendingIntent
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.navigation, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun createNotificationChannel() {
        Log.d("CreateNotification", "detected")
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.app_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel =
                NotificationChannel(
                    resources.getString(R.string.channel_id),
                    name,
                    importance
                ).apply {
                    description = descriptionText
                }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(networkStateChangedReceiver)
    }


}
