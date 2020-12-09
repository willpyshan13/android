package com.owncloud.android.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.owncloud.android.MainApp
import com.owncloud.android.R
import com.owncloud.android.lib.resources.files.SearchRemoteOperation
import com.owncloud.android.ui.activity.FileDisplayActivity
import com.owncloud.android.ui.events.SearchEvent
import kotlinx.android.synthetic.main.fragment_all_file_home.*
import kotlinx.android.synthetic.main.fragment_more.*
import org.parceler.Parcels

class HomeAllFileFragment : Fragment() {

    private var fileFragment: Fragment? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_file_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val activity = activity as FileDisplayActivity

        nav_mine_zone.setOnClickListener {

        }

        nav_group_zone.setOnClickListener {
            MainApp.showOnlyFilesOnDevice(false)
            showFiles(SearchEvent("", SearchRemoteOperation.SearchType.SHARED_FILTER))
        }

        nav_shared_zone.setOnClickListener {
            MainApp.showOnlyFilesOnDevice(true)
            showFiles(null)
        }
        (activity as? FileDisplayActivity)?.setupToolbar()
    }

    private fun showFiles(searchEvent: SearchEvent?) {
        val bundle = Bundle()
        searchEvent?.apply {
            bundle.putParcelable(OCFileListFragment.SEARCH_EVENT, Parcels.wrap(searchEvent))
        }
        val fragment = OCFileListFragment()
        fragment.arguments = bundle
        childFragmentManager.beginTransaction()
            .replace(R.id.container, fragment, FileDisplayActivity.TAG_LIST_OF_FILES)
            .commit()
        contentView.visibility = View.GONE
        fileFragment = fragment
    }

    fun getListOfFilesFragment(): OCFileListFragment? {
        if (!isAdded) {
            return null
        }
        val listOfFiles: Fragment? = childFragmentManager.findFragmentByTag(FileDisplayActivity.TAG_LIST_OF_FILES)
        if (listOfFiles != null) {
            return listOfFiles as OCFileListFragment
        }
        return null
    }

    fun removeFiles() {
        fileFragment?.apply {
            this@HomeAllFileFragment.childFragmentManager.beginTransaction()
                .remove(this)
                .commit()
        }
        fileFragment = null
        contentView.visibility = View.VISIBLE
    }


    fun isRoot(): Boolean {
        return fileFragment == null
    }


}
