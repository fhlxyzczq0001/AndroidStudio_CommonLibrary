package org.byteam.superadapter;

import android.view.View;

import java.util.List;

/**
 * OnItemClickListener for RecyclerView.
 * <p>
 * Created by Cheney on 16/1/13.
 */
public interface OnItemClickListener<T> {
    void onItemClick(View itemView, int viewType, int position, List<T> mData );
}
