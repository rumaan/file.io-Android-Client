package com.thecoolguy.rumaan.fileio.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import com.thecoolguy.rumaan.fileio.R

/**
 * Thanks to
 * @author Kitek -- Github
 * */
abstract class SwipeToDeleteCallBack(context: Context) : ItemTouchHelper.SimpleCallback(
    0, ItemTouchHelper.LEFT
) {

  private val deleteIcon: Drawable = context.getDrawable(R.drawable.ic_delete_white_24dp)
  private val intrinsicWidth = deleteIcon.intrinsicWidth
  private val intrinsicHeight = deleteIcon.intrinsicHeight

  private val background = ColorDrawable()
  private val backgroundColor = Color.parseColor("#f44336")

  private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

  override fun getMovementFlags(
          recyclerView: androidx.recyclerview.widget.RecyclerView?,
          viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder?
  ): Int {
    // Disable swipes for date view
    if (viewHolder is UploadHistoryListAdapter.UploadHistoryListDateViewHolder) {
      return 0
    }
    return super.getMovementFlags(recyclerView, viewHolder)
  }

  override fun onMove(
          recyclerView: androidx.recyclerview.widget.RecyclerView?,
          viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder?,
          target: androidx.recyclerview.widget.RecyclerView.ViewHolder?
  ): Boolean {
    return false
  }

  override fun onChildDraw(
          c: Canvas?,
          recyclerView: androidx.recyclerview.widget.RecyclerView?,
          viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder,
          dX: Float,
          dY: Float,
          actionState: Int,
          isCurrentlyActive: Boolean
  ) {
    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)

    val itemView = viewHolder.itemView
    val itemHeight = itemView.bottom - itemView.top

    val isCanceled = dX == 0f && !isCurrentlyActive

    if (isCanceled) {
      clearCanvas(
          c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(),
          itemView.bottom.toFloat()
      )
      super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
      return
    }

    // Draw the red delete background
    background.color = backgroundColor
    background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
    background.draw(c)

    // Calculate position of delete icon
    val deleteIconTop = itemView.top + (itemHeight - intrinsicHeight) / 2
    val deleteIconMargin = (itemHeight - intrinsicHeight) / 2
    val deleteIconLeft = itemView.right - deleteIconMargin - intrinsicWidth
    val deleteIconRight = itemView.right - deleteIconMargin
    val deleteIconBottom = deleteIconTop + intrinsicHeight

    // Draw the delete icon
    deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
    deleteIcon.draw(c)

    super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
  }

  private fun clearCanvas(
    c: Canvas?,
    left: Float,
    top: Float,
    right: Float,
    bottom: Float
  ) {
    c?.drawRect(left, top, right, bottom, clearPaint)
  }
}
