package mattie.freelancer.flickerbrowser

import android.content.Context
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.recyclerview.widget.RecyclerView

class RecyclerItemClickListener(
    context: Context,
    recyclerView: RecyclerView,
    private val listener: OnRecycleClickListener
) : RecyclerView.SimpleOnItemTouchListener() {

    @Suppress("PrivatePropertyName")
    private val TAG = "RecyclerItemClickListen"

    interface OnRecycleClickListener {
        fun onItemClick(view: View, position: Int)
        fun onItemLongClick(view: View, position: Int)
    }

    // add the gestureDetector
    private val gestureDetector =
        GestureDetectorCompat(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onSingleTapUp(e: MotionEvent): Boolean {
                Log.d(TAG, "onSingleTapUp: starts")
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                Log.d(TAG, "onSingleTapUp: calling listener.onItemClicked")
                if (childView != null) {
                    listener.onItemClick(childView, recyclerView.getChildAdapterPosition(childView))
                }
                return true
            }

            override fun onLongPress(e: MotionEvent) {
                Log.d(TAG, "onLongPress: starts")
                val childView = recyclerView.findChildViewUnder(e.x, e.y)
                Log.d(TAG, "onLongPress: calling listener.onItemLongClicked")
                if (childView != null) {
                    listener.onItemLongClick(
                        childView,
                        recyclerView.getChildAdapterPosition(childView)
                    )
                }
            }
        })

    override fun onInterceptTouchEvent(rv: RecyclerView, e: MotionEvent): Boolean {
        Log.d(TAG, "onInterceptTouchEvent: starts $e")
        val result = gestureDetector.onTouchEvent(e)
        Log.d(TAG, "onInterceptTouchEvent: returning $result")
//        return super.onInterceptTouchEvent(rv, e)
        return false
    }
}