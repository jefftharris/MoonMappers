package org.cosmoquest.moonmappers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class LoginFragment extends Fragment
    implements View.OnClickListener
{
    private View itsView;

    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        itsView = inflater.inflate(R.layout.fragment_login, container, false);

        Button login = (Button)itsView.findViewById(R.id.login);
        login.setOnClickListener(this);

        return itsView;
    }

    public void onClick(View v)
    {

    }
}
