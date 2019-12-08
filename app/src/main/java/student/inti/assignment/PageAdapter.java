package student.inti.assignment;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class PageAdapter extends FragmentPagerAdapter {
    private int numOnTabs;

    PageAdapter(FragmentManager fm,int numOnTabs){
        super(fm);
        this.numOnTabs=numOnTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new HomeFragment();
            case 1:
                return new CalendarFragment();
            case 2:
                return new ProfileFragment();
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return numOnTabs;
    }
}
