package org.cosmoquest.moonmappers;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;

public class MainFragment extends Fragment
{
    private View itsView;
    private ActivityProvider itsActProvider;

    private int[] itsChoices = new int[]
        { R.id.crater_perfect, R.id.crater_worn, R.id.crater_wear_tear,
          R.id.crater_none };
    private int itsCurrChoice = -1;
    // TODO: save/restore curr choice in bundle


    public interface ActivityProvider
    {
        public String getCurrUser();
    }

    public static MainFragment newInstance()
    {
        MainFragment frag = new MainFragment();
        Bundle args = new Bundle();
        frag.setArguments(args);
        return frag;
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
     */
    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        itsActProvider = (ActivityProvider)activity;
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
     */
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState)
    {
        itsView = inflater.inflate(R.layout.fragment_main, container, false);

        setButtonImg(itsView, R.drawable.crater_perfect, R.id.crater_perfect);
        setButtonImg(itsView, R.drawable.crater_worn, R.id.crater_worn);
        setButtonImg(itsView, R.drawable.crater_wear_tear, R.id.crater_wear_tear);
        setButtonImg(itsView, R.drawable.crater_none, R.id.crater_none);

        final Button submit = (Button)itsView.findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                submit();
            }
        });

        View.OnClickListener rbListener = new View.OnClickListener()
        {
            public void onClick(View v)
            {
                CompoundButton currBtn = (CompoundButton)v;
                itsCurrChoice = currBtn.getId();
                for (int id : itsChoices) {
                    if (id != itsCurrChoice) {
                        CompoundButton btn =
                            (CompoundButton)itsView.findViewById(id);
                        btn.setChecked(false);
                    }
                }
                submit.setEnabled(true);
            }
        };
        for (int id : itsChoices) {
            View btn = itsView.findViewById(id);
            btn.setOnClickListener(rbListener);
        }

        TextView tv = (TextView)itsView.findViewById(R.id.user);
        String str = getString(R.string.user, itsActProvider.getCurrUser());
        tv.setText(str);
        return itsView;
    }

    private void submit()
    {

    }

    private void setButtonImg(View root, int drawId, int buttonId)
    {
        Drawable d = getResources().getDrawable(drawId);
        d.setBounds(0, 0, (int)(d.getIntrinsicWidth()*0.25),
                    (int)(d.getIntrinsicHeight()*0.25));
        ScaleDrawable sd = new ScaleDrawable(d, 0, 1.0f, 1.0f);
        Button btn = (Button)root.findViewById(buttonId);
        btn.setCompoundDrawables(sd.getDrawable(), null, null, null);
    }
}
