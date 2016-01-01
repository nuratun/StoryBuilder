package tahastudio.storybuilder.ui;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import tahastudio.storybuilder.fragments.AddCharacters;
import tahastudio.storybuilder.fragments.AddPlaces;
import tahastudio.storybuilder.fragments.AddPlotline;

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
                AddPlaces sbTab2 = new AddPlaces();
                return sbTab2;
            case 2:
                AddPlotline sbTab3 = new AddPlotline();
                return sbTab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return sbTabs;
    }

}