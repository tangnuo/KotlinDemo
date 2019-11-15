package com.kedacom.widget.photoview

import android.content.Context
import android.graphics.Matrix
import android.graphics.Matrix.ScaleToFit
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.View.OnLongClickListener
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.ImageView
import android.widget.ImageView.ScaleType
import android.widget.OverScroller
import kotlin.math.pow
import kotlin.math.roundToInt
import kotlin.math.sqrt

/**
 * The component of [PhotoView] which does the work allowing for zooming, scaling, panning, etc.
 * It is made public in case you need to subclass something other than AppCompatImageView and still
 * gain the functionality that [PhotoView] offers
 */
class PhotoViewAttacher(private val mImageView: ImageView) : View.OnTouchListener, View.OnLayoutChangeListener {

    private var mInterpolator: Interpolator = AccelerateDecelerateInterpolator()
    private var mZoomDuration = DEFAULT_ZOOM_DURATION
    private var mMinScale = DEFAULT_MIN_SCALE
    private var mMidScale = DEFAULT_MID_SCALE
    private var mMaxScale = DEFAULT_MAX_SCALE

    private var mAllowParentInterceptOnEdge = true
    private var mBlockParentIntercept = false

    // Gesture Detectors
    private var mGestureDetector: GestureDetector? = null
    private var mScaleDragDetector: CustomGestureDetector? = null

    // These are set so we don't keep allocating them on the heap
    private val mBaseMatrix = Matrix()
    val imageMatrix = Matrix()
    private val mSuppMatrix = Matrix()
    private val mDisplayRect = RectF()
    private val mMatrixValues = FloatArray(9)

    // Listeners
    private var mMatrixChangeListener: OnMatrixChangedListener? = null
    private var mPhotoTapListener: OnPhotoTapListener? = null
    private var mOutsidePhotoTapListener: OnOutsidePhotoTapListener? = null
    private var mViewTapListener: OnViewTapListener? = null
    private var mOnClickListener: View.OnClickListener? = null
    private var mLongClickListener: OnLongClickListener? = null
    private var mScaleChangeListener: OnScaleChangedListener? = null
    private var mSingleFlingListener: OnSingleFlingListener? = null
    private var mOnViewDragListener: OnViewDragListener? = null

    private var mCurrentFlingRunnable: FlingRunnable? = null
    private var mScrollEdge = EDGE_BOTH
    private var mBaseRotation: Float = 0.toFloat()

    private var mZoomEnabled = true

    var scaleType = ScaleType.FIT_CENTER
        set(scaleType) {
            if (Util.isSupportedScaleType(scaleType) && scaleType != this.scaleType) {
                field = scaleType
                update()
            }
        }

    private val onGestureListener = object : OnGestureListener {
        override fun onDrag(dx: Float, dy: Float) {
            if (mScaleDragDetector?.isScaling == true) {
                return  // Do not drag if we are already scaling
            }

            if (mOnViewDragListener != null) {
                mOnViewDragListener!!.onDrag(dx, dy)
            }
            mSuppMatrix.postTranslate(dx, dy)
            checkAndDisplayMatrix()

            /*
             * Here we decide whether to let the ImageView's parent to start taking
             * over the touch event.
             *
             * First we check whether this function is enabled. We never want the
             * parent to take over if we're scaling. We then check the edge we're
             * on, and the direction of the scroll (i.e. if we're pulling against
             * the edge, aka 'overscrolling', let the parent take over).
             */
            val parent = mImageView.parent
            if (mAllowParentInterceptOnEdge && mScaleDragDetector?.isScaling == false && !mBlockParentIntercept) {
                if (mScrollEdge == EDGE_BOTH
                        || mScrollEdge == EDGE_LEFT && dx >= 1f
                        || mScrollEdge == EDGE_RIGHT && dx <= -1f) {
                    parent?.requestDisallowInterceptTouchEvent(false)
                }
            } else {
                parent?.requestDisallowInterceptTouchEvent(true)
            }
        }

        override fun onFling(startX: Float, startY: Float, velocityX: Float, velocityY: Float) {
            mCurrentFlingRunnable = FlingRunnable(mImageView.context)
            mCurrentFlingRunnable!!.fling(getImageViewWidth(mImageView),
                    getImageViewHeight(mImageView), velocityX.toInt(), velocityY.toInt())
            mImageView.post(mCurrentFlingRunnable)
        }

        override fun onScale(scaleFactor: Float, focusX: Float, focusY: Float) {
            if ((scale < mMaxScale || scaleFactor < 1f) && (scale > mMinScale || scaleFactor > 1f)) {
                mScaleChangeListener?.onScaleChange(scaleFactor, focusX, focusY)
                mSuppMatrix.postScale(scaleFactor, scaleFactor, focusX, focusY)
                checkAndDisplayMatrix()
            }
        }
    }

