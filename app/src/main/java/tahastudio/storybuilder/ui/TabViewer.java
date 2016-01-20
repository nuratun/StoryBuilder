package tahastudio.storybuilder.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tahastudio.storybuilder.fragments.AddCharacters;
import tahastudio.storybuilder.fragments.AddLocations;
import tahastudio.storybuilder.fragments.AddEvents;

/**
 * This class allows flipping through fragments as tabs
 * Initialized from: ShowStory
 */
public class TabViewer extends FragmentStatePagerAdapter {
    int sbTabs;

    public TabViewer(FragmentManager fm, int nmTabs) {
        super(fm);
        this.sbTabs = nmTabs;
    }

    // TODO -> Check into releasing tabs to save memory
    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                AddCharacters sbTab1 = new AddCharacters();
                return sbTab1;
            case 1:
                AddLocations sbTab2 = new AddLocations();
                return sbTab2;
            case 2:
                AddEvents sbTab3 = new AddEvents();
                return sbTab3;
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