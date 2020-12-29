package com.airi.presentation

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeDelete(context: Context)
    : ItemTouchHelper.SimpleCallback(0, (ItemTouchHelper.LEFT)) {

    private val deleteIcon = ContextCompat.getDrawable(context, R.drawable.trash)
    private val deleteIconIntrinsicWidth = deleteIcon?.intrinsicWidth
    private val deleteIconIntrinsicHeight = deleteIcon?.intrinsicHeight

    private val background = ColorDrawable()
    private val leftBackgroundColor = Color.parseColor("#f44336")
    private val clearPaint = Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        val itemView = viewHolder?.itemView ?: return

        val isCanceled = dX == 0f && !isCurrentlyActive
        if (isCanceled) {
            clearCanvas(c, itemView.right + dX, itemView.top.toFloat(), itemView.right.toFloat(), itemView.bottom.toFloat())
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            return
        }

        // Draw the red delete background
        val isLeftDirection = dX < 0
        if (isLeftDirection) {
            background.color = leftBackgroundColor
            background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
        }
        background.draw(c)

        val itemHeight = itemView.bottom - itemView.top
        if (deleteIcon != null && deleteIconIntrinsicWidth != null && deleteIconIntrinsicHeight != null) {

            if (isLeftDirection) {
                // Calculate position of delete icon
                val deleteIconTop = itemView.top + (itemHeight - deleteIconIntrinsicHeight) / 2
                val deleteIconMargin = (itemHeight - deleteIconIntrinsicHeight) / 2
                val deleteIconLeft = itemView.right - deleteIconMargin - deleteIconIntrinsicWidth
                val deleteIconRight = itemView.right - deleteIconMargin
                val deleteIconBottom = deleteIconTop + deleteIconIntrinsicHeight

                // Draw the delete icon
                deleteIcon.setBounds(deleteIconLeft, deleteIconTop, deleteIconRight, deleteIconBottom)
                deleteIcon.draw(c)
            }
        }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }

    private fun clearCanvas(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    }
}