    val displayRect: RectF?
        get() {
            checkMatrixBounds()
            return getDisplayRect(drawMatrix)
        }

    var minimumScale: Float
        get() = mMinScale
        set(minimumScale) {
            Util.checkZoomLevels(minimumScale, mMidScale, mMaxScale)
            mMinScale = minimumScale
        }

    var mediumScale: Float
        get() = mMidScale
        set(mediumScale) {
            Util.checkZoomLevels(mMinScale, mediumScale, mMaxScale)
            mMidScale = mediumScale
        }

    var maximumScale: Float
        get() = mMaxScale
        set(maximumScale) {
            Util.checkZoomLevels(mMinScale, mMidScale, maximumScale)
            mMaxScale = maximumScale
        }

    var scale: Float
        get() = sqrt((getValue(mSuppMatrix, Matrix.MSCALE_X).toDouble().pow(2.0).toFloat() + getValue(mSuppMatrix, Matrix.MSKEW_Y).toDouble().pow(2.0).toFloat()).toDouble()).toFloat()
        set(scale) = setScale(scale, false)

    private val drawMatrix: Matrix
        get() {
            imageMatrix.set(mBaseMatrix)
            imageMatrix.postConcat(mSuppMatrix)
            return imageMatrix
        }

    init {
        mImageView.setOnTouchListener(this)
        mImageView.addOnLayoutChangeListener(this)

        if (!mImageView.isInEditMode) {
            mBaseRotation = 0.0f

            // Create Gesture Detectors...
            mScaleDragDetector = CustomGestureDetector(mImageView.context, onGestureListener)

            mGestureDetector = GestureDetector(mImageView.context, object : GestureDetector.SimpleOnGestureListener() {

                // forward long click listener
                override fun onLongPress(e: MotionEvent) {
                    mLongClickListener?.onLongClick(mImageView)
                }

                override fun onFling(e1: MotionEvent, e2: MotionEvent,
                                     velocityX: Float, velocityY: Float): Boolean {
                    return when {
                        mSingleFlingListener == null -> false
                        scale > DEFAULT_MIN_SCALE -> false
                        e1.pointerCount > SINGLE_TOUCH || e1.pointerCount > SINGLE_TOUCH -> false
                        else -> mSingleFlingListener!!.onFling(e1, e2, velocityX, velocityY)
                    }
                }
            })

            mGestureDetector?.setOnDoubleTapListener(object : GestureDetector.OnDoubleTapListener {
                override fun onSingleTapConfirmed(e: MotionEvent): Boolean {
                    mOnClickListener?.onClick(mImageView)
                    val displayRect = this@PhotoViewAttacher.displayRect

                    val x = e.x
                    val y = e.y
                    mViewTapListener?.onViewTap(mImageView, x, y)

                    displayRect?.apply {
                        // Check to see if the user tapped on the photo
                        if (contains(x, y)) {
                            val xResult = (x - left) / width()
                            val yResult = (y - top) / height()
                            mPhotoTapListener?.onPhotoTap(mImageView, xResult, yResult)
                            return true
                        } else {
                            mOutsidePhotoTapListener?.onOutsidePhotoTap(mImageView)
                        }
                    }

                    return false
                }

                override fun onDoubleTap(ev: MotionEvent): Boolean {
                    return try {
                        val scale = scale
                        val x = ev.x
                        val y = ev.y

                        when {
                            scale < mediumScale -> setScale(mediumScale, x, y, true)
                            scale >= mediumScale && scale < maximumScale -> setScale(maximumScale, x, y, true)
                            else -> setScale(minimumScale, x, y, true)
                        }
                        true
                    } catch (e: ArrayIndexOutOfBoundsException) {
                        // Can sometimes happen when getX() and getY() is called
                        true
                    }
                }

                override fun onDoubleTapEvent(e: MotionEvent): Boolean {
                    // Wait for the confirmed onDoubleTap() instead
                    return false
                }
            })
        }
    }

