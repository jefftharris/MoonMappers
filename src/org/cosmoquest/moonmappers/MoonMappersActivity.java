package org.cosmoquest.moonmappers;

import org.cosmoquest.moonmappers.LoginFragment.LoginResult;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MoonMappersActivity extends FragmentActivity
    implements LoginFragment.ActivityProvider,
    MainFragment.ActivityProvider
{
    private String itsCurrUser;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moonmappers);
    }

    /* (non-Javadoc)
     * @see org.cosmoquest.moonmappers.LoginFragment.ActivityProvider#handleLoginComplete(org.cosmoquest.moonmappers.LoginFragment.LoginResult)
     */
    public void handleLoginComplete(LoginResult result)
    {
        itsCurrUser = result.itsUser;
        MainFragment frag = MainFragment.newInstance();
        replaceDetailsFragment(frag);
    }

    /* (non-Javadoc)
     * @see org.cosmoquest.moonmappers.MainFragment.ActivityProvider#getCurrUser()
     */
    public String getCurrUser()
    {
        return itsCurrUser;
    }


    private void replaceDetailsFragment(Fragment frag)
    {
        FragmentManager fragMgr = getSupportFragmentManager();
        FragmentTransaction ft = fragMgr.beginTransaction();
        ft.replace(R.id.main, frag);
        ft.addToBackStack(null);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
        ft.commit();
    }
}