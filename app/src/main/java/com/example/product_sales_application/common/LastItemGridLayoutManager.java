package com.example.product_sales_application.common;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class LastItemGridLayoutManager extends GridLayoutManager {

    public LastItemGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);

        final int itemCount = state.getItemCount();
        if (itemCount > 0) {
            View lastView = recycler.getViewForPosition(itemCount - 1);
            int spanCount = getSpanCount();
            int spanSizeLookup = getSpanSizeLookup().getSpanSize(itemCount - 1);
            if (spanSizeLookup == spanCount) {
                // Last item spans entire row, so need to move it to next row
                int left = lastView.getLeft() - getPaddingLeft();
                int top = lastView.getTop() + lastView.getHeight() + getDecoratedBottom(lastView);
                int right = lastView.getRight() - getPaddingRight();
                int bottom = top + lastView.getHeight();
                layoutDecorated(lastView, left, top, right, bottom);
            } else {
                // Last item does not span entire row, so need to adjust span size lookup
                final int lastRowFirstItemIndex = ((int) Math.ceil((double) itemCount / (double) spanCount) - 1) * spanCount;
                 setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        if(itemCount % 2 == 0){
                           return (position == itemCount - 1 || position == itemCount - 2) ? getSpanCount() : 1;
                        }
                        return (position == lastRowFirstItemIndex) ? getSpanCount() : 1;
                    }
                });
                View firstViewInLastRow = findViewByPosition(lastRowFirstItemIndex);
                if (firstViewInLastRow != null) {
                    // Need to adjust top position of last view to account for new row
                    int left = lastView.getLeft() - getPaddingLeft();
                    int top = firstViewInLastRow.getTop() + firstViewInLastRow.getHeight() + getDecoratedBottom(firstViewInLastRow);
                    int right = lastView.getRight() - getPaddingRight();
                    int bottom = top + lastView.getHeight();
                    layoutDecorated(lastView, left, top, right, bottom);
                }
            }
        }
    }
}