    fun setOnDoubleTapListener(newOnDoubleTapListener: GestureDetector.OnDoubleTapListener?) {
        this.mGestureDetector?.setOnDoubleTapListener(newOnDoubleTapListener)
    }

    fun setOnScaleChangeListener(onScaleChangeListener: OnScaleChangedListener?) {
        this.mScaleChangeListener = onScaleChangeListener
    }

    fun setOnSingleFlingListener(onSingleFlingListener: OnSingleFlingListener?) {
        this.mSingleFlingListener = onSingleFlingListener
    }

    fun setDisplayMatrix(finalMatrix: Matrix?): Boolean {
        requireNotNull(finalMatrix) { "Matrix cannot be null" }

        return if (mImageView.drawable == null)
            false
        else run {
            mSuppMatrix.set(finalMatrix)
            checkAndDisplayMatrix()
            true
        }
    }

    fun setBaseRotation(degrees: Float) {
        mBaseRotation = degrees % 360
        update()
        setRotationBy(mBaseRotation)
        checkAndDisplayMatrix()
    }

    fun setRotationTo(degrees: Float) {
        mSuppMatrix.setRotate(degrees % 360)
        checkAndDisplayMatrix()
    }

    fun setRotationBy(degrees: Float) {
        mSuppMatrix.postRotate(degrees % 360)
        checkAndDisplayMatrix()
    }

    override fun onLayoutChange(v: View, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
        // Update our base matrix, as the bounds have changed
        if (left != oldLeft || top != oldTop || right != oldRight || bottom != oldBottom) {
            updateBaseMatrix(mImageView.drawable)
        }
    }

    override fun onTouch(v: View, ev: MotionEvent): Boolean {
        if (!mZoomEnabled || !Util.hasDrawable(v as ImageView)) {
            return false
        }

        var handled = false
        when (ev.action) {
            MotionEvent.ACTION_DOWN -> {
                val parent = v.getParent()
                // First, disable the Parent from intercepting the touch
                // event
                parent?.requestDisallowInterceptTouchEvent(true)

                // If we're flinging, and the user presses down, cancel
                // fling
                cancelFling()
            }

            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_UP -> {
                // If the user has zoomed less than min scale, zoom back
                // to min scale
                when {
                    scale < mMinScale -> {
                        val rect = displayRect
                        if (rect != null) {
                            v.post(AnimatedZoomRunnable(scale, mMinScale,
                                    rect.centerX(), rect.centerY()))
                            handled = true
                        }
                    }

                    scale > mMaxScale -> {
                        val rect = displayRect
                        if (rect != null) {
                            v.post(AnimatedZoomRunnable(scale, mMaxScale,
                                    rect.centerX(), rect.centerY()))
                            handled = true
                        }
                    }

                    else -> {
                    }
                }
            }
        }

        // Try the Scale/Drag detector
        mScaleDragDetector?.apply {
            val wasScaling = isScaling
            val wasDragging = isDragging

            handled = onTouchEvent(ev)

            val didntScale = !wasScaling && !isScaling
            val didntDrag = !wasDragging && !isDragging

            mBlockParentIntercept = didntScale && didntDrag
        }

        // Check to see if the user double tapped
        if (mGestureDetector?.onTouchEvent(ev) == true) {
            handled = true
        }

        return handled
    }

    fun setAllowParentInterceptOnEdge(allow: Boolean) {
        mAllowParentInterceptOnEdge = allow
    }

