package com.airi.presentation

import android.content.Context
import android.graphics.*
import android.graphics.drawable.ColorDrawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView

abstract class SwipeDelete(context: Context?)
    : ItemTouchHelper.SimpleCallback(0, (ItemTouchHelper.RIGHT)){

    private val trashIcon = ContextCompat.getDrawable(context, R.drawable.trash)
    private val trashIconIntrinsicWidth = trashIcon?.intrinsicWidth
    private val trashIconIntrinsicHeight = trashIcon?.intrinsicHeight

    private val background = ColorDrawable()
    private val rightBackgroundColor = Color.parseColor("#ff0033")
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
        val isRightDirection = dX < 0
        if (isRightDirection) {
            background.color = rightBackgroundColor
            background.setBounds(itemView.left, itemView.top, itemView.left + dX.toInt(), itemView.bottom)
        }
        background.draw(c)

        val itemHeight = itemView.bottom - itemView.top
        if (trashIcon != null
            && trashIconIntrinsicWidth != null
            && trashIconIntrinsicHeight != null){

                if (isRightDirection) {
                    // Calculate position of delete icon
                    val trashIconTop = itemView.top + (itemHeight - trashIconIntrinsicHeight) / 2
                    val trashIconMargin = (itemHeight - trashIconIntrinsicHeight) / 2
                    val trashIconLeft = itemView.right - trashIconMargin - trashIconIntrinsicWidth
                    val trashIconRight = itemView.right - trashIconMargin
                    val trashIconBottom = trashIconTop + trashIconIntrinsicHeight

                    // Draw the delete icon
                    trashIcon.setBounds(trashIconLeft, trashIconTop, trashIconRight, trashIconBottom)
                    trashIcon.draw(c)

                }

        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
    fun(c: Canvas?, left: Float, top: Float, right: Float, bottom: Float) {
        c?.drawRect(left, top, right, bottom, clearPaint)
    } }
    abstract fun clearCanvas(c: Canvas, fl: Float, toFloat: Float, toFloat1: Float, toFloat2: Float)
}