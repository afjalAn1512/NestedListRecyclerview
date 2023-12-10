package com.shit.nestedlistrecyclerview.list.data;

public class GroupItem extends BaseItem{

    private int mLevel;

    public GroupItem(String name) {
        super(name);
        mLevel = 0;
    }

    public void setLevel(int level){
        mLevel = level;
    }

    public int getLevel(){
        return mLevel;
    }
}