    fun setScaleLevels(minimumScale: Float, mediumScale: Float, maximumScale: Float) {
        Util.checkZoomLevels(minimumScale, mediumScale, maximumScale)
        mMinScale = minimumScale
        mMidScale = mediumScale
        mMaxScale = maximumScale
    }

    fun setOnLongClickListener(listener: OnLongClickListener?) {
        mLongClickListener = listener
    }

    fun setOnClickListener(listener: View.OnClickListener?) {
        mOnClickListener = listener
    }

    fun setOnMatrixChangeListener(listener: OnMatrixChangedListener?) {
        mMatrixChangeListener = listener
    }

    fun setOnPhotoTapListener(listener: OnPhotoTapListener?) {
        mPhotoTapListener = listener
    }

    fun setOnOutsidePhotoTapListener(mOutsidePhotoTapListener: OnOutsidePhotoTapListener?) {
        this.mOutsidePhotoTapListener = mOutsidePhotoTapListener
    }

    fun setOnViewTapListener(listener: OnViewTapListener?) {
        mViewTapListener = listener
    }

    fun setOnViewDragListener(listener: OnViewDragListener?) {
        mOnViewDragListener = listener
    }

    fun setScale(scale: Float, animate: Boolean) {
        setScale(scale,
                (mImageView.right / 2).toFloat(),
                (mImageView.bottom / 2).toFloat(),
                animate)
    }

    fun setScale(scale: Float, focalX: Float, focalY: Float, animate: Boolean) {
        // Check to see if the scale is within bounds
        require(!(scale < mMinScale || scale > mMaxScale)) { "Scale must be within the range of minScale and maxScale" }

        when {
            animate -> mImageView.post(AnimatedZoomRunnable(scale, scale, focalX, focalY))
            else -> {
                mSuppMatrix.setScale(scale, scale, focalX, focalY)
                checkAndDisplayMatrix()
            }
        }
    }

    @Deprecated("")
    fun isZoomEnabled(): Boolean {
        return mZoomEnabled
    }

    /**
     * Set the zoom interpolator
     *
     * @param interpolator the zoom interpolator
     */
    fun setZoomInterpolator(interpolator: Interpolator) {
        mInterpolator = interpolator
    }

    fun isZoomable(): Boolean {
        return mZoomEnabled
    }

    fun setZoomable(zoomable: Boolean) {
        mZoomEnabled = zoomable
        update()
    }

    fun update() {
        if (mZoomEnabled) {
            // Update the base matrix using the current drawable
            updateBaseMatrix(mImageView.drawable)
            return
        }
        // Reset the Matrix...
        resetMatrix()
    }

    /**
     * Get the display matrix
     *
     * @param matrix target matrix to copy to
     */
    fun getDisplayMatrix(matrix: Matrix) {
        matrix.set(drawMatrix)
    }

    /**
     * Get the current support matrix
     */
    fun getSuppMatrix(matrix: Matrix) {
        matrix.set(mSuppMatrix)
    }

    fun setZoomTransitionDuration(milliseconds: Int) {
        this.mZoomDuration = milliseconds
    }

    /**
     * Helper method that 'unpacks' a Matrix and returns the required value
     *
     * @param matrix     Matrix to unpack
     * @param whichValue Which value from Matrix.M* to return
     * @return returned value
     */
    private fun getValue(matrix: Matrix, whichValue: Int): Float {
        matrix.getValues(mMatrixValues)
        return mMatrixValues[whichValue]
    }

    /**
     * Resets the Matrix back to FIT_CENTER, and then displays its contents
     */
    private fun resetMatrix() {
        mSuppMatrix.reset()
        setRotationBy(mBaseRotation)
        setImageViewMatrix(drawMatrix)
        checkMatrixBounds()
    }

    private fun setImageViewMatrix(matrix: Matrix) {
        mImageView.imageMatrix = matrix

        // Call MatrixChangedListener if needed
        mMatrixChangeListener?.let { listener ->
            val displayRect = getDisplayRect(matrix)
            displayRect?.let { rect -> listener.onMatrixChanged(rect) }
        }
    }

    /**
     * Helper method that simply checks the Matrix, and then displays the result
     */
    private fun checkAndDisplayMatrix() {
        if (checkMatrixBounds()) {
            setImageViewMatrix(drawMatrix)
        }
    }

    /**
     * Helper method that maps the supplied Matrix to the current Drawable
     *
     * @param matrix - Matrix to map Drawable against
     * @return RectF - Displayed Rectangle
     */
    private fun getDisplayRect(matrix: Matrix): RectF? {
        val d = mImageView.drawable
        d?.let {
            mDisplayRect.set(0f, 0f, it.intrinsicWidth.toFloat(), it.intrinsicHeight.toFloat())
            matrix.mapRect(mDisplayRect)
            return mDisplayRect
        }

        return null
    }

    /**
     * Calculate Matrix for FIT_CENTER
     *
     * @param drawable - Drawable being displayed
     */
    private fun updateBaseMatrix(drawable: Drawable?) {
        if (drawable == null) {
            return
        }

        val viewWidth = getImageViewWidth(mImageView).toFloat()
        val viewHeight = getImageViewHeight(mImageView).toFloat()

        val drawableWidth = drawable.intrinsicWidth
        val drawableHeight = drawable.intrinsicHeight

        mBaseMatrix.reset()

        val widthScale = viewWidth / drawableWidth
        val heightScale = viewHeight / drawableHeight

        when (scaleType) {
            ScaleType.CENTER -> mBaseMatrix.postTranslate((viewWidth - drawableWidth) / 2f,
                    (viewHeight - drawableHeight) / 2f)
            ScaleType.CENTER_CROP -> {
                val scale = widthScale.coerceAtLeast(heightScale)
                mBaseMatrix.postScale(scale, scale)
                mBaseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2f,
                        (viewHeight - drawableHeight * scale) / 2f)

            }
            ScaleType.CENTER_INSIDE -> {
                val scale = 1.0f.coerceAtMost(widthScale.coerceAtMost(heightScale))
                mBaseMatrix.postScale(scale, scale)
                mBaseMatrix.postTranslate((viewWidth - drawableWidth * scale) / 2f,
                        (viewHeight - drawableHeight * scale) / 2f)

            }
            else -> {
                var mTempSrc = RectF(0f, 0f, drawableWidth.toFloat(), drawableHeight.toFloat())
                val mTempDst = RectF(0f, 0f, viewWidth, viewHeight)

                if (mBaseRotation.toInt() % 180 != 0) {
                    mTempSrc = RectF(0f, 0f, drawableHeight.toFloat(), drawableWidth.toFloat())
                }

                when (scaleType) {
                    ScaleType.FIT_CENTER -> mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.CENTER)

                    ScaleType.FIT_START -> mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.START)

                    ScaleType.FIT_END -> mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.END)

                    ScaleType.FIT_XY -> mBaseMatrix.setRectToRect(mTempSrc, mTempDst, ScaleToFit.FILL)

