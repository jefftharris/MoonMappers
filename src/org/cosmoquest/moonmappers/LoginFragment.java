package org.cosmoquest.moonmappers;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

public class LoginFragment extends Fragment
    implements View.OnClickListener
{
    private static String TAG = "LoginFragment";

    private View itsView;
    private TextView itsUser;
    private TextView itsPassword;
    private Button itsLogin;
    private AsyncTask<LoginInfo, Void, LoginResult> itsTask;
    private ActivityProvider itsActProvider;

    public static class LoginResult
    {
        public final String itsUser;
        public final Throwable itsError;

        public LoginResult(String user, Throwable error)
        {
            itsUser = user;
            itsError = error;
        }
    }

    public interface ActivityProvider
    {
        public void handleLoginComplete(LoginResult result);
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
        itsView = inflater.inflate(R.layout.fragment_login, container, false);

        itsUser = (TextView)itsView.findViewById(R.id.user);
        itsPassword = (TextView)itsView.findViewById(R.id.password);

        itsLogin = (Button)itsView.findViewById(R.id.login);
        itsLogin.setOnClickListener(this);

        TextWatcher loginWatcher = new TextWatcher()
        {
            public void afterTextChanged(Editable s)
            {
                boolean haveValues =
                    (itsUser.length() > 0) && (itsPassword.length() > 0);
                itsLogin.setEnabled(haveValues);
            }

            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after)
            {
            }

            public void onTextChanged(CharSequence s, int start,
                                      int before, int count)
            {
            }
        };
        itsUser.addTextChangedListener(loginWatcher);
        itsPassword.addTextChangedListener(loginWatcher);
        loginWatcher.afterTextChanged(null);

        return itsView;
    }

    public void onClick(View v)
    {
        InputMethodManager imm =
            (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(itsView.getWindowToken(), 0);

        View progress = itsView.findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        View login = itsView.findViewById(R.id.login);
        login.setEnabled(false);

        if (itsTask != null) {
            itsTask.cancel(true);
        }

        View error = itsView.findViewById(R.id.error);
        error.setVisibility(View.GONE);
        LoginInfo info = new LoginInfo(itsUser.getText().toString(),
                                       itsPassword.getText().toString());
        itsTask = new LoginTask().execute(info);
        // TODO: handle onPause and resume
        // TODO: handle screen rotates
    }

    private void loginComplete(LoginResult result)
    {
        Log.i(TAG, "loginComplete: " + result.itsError);

        View progress = itsView.findViewById(R.id.progress);
        progress.setVisibility(View.GONE);
        View login = itsView.findViewById(R.id.login);
        login.setEnabled(true);

        if (result.itsError == null) {
            itsActProvider.handleLoginComplete(result);
        } else {
            TextView tv = (TextView)itsView.findViewById(R.id.error);
            String error = getString(R.string.login_error,
                                     result.itsError.toString());
            tv.setText(error);
            tv.setVisibility(View.VISIBLE);
        }
        itsTask = null;
    }

    private static class LoginInfo
    {
        public final String itsUser;
        public final String itsPassword;

        public LoginInfo(String user, String password)
        {
            itsUser = user;
            itsPassword = password;
        }
    }

    private class LoginTask extends AsyncTask<LoginInfo, Void, LoginResult>
    {
        @Override
        protected LoginResult doInBackground(LoginInfo... params)
        {
            LoginInfo info = params[0];
            Log.i(TAG, "doInBackground user: " + info.itsUser +
                  ", pass: " + info.itsPassword);
            try {
                Thread.sleep(5000);
                if (info.itsPassword.equals("bad")) {
                    throw new Exception("Bad password");
                }
                return new LoginResult(info.itsUser, null);
            }
            catch (Throwable e) {
                return new LoginResult(info.itsUser, e);
            }
        }

        @Override
        protected void onPostExecute(LoginResult result)
        {
            if (isCancelled()) {
                return;
            }
            loginComplete(result);
        }
    }
}
