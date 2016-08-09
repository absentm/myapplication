package com.example.dm.myapplication.customviews.ninegridview;

import android.util.SparseArray;
import android.view.View;

/**
 * Created by dm on 16-4-8.
 * 九宫格万能BaseViewHolder
 */
public class NineBaseViewHolder {
    public static <T extends View> T get(View view, int id) {
        SparseArray<View> viewHolder = (SparseArray<View>) view.getTag();
        if (viewHolder == null) {
            viewHolder = new SparseArray<View>();
            view.setTag(viewHolder);
        }

        View childView = viewHolder.get(id);
        if (childView == null) {
            childView = view.findViewById(id);
            viewHolder.put(id, childView);
        }

        return (T) childView;
    }
}
