package com.shit.nestedlistrecyclerview.list;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.shit.nestedlistrecyclerview.R;
import com.shit.nestedlistrecyclerview.list.data.BaseItem;
import com.shit.nestedlistrecyclerview.list.data.DataProvider;
import com.shit.nestedlistrecyclerview.list.views.LevelBeamView;

import java.util.List;

import pl.openrnd.multilevellistview.ItemInfo;
import pl.openrnd.multilevellistview.MultiLevelListAdapter;
import pl.openrnd.multilevellistview.MultiLevelListView;
import pl.openrnd.multilevellistview.NestType;
import pl.openrnd.multilevellistview.OnItemClickListener;

public class DataActivity extends AppCompatActivity {

    private MultiLevelListView mListView;
    private Switch mMultipliedExpandingView;
    private Switch mAlwaysExpandedView;

    private boolean mAlwaysExpandend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);

        confViews();
    }

    private void confViews() {
        setContentView(R.layout.activity_data);

        mListView = (MultiLevelListView) findViewById(R.id.listView);
        mMultipliedExpandingView = (Switch) findViewById(R.id.multipledExpanding);
        mAlwaysExpandedView = (Switch) findViewById(R.id.alwaysExpanded);

        mMultipliedExpandingView.setOnCheckedChangeListener(mOnCheckedChangeListener);
        mAlwaysExpandedView.setOnCheckedChangeListener(mOnCheckedChangeListener);

        setAlwaysExpanded(mAlwaysExpandedView.isChecked());
        setMultipleExpanding(mMultipliedExpandingView.isChecked());

        ListAdapter listAdapter = new ListAdapter();

        mListView.setAdapter(listAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);

        listAdapter.setDataItems(DataProvider.getInitialItems());
    }

    private void setAlwaysExpanded(boolean alwaysExpanded) {
        mAlwaysExpandend = alwaysExpanded;
        mListView.setAlwaysExpanded(alwaysExpanded);
    }

    private void setMultipleExpanding(boolean multipleExpanding) {
        mListView.setNestType(multipleExpanding ? NestType.MULTIPLE : NestType.SINGLE);
    }

    private CompoundButton.OnCheckedChangeListener mOnCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

            int id = buttonView.getId();

            if (id == R.id.multipledExpanding){
                setMultipleExpanding(isChecked);
            }else if (id == R.id.alwaysExpanded){
                setAlwaysExpanded(isChecked);
            }


        }
    };

    private OnItemClickListener mOnItemClickListener = new OnItemClickListener() {

        private void showItemDescription(Object object, ItemInfo itemInfo) {
            StringBuilder builder = new StringBuilder("\"");
            builder.append(((BaseItem) object).getName());
            builder.append("\" clicked!\n");
            builder.append(getItemInfoDsc(itemInfo));

            Toast.makeText(DataActivity.this, builder.toString(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }

        @Override
        public void onGroupItemClicked(MultiLevelListView parent, View view, Object item, ItemInfo itemInfo) {
            showItemDescription(item, itemInfo);
        }
    };

    private class ListAdapter extends MultiLevelListAdapter {

        private class ViewHolder {
            TextView nameView;
            TextView infoView;
            ImageView arrowView;
            LevelBeamView levelBeamView;
        }

        @Override
        public List<?> getSubObjects(Object object) {
            return DataProvider.getSubItems((BaseItem) object);
        }

        @Override
        public boolean isExpandable(Object object) {
            return DataProvider.isExpandable((BaseItem) object);
        }

        @Override
        public View getViewForObject(Object object, View convertView, ItemInfo itemInfo) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(DataActivity.this).inflate(R.layout.data_item, null);
                viewHolder.infoView = (TextView) convertView.findViewById(R.id.dataItemInfo);
                viewHolder.nameView = (TextView) convertView.findViewById(R.id.dataItemName);
                viewHolder.arrowView = (ImageView) convertView.findViewById(R.id.dataItemArrow);
                viewHolder.levelBeamView = (LevelBeamView) convertView.findViewById(R.id.dataItemLevelBeam);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.nameView.setText(((BaseItem) object).getName());
            viewHolder.infoView.setText(getItemInfoDsc(itemInfo));

            if (itemInfo.isExpandable() && !mAlwaysExpandend) {
                viewHolder.arrowView.setVisibility(View.VISIBLE);
                viewHolder.arrowView.setImageResource(itemInfo.isExpanded() ?
                        R.drawable.ic_keyboard_arrow_up_black_24dp : R.drawable.ic_keyboard_arrow_down_black_24dp);
            } else {
                viewHolder.arrowView.setVisibility(View.GONE);
            }

            viewHolder.levelBeamView.setLevel(itemInfo.getLevel());

            return convertView;
        }
    }

    private String getItemInfoDsc(ItemInfo itemInfo) {
        StringBuilder builder = new StringBuilder();

        builder.append(String.format("level[%d], idx in level[%d/%d]",
                itemInfo.getLevel() + 1, /*Indexing starts from 0*/
                itemInfo.getIdxInLevel() + 1 /*Indexing starts from 0*/,
                itemInfo.getLevelSize()));

        if (itemInfo.isExpandable()) {
            builder.append(String.format(", expanded[%b]", itemInfo.isExpanded()));
        }
        return builder.toString();
    }
}