package com.daneja.popularmovies;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Reference: http://stackoverflow.com/questions/28531996/android-recyclerview-gridlayoutmanager-column-spacing
 * Needed a way to space out grid cells.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SpacesItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view,
                               RecyclerView parent, RecyclerView.State state) {
        outRect.left = space/2;
        outRect.right=space/2;
         outRect.top = space;
        }
    }

