package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.Volley
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.adapter.RecyclerViewAdapter
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.Commit
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.Project
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.User
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.viewmodel.CommitViewModel
import org.json.JSONArray

class CommitFragment : Fragment() {
    private lateinit var commitViewModel: CommitViewModel
    private lateinit var project: Project
    private lateinit var user: User
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var recyclerViewAdapter: RecyclerViewAdapter
    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        commitViewModel = ViewModelProviders.of(this).get(CommitViewModel::class.java)
        recyclerViewAdapter =
            RecyclerViewAdapter()
        // nested observe to handle race condition in user model
        commitViewModel.getAllUsers().observe(this, Observer<List<User>> { data ->
            if (data.isNotEmpty()) {
                user = data[0]
                commitViewModel.getSelectedProject().observe(this, Observer<List<Project>> { data ->
                    if (data.isNotEmpty()) {
                        project = data[0]
                        commitViewModel.deleteAllCommits()
                        makeRequest(1)
                    }
                })
            }
        })

        return inflater.inflate(R.layout.fragment_commit, container, false)
    }

    // set recyclerView when view been created
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.commit_recyclerview)
        linearLayoutManager = LinearLayoutManager(context)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)
        recyclerView.adapter = recyclerViewAdapter
        val itemDecoration = DividerItemDecoration(context, DividerItemDecoration.VERTICAL)
        itemDecoration.setDrawable(
            ContextCompat.getDrawable(
                context!!,
                R.drawable.recyclerview_divider
            )!!
        )
        recyclerView.addItemDecoration(itemDecoration)

        commitViewModel.getAllCommits().observe(this, Observer<List<Commit>> { data ->
            recyclerViewAdapter.setData(data)
        })

    }

    private fun makeRequest(page: Int) {
        Log.d("make request", "detected in function makeRequest")
        // Request a string response from the provided URL.
        Thread(Runnable {
            val queue = Volley.newRequestQueue(context)
            var url =
                if (page > 1) "https://gitlab.com/api/v4/projects/${project.id}/repository/commits?private_token=${user.pat}&page=$page&per_page=50"
                else "https://gitlab.com/api/v4/projects/${project.id}/repository/commits?private_token=${user.pat}&per_page=50"
            val jsonRequest = JsonArrayRequest(
                Request.Method.GET, url, null,
                Response.Listener<JSONArray> { response ->
                    if (response.length() in 1..50) {
                        val listCommit = ArrayList<Commit>()

                        for (i in 0 until response.length()) {
                            listCommit.add(
                                Commit(
                                    response.getJSONObject(i).get("short_id") as String,
                                    project.id,
                                    response.getJSONObject(i).get("author_name") as String,
                                    response.getJSONObject(i).get("message") as String,
                                    response.getJSONObject(i).get("authored_date") as String
                                )
                            )

                        }
                        commitViewModel.insertAll(listCommit)

                        if (response.length() == 50) {
                            makeRequest(page + 1)
                        }
                    } else if (response.length() == 0) {
                        commitViewModel.deleteAllCommits()
                        Toast.makeText(
                            view!!.context,
                            R.string.commit_empty,
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        Toast.makeText(
                            view!!.context,
                            R.string.failed_retrieve_commit,
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                },
                Response.ErrorListener {error ->
                    Log.d("error", error.toString())
                    Toast.makeText(
                        view!!.context,
                        R.string.error_making_request,
                        Toast.LENGTH_SHORT
                    ).show()
                })
            queue.add(jsonRequest)
        }).start()
        // Add the request to the RequestQueue.
    }

}