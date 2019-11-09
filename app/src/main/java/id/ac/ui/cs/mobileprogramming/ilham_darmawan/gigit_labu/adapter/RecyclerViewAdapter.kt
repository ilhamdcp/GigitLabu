package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.R
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.Commit

// RecyclerViewAdapter to handle data in RecyclerView
class RecyclerViewAdapter :
    RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>() {
    var commitList = ArrayList<Commit>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val v: View = LayoutInflater.from(parent.context)
            .inflate(R.layout.commit_item, parent, false)
        return ViewHolder(v)

    }

    override fun getItemCount(): Int {
        return commitList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(commitList[position])
    }

    fun setData(newData: List<Commit>) {
        if (commitList.isNotEmpty()) {
            commitList.clear()
            commitList.addAll(newData)
            notifyDataSetChanged()
        } else {
            commitList = newData as ArrayList<Commit>
        }
    }

    // The class to hold data on each recyclerview's item
    inner class ViewHolder constructor(
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView) {

        private val commitMessage = itemView.findViewById<TextView>(R.id.commit_message)
        private val commitUser = itemView.findViewById<TextView>(R.id.commit_username)
        private val commitCreatedAt = itemView.findViewById<TextView>(R.id.commit_time)

        fun bindData(commit: Commit) {
            commitMessage.text = commit.message
            commitUser.text = commit.createdBy
            commitCreatedAt.text = parseISODate(commit.createdAt)
        }

        fun parseISODate(dateString: String): String {
            val parsedISODate = dateString.split("T")
            val date = parsedISODate[0].split("-")
            val time = parsedISODate[1].split(":")
            return "${date[2]}-${date[1]}-${date[0]} on ${time[0]}:${time[1]}"
        }

    }
// unused class reserved for further changes
/*    internal inner class CommitDiffCallback(
        private val oldCommits: List<Commit>,
        private val newCommits: List<Commit>
    ) : DiffUtil.Callback() {

        override fun getOldListSize(): Int {
            return oldCommits.size
        }

        override fun getNewListSize(): Int {
            return newCommits.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCommits[oldItemPosition].id === newCommits[newItemPosition].id
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldCommits[oldItemPosition].equals(newCommits[newItemPosition])
        }
    }*/
}
