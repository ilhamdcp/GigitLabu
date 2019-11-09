package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.User
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.viewmodel.*
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.receiver.ReminderReceiver
import org.json.JSONArray
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.io.OutputStream

class ProfileFragment : Fragment() {
    private lateinit var userViewModel: UserViewModel
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var commitViewModel: CommitViewModel
    private lateinit var buttonSaveListener: ButtonSaveListener
    private val WRITE_EXTERNAL_STORAGE_REQUEST_CODE = 101

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        userViewModel = ViewModelProviders.of(this).get(UserViewModel::class.java)
        projectViewModel = ViewModelProviders.of(this).get(ProjectViewModel::class.java)
        commitViewModel = ViewModelProviders.of(this).get(CommitViewModel::class.java)

        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        view.findViewById<Button>(R.id.logout_button).setOnClickListener { logout() }
        userViewModel.getAllUsers().observe(this, Observer<List<User>> { data ->
            if (data.isNotEmpty() && data[0].avatar == null) {
                makeRequest(data[0])
            } else if (data.isNotEmpty() && data[0].avatar != null) {
                // display user data on profile fragment
                view.findViewById<ImageView>(R.id.user_avatar).setImageBitmap(data[0].avatar?.let {
                    stringToBitMap(
                        it
                    )
                })
                view.findViewById<TextView>(R.id.username).text = data[0].username
                view.findViewById<TextView>(R.id.pat_form).text = data[0].pat

                buttonSaveListener = ButtonSaveListener(
                    data[0],
                    view.findViewById(R.id.user_avatar)
                )
                view.findViewById<Button>(R.id.save_image_button)
                    .setOnClickListener(
                        buttonSaveListener
                    )
            } else {
                startActivity(Intent(view.context, LoginActivity::class.java))
                activity?.finish()
            }
        })
    }

    //
    private fun cancelAlarm() {
        val currentIntent = Intent(context, ReminderReceiver::class.java)
        val pendingIntent =
            PendingIntent.getBroadcast(context, 0, currentIntent, PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = context!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }

    // flush view model and disable notification
    fun logout() {
        cancelAlarm()
        commitViewModel.deleteAllCommits()
        projectViewModel.deleteAllUsers()
        userViewModel.deleteAllUsers()
    }

    // convert bitmap to string to be saved in DB
    fun bitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }

    // reconvert to bitmap to bet set as value in ImageView
    fun stringToBitMap(encodedString: String): Bitmap? {
        return try {
            val encodeByte = Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }

    }


    // add permission callback handler to save image immediately after
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            WRITE_EXTERNAL_STORAGE_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    buttonSaveListener.saveImage(view!!)
            }
        }
    }

    // retrieve avatar, called when user logs in for the first time
    fun makeRequest(user: User) {
        val queue = Volley.newRequestQueue(view!!.context)
        val url = "https://gitlab.com/api/v4/users?username=${user.username}"

        // Request a string response from the provided URL.
        val jsonRequest = JsonArrayRequest(
            Request.Method.GET, url, null,
            Response.Listener<JSONArray> { response ->
                if (response.length() > 0) {
                    val avatarUrl = response.getJSONObject(0)["avatar_url"].toString()
                    DownloadImageTask(
                        view!!.findViewById(R.id.user_avatar),
                        user
                    ).execute(avatarUrl)
                    view!!.findViewById<TextView>(R.id.username).text = user.username
                    view!!.findViewById<TextView>(R.id.pat_form).text = user.pat
                }

            },
            Response.ErrorListener {
                Toast.makeText(
                    view!!.context,
                    resources.getString(R.string.failed_retrieve_avatar),
                    Toast.LENGTH_SHORT
                ).show()
            })

        // Add the request to the RequestQueue.
        queue.add(jsonRequest)
    }

    private inner class ButtonSaveListener(private val user: User, private val image: ImageView) :
        View.OnClickListener {

        override fun onClick(v: View?) {
            // popup request permission to write to external storage
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                if (checkSelfPermission(
                        v!!.context,
                        WRITE_EXTERNAL_STORAGE
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    saveImage(v)
                } else {
                    Log.v("CHECK PERMISSION", "Permission denied")
                    requestPermissions(
                        arrayOf(WRITE_EXTERNAL_STORAGE),
                        WRITE_EXTERNAL_STORAGE_REQUEST_CODE
                    )
                }

        }

        // save image to gallery
        fun saveImage(
            v: View?
        ) {
            val bitmap = image.drawable.toBitmap(128, 128)
            val displayName = "Avatar of ${user.username}"

            // get URI location and define type of media
            val relativeLocation =
                Environment.DIRECTORY_PICTURES + File.separator + resources.getString(R.string.app_name)
            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, displayName)
                put(MediaStore.MediaColumns.MIME_TYPE, "image/png")

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    put(MediaStore.Images.ImageColumns.RELATIVE_PATH, relativeLocation)
                }
            }

            // get content resolver and stream to write image file
            val contentResolver = v!!.context.contentResolver
            var uri: Uri? = null
            var stream: OutputStream? = null
            try {
                uri = contentResolver.insert(EXTERNAL_CONTENT_URI, contentValues)
                    ?: throw IOException(resources.getString(R.string.failed_create_mediastore_record))

                stream = contentResolver.openOutputStream(uri)
                    ?: throw IOException(resources.getString(R.string.failed_outputstream))

                if (!bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)) {
                    throw IOException(resources.getString(R.string.failed_save_bitmap))
                }

                Toast.makeText(
                    v.context,
                    resources.getString(R.string.avatar_saved),
                    Toast.LENGTH_SHORT
                ).show()


            } catch (e: IOException) {
                if (uri != null) {
                    contentResolver.delete(uri, null, null)
                }

                throw IOException(e)

            } finally {
                stream?.close()
            }
        }

    }

    // Reserve image download in async task to prevent UI blocking
    private inner class DownloadImageTask(
        internal var bmImage: ImageView,
        internal val user: User
    ) :
        AsyncTask<String, Void, Bitmap>() {

        override fun doInBackground(vararg urls: String): Bitmap? {
            val urldisplay = urls[0]
            var mIcon11: Bitmap? = null
            try {
                val `in` = java.net.URL(urldisplay).openStream()
                mIcon11 = BitmapFactory.decodeStream(`in`)
            } catch (e: Exception) {
                Log.e("Error", e.message!!)
                e.printStackTrace()
            }

            return mIcon11
        }

        override fun onPostExecute(result: Bitmap) {
            bmImage.setImageBitmap(result)
            userViewModel.updateAvatar(user, bitMapToString(result))

        }
    }
}