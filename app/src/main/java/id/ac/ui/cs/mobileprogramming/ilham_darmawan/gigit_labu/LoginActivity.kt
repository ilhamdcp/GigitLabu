package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.WindowManager
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyLog
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import com.android.volley.toolbox.StringRequest
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.User
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.viewmodel.UserViewModel
import org.json.JSONArray
import java.lang.Exception


class LoginActivity : AppCompatActivity() {
    private lateinit var userViewModel: UserViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)

    }

    // make request to Gitlab API to validata user data
    fun validateUserData(view: View) {
        VolleyLog.DEBUG = true
        val username = findViewById<EditText>(R.id.username_form).text.toString().trim()
        val pat = findViewById<EditText>(R.id.pat_form).text.toString().trim()

        if (username == "" || pat == "") {
            Toast.makeText(
                this,
                "Username and Personal Access Token should not be empty",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val queue = Volley.newRequestQueue(this)
            val url = "https://gitlab.com/api/v4/users/$username/projects?private_token=$pat"

            val jsonRequest = StringRequest(
                Request.Method.GET, url,
                Response.Listener<String> { response ->
                    // Check whether the response is an array
                    try {
                        val jsonResponse = JSONArray(response)
                        Toast.makeText(this, "success", Toast.LENGTH_SHORT).show()
                        userViewModel.insert(
                            User(
                                username,
                                pat,
                                null
                            )
                        )
                        Handler().post {
                            startActivity(Intent(this, MainActivity::class.java))
                            this.finish()
                        }
                    } catch (e: Exception) {
                        // if not then it is an error message
                        // for now it is considered as 401
                        val jsonResponse = JSONObject(response)
                        if (jsonResponse.get("message") == "401 Unauthorized") {
                            Toast.makeText(
                                this,
                                "Invalid username or Personal Access Token",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                },
                Response.ErrorListener { error ->
                    Toast.makeText(this, "error bro", Toast.LENGTH_SHORT).show()
                })

// Add the request to the RequestQueue.
            queue.add(jsonRequest)
        }
    }
}
