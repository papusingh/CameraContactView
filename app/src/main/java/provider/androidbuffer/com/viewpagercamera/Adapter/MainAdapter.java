package provider.androidbuffer.com.viewpagercamera.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import provider.androidbuffer.com.viewpagercamera.Fragment.CameraFragment;
import provider.androidbuffer.com.viewpagercamera.Fragment.ContactFragment;

/**
 * Created by incred-dev on 30/8/18.
 */

public class MainAdapter extends FragmentStatePagerAdapter {

    private static final int NUM_ITEMS = 2;

    public MainAdapter(FragmentManager fm) {
        super(fm);
    }

    //Return the fragment to display the page
    @Override
    public Fragment getItem(int position) {
        switch (position){

            case 0:
                return CameraFragment.newInstance();

            case 1:
                return ContactFragment.newInstance();

            default:
                return null;
        }
    }

    //Return the title of the fragment
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "Camera";

            case 1:
                return "Contact";

            default:
                return "";
        }
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
