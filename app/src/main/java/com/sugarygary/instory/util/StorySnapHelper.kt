package com.sugarygary.instory.util

import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSnapHelper
import androidx.recyclerview.widget.RecyclerView

class StorySnapHelper : LinearSnapHelper() {
    override fun findSnapView(layoutManager: RecyclerView.LayoutManager): View? {
        val linearLayoutManager = layoutManager as? LinearLayoutManager
            ?: return super.findSnapView(layoutManager)

        return linearLayoutManager
            .takeIf { isValidSnap(it) }
            ?.run { super.findSnapView(layoutManager) }
    }

    private fun isValidSnap(linearLayoutManager: LinearLayoutManager) =
        linearLayoutManager.findFirstCompletelyVisibleItemPosition() != 0 &&
                linearLayoutManager.findLastCompletelyVisibleItemPosition() != linearLayoutManager.itemCount - 1
}