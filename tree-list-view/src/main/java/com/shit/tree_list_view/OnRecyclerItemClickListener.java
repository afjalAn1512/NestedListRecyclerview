package com.shit.tree_list_view;

import android.view.View;

import com.shit.tree_list_view.models.RecyclerViewItem;

public interface OnRecyclerItemClickListener {

    void onItemClick(View view, RecyclerViewItem item, int position);

}
