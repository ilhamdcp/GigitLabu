package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.User
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.viewmodel.UserViewModel

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        val userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        userViewModel.getAllUsers().observe(this, Observer<List<User>> { data ->
            if (data.isNotEmpty()) {
                Handler().postDelayed({
                    startActivity(Intent(this, MainActivity::class.java))
                    this.finish()
                }, 1000)
            } else {
                Handler().postDelayed({
                    startActivity(Intent(this, LoginActivity::class.java))
                    this.finish()
                }, 1000)
            }
        })
    }
}