                    else -> {
                    }
                }
            }
        }

        resetMatrix()
    }

    private fun checkMatrixBounds(): Boolean {

        val rect = getDisplayRect(drawMatrix) ?: return false

        val height = rect.height()
        val width = rect.width()
        var deltaX = 0f
        var deltaY = 0f

        val viewHeight = getImageViewHeight(mImageView)
        when {
            height <= viewHeight -> deltaY = when (scaleType) {
                ScaleType.FIT_START -> -rect.top
                ScaleType.FIT_END -> viewHeight.toFloat() - height - rect.top
                else -> (viewHeight - height) / 2 - rect.top
            }
            rect.top > 0 -> deltaY = -rect.top
            rect.bottom < viewHeight -> deltaY = viewHeight - rect.bottom
        }

        val viewWidth = getImageViewWidth(mImageView)
        when {
            width <= viewWidth -> {
                deltaX = when (scaleType) {
                    ScaleType.FIT_START -> -rect.left
                    ScaleType.FIT_END -> viewWidth.toFloat() - width - rect.left
                    else -> (viewWidth - width) / 2 - rect.left
                }
                mScrollEdge = EDGE_BOTH
            }
            rect.left > 0 -> {
                mScrollEdge = EDGE_LEFT
                deltaX = -rect.left
            }
            rect.right < viewWidth -> {
                deltaX = viewWidth - rect.right
                mScrollEdge = EDGE_RIGHT
            }
            else -> mScrollEdge = EDGE_NONE
        }

        // Finally actually translate the matrix

        // Finally actually translate the matrix
        mSuppMatrix.postTranslate(deltaX, deltaY)
        return true
    }

    private fun getImageViewWidth(imageView: ImageView): Int {
        return imageView.width - imageView.paddingLeft - imageView.paddingRight
    }

    private fun getImageViewHeight(imageView: ImageView): Int {
        return imageView.height - imageView.paddingTop - imageView.paddingBottom
    }

    private fun cancelFling() {
        if (mCurrentFlingRunnable != null) {
            mCurrentFlingRunnable!!.cancelFling()
            mCurrentFlingRunnable = null
        }
    }

    private inner class AnimatedZoomRunnable(private val mZoomStart: Float, private val mZoomEnd: Float,
                                             private val mFocalX: Float, private val mFocalY: Float) : Runnable {
        private val mStartTime: Long = System.currentTimeMillis()

        override fun run() {

            val t = interpolate()
            val scale = mZoomStart + t * (mZoomEnd - mZoomStart)
            val deltaScale = scale / this@PhotoViewAttacher.scale

            onGestureListener.onScale(deltaScale, mFocalX, mFocalY)

            // We haven't hit our target scale yet, so post ourselves again
            if (t < 1f) {
                Compat.postOnAnimation(mImageView, this)
            }
        }

        private fun interpolate(): Float {
            var t = 1f * (System.currentTimeMillis() - mStartTime) / mZoomDuration
            t = 1f.coerceAtMost(t)
            t = mInterpolator.getInterpolation(t)
            return t
        }
    }

    private inner class FlingRunnable(context: Context) : Runnable {

        private val mScroller: OverScroller = OverScroller(context)
        private var mCurrentX: Int = 0
        private var mCurrentY: Int = 0

        fun cancelFling() {
            mScroller.forceFinished(true)
        }

        fun fling(viewWidth: Int, viewHeight: Int, velocityX: Int,
                  velocityY: Int) {
            val rect = displayRect ?: return

            val startX = (-rect.left).roundToInt()
            val minX: Int
            val maxX: Int
            val minY: Int
            val maxY: Int

            if (viewWidth < rect.width()) {
                minX = 0
                maxX = (rect.width() - viewWidth).roundToInt()
            } else {
                maxX = startX
                minX = maxX
            }

            val startY = (-rect.top).roundToInt()
            if (viewHeight < rect.height()) {
                minY = 0
                maxY = (rect.height() - viewHeight).roundToInt()
            } else {
                maxY = startY
                minY = maxY
            }

            mCurrentX = startX
            mCurrentY = startY

            // If we actually can move, fling the scroller
            if (startX != maxX || startY != maxY) {
                mScroller.fling(startX, startY, velocityX, velocityY, minX,
                        maxX, minY, maxY, 0, 0)
            }
        }

        override fun run() {
            if (mScroller.isFinished) {
                return  // remaining post that should not be handled
            }

            if (mScroller.computeScrollOffset()) {

                val newX = mScroller.currX
                val newY = mScroller.currY

                mSuppMatrix.postTranslate((mCurrentX - newX).toFloat(), (mCurrentY - newY).toFloat())
                checkAndDisplayMatrix()

                mCurrentX = newX
                mCurrentY = newY

                // Post On animation
                Compat.postOnAnimation(mImageView, this)
            }
        }
    }

    companion object {

        private const val DEFAULT_MAX_SCALE = 3.0f
        private const val DEFAULT_MID_SCALE = 1.75f
        private const val DEFAULT_MIN_SCALE = 1.0f
        private const val DEFAULT_ZOOM_DURATION = 200

        private const val EDGE_NONE = -1
        private const val EDGE_LEFT = 0
        private const val EDGE_RIGHT = 1
        private const val EDGE_BOTH = 2
        private const val SINGLE_TOUCH = 1
    }
}
