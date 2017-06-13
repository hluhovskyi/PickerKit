package com.dewarder.pickerok;

import android.graphics.Rect;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public final class SpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int mTop;
    private final int mLeft;
    private final int mBottom;
    private final int mRight;
    private final boolean mIncludeEdges;
    private final boolean mReverseLayout;
    private final int mOrientation;

    private SpaceItemDecoration(int top, int left, int bottom, int right, boolean includeEdges, boolean reverseLayout, int orientation) {
        mTop = top;
        mLeft = left;
        mBottom = bottom;
        mRight = right;
        mIncludeEdges = includeEdges;
        mReverseLayout = reverseLayout;
        mOrientation = orientation;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        int adapterLast = parent.getAdapter().getItemCount() - 1;
        int first = mReverseLayout ? adapterLast : 0;
        int last = mReverseLayout ? 0 : adapterLast;

        if (mOrientation == OrientationHelper.HORIZONTAL) {
            modifyHorizontal(outRect, view, parent, first, last);
        } else {
            modifyVertical(outRect, view, parent, first, last);
        }
    }

    private void modifyHorizontal(Rect outRect, View view, RecyclerView parent, int first, int last) {
        outRect.top = mTop;
        outRect.bottom = mTop;
        if (parent.getChildAdapterPosition(view) == first) {
            if (mIncludeEdges) {
                outRect.left = mLeft;
            }
            outRect.right = mRight / 2;
        } else if (parent.getChildAdapterPosition(view) == last) {
            if (mIncludeEdges) {
                outRect.right = mRight;
            }
            outRect.left = mLeft / 2;
        } else {
            outRect.left = mLeft / 2;
            outRect.right = mRight / 2;
        }
    }

    private void modifyVertical(Rect outRect, View view, RecyclerView parent, int first, int last) {
        outRect.left = mLeft;
        outRect.right = mRight;
        if (parent.getChildAdapterPosition(view) == first) {
            if (mIncludeEdges) {
                outRect.top = mTop;
            }
            outRect.bottom = mBottom / 2;
        } else if (parent.getChildAdapterPosition(view) == last) {
            if (mIncludeEdges) {
                outRect.bottom = mBottom;
            }
            outRect.top = mTop / 2;
        } else {
            outRect.top = mTop / 2;
            outRect.bottom = mBottom / 2;
        }
    }

    public static SpaceItemDecoration horizontalAll(int all) {
        return new SpaceItemDecoration(all, all, all, all, true, false, OrientationHelper.HORIZONTAL);
    }
}