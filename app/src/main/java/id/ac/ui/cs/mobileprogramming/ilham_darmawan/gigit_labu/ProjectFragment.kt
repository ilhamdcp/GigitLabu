package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.Project
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.User
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.viewmodel.ProjectViewModel
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.adapter.SpinnerAdapter
import org.json.JSONArray
import kotlin.collections.ArrayList

class ProjectFragment : Fragment() {
    private lateinit var projectViewModel: ProjectViewModel
    private lateinit var user: User
    private lateinit var spinner: Spinner

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        projectViewModel = ViewModelProviders.of(this).get(ProjectViewModel::class.java)
        return inflater.inflate(R.layout.fragment_project, container, false)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner = view.findViewById(R.id.project_spinner)
        projectViewModel.getAllUsers().observe(this, Observer<List<User>> { data ->
            if (data.isNotEmpty()) {
                user = data[0]
                observeProject()
            }
        })
        view.findViewById<Button>(R.id.select_project_button).setOnClickListener {
            projectViewModel.changeSelectedProject(spinner.selectedItem as Project)
            Toast.makeText(context, spinner.selectedItem.toString(), Toast.LENGTH_SHORT).show()
        }
    }

    // observe project live data and provide the data to spinner (dropdown menu)
    private fun observeProject() {
        projectViewModel.getAllProjects().observe(this, Observer<List<Project>> { data ->
            if (data.isEmpty()) {
                makeRequest(1)
                Toast.makeText(view!!.context, user.username, Toast.LENGTH_SHORT).show()
            } else {
                val adapter = SpinnerAdapter(
                        context!!,
                        R.id.project_spinner,
                        R.id.project_item,
                        data.toList()
                    )
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = adapter
            }
        })
    }

    private fun makeRequest(page: Int) {
        val queue = Volley.newRequestQueue(context)
        // Request a string response from the provided URL.
        Thread(Runnable {
            // request project by a max of 50 per page, if the projects are equal to 50 then make a request for the next page
            var url =
                if (page > 1) "https://gitlab.com/api/v4/users/${user.username}/projects?private_token=${user.pat}&page=$page&per_page=50"
                else "https://gitlab.com/api/v4/users/${user.username}/projects?private_token=${user.pat}&per_page=50"
            val jsonRequest = JsonArrayRequest(
                Request.Method.GET, url, null,
                Response.Listener<JSONArray> { response ->
                    if (response.length() in 1..50) {
                        val listProject = ArrayList<Project>()

                        for (i in 0 until response.length()) {
                            listProject.add(
                                Project(
                                    response.getJSONObject(i).get("id") as Int,
                                    response.getJSONObject(i).get("name") as String,
                                    user.username,
                                    false
                                )
                            )
                        }
                        Log.d("PROJECT PAGE", page.toString())
                        projectViewModel.insertAll(listProject)

                        if (response.length() == 50) {
                            makeRequest(page + 1)
                        }
                    } else {
                        Toast.makeText(
                            view!!.context,
                            "Project Failed to be retrieved",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                },
                Response.ErrorListener {
                    Toast.makeText(view!!.context, "error bro", Toast.LENGTH_SHORT).show()
                })

            // Add the request to the RequestQueue.
            queue.add(jsonRequest)
        }).start()
    }
}