package com.example.gabriel.app.movieapp;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Gabriel on 10/08/16.
 */

public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

    private int mSpace;

    public SpacesItemDecoration(int space){
        mSpace = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = mSpace;
        outRect.right = mSpace;
        outRect.bottom = mSpace;

        // Add top margin only for the first item to avoid double space between items
        if (parent.getChildLayoutPosition(view) == 0) {
            outRect.top = mSpace;
        } else {
            outRect.top = 0;
        }
    }
}
