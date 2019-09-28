package com.example.myfirstapp;

public enum ModelObject {

    VISUALIZER(R.string.visualizer, R.layout.fragment_visualizer),
    ARABIC(R.string.arabic, R.layout.fragment_arabic),
    TRANSLITERATION(R.string.transliteration, R.layout.fragment_transliteration);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}