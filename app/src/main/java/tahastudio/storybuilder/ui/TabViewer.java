package tahastudio.storybuilder.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tahastudio.storybuilder.fragments.AddCharacters;
import tahastudio.storybuilder.fragments.AddLocations;
import tahastudio.storybuilder.fragments.AddEvents;

/**
 * This class allows flipping through fragments as tabs
 * Initialized from: Story
 */
public class TabViewer extends FragmentStatePagerAdapter {
    int sbTabs;

    public TabViewer(FragmentManager fm, int nmTabs) {
        super(fm);
        this.sbTabs = nmTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new AddCharacters();
            case 1:
                return new AddLocations();
            case 2:
                return new AddEvents();
            default:
                return null;
        }
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return sbTabs;
    }
}