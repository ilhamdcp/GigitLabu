package id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.R
import id.ac.ui.cs.mobileprogramming.ilham_darmawan.gigit_labu.model.Project

class SpinnerAdapter(
    private val mContext: Context,
    private val resourceId: Int,
    private val itemId: Int,
    private val projectList: List<Project>

) : ArrayAdapter<Project>(mContext, resourceId, itemId, projectList) {


    // holder for each item inside spinner
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val current = projectList[position]
        lateinit var listItem: View
        if (convertView == null)
            listItem =
                LayoutInflater.from(mContext).inflate(R.layout.project_item, parent, false)
        else
            listItem = convertView
        listItem.findViewById<TextView>(R.id.project_item).text = current.name
        return listItem
    }

    // dropdown view to handle array of items
    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        lateinit var listItem: View
        if (convertView == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.project_item, parent, false)
        else
            listItem = convertView

        val current = getItem(position)
        listItem.findViewById<TextView>(itemId).text = current?.name
        return listItem
    }


}