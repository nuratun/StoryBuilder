package tahastudio.storybuilder;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class TabViewer extends FragmentStatePagerAdapter {
    int sbTabs;

    public TabViewer(FragmentManager fm, int sbTabs) {
        super(fm);
        this.sbTabs = sbTabs;
    }

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
