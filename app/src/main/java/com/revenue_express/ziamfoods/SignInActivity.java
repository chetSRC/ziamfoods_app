package com.revenue_express.ziamfoods;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener{

    EditText editName,editPassword;
    String msg,Name,Password;

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final int RC_SIGN_IN = 007;


    TextView tvRegister,tvSkip;
    private final String serverUrl= "http://www2.ziamthai.com/index.php/api/accounts/login";
    String API_Key = "sd23g125sdf1gc10b3yhik58l4o4";

    private GoogleApiClient mGoogleApiClient;
    private ProgressDialog mProgressDialog;

//    private SignInButton btnSignIn;
//    private Button btnSignOut;

    Button fb,btnSignIn;
    LoginButton loginButton;
    CallbackManager callbackManager;
    AccessTokenTracker accessTokenTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_sign_in);
        setInitView();

        tvRegister = (TextView)findViewById(R.id.tv_Register);
        tvSkip = (TextView)findViewById(R.id.skip);

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
                startActivity(intent);
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Name = editName.getText().toString();
                Password = editPassword.getText().toString();
                if(Name.equals("") || Password.equals("")){
                    Toast.makeText(SignInActivity.this, "Username or password must be filled", Toast.LENGTH_LONG).show();
                    return;
                }
                if(Name.length() <= 1 || Password.length() <= 1){
                    Toast.makeText(SignInActivity.this, "Username or password length must be greater than one", Toast.LENGTH_LONG).show();
                    return;
                }

                // request authentication with remote server4
                AsyncDataClass asyncRequestObject = new AsyncDataClass();
                asyncRequestObject.execute(serverUrl, Name, Password, API_Key);
            }

        });

//        btnSignOut = (Button) findViewById(btn_sign_out);
//        btnSignOut.setOnClickListener(this);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        fb = (Button) findViewById(R.id.fb);
        loginButton = (LoginButton) findViewById(R.id.login_button);

        loginButton.setReadPermissions(Arrays.asList("public_profile, email, user_birthday, user_friends"));
        callbackManager = CallbackManager.Factory.create();

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {


                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {


                                try {
                                    String id = object.getString("id");
                                    String name = object.getString("name");
//                                    tv_profile_name.setText(name);
                                    String firstname = name;

                                    String imageurl = "https://graph.facebook.com/" + id + "/picture?type=large";
                                    String pic = imageurl;
//                                    Picasso.with(LoginAppActivity.this).load(imageurl).into(iv_profile_pic);

                                    String aaa = object.getString("name");

                                    Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
                                    intent.putExtra("name",firstname);
                                    intent.putExtra("pic",pic);
                                    startActivity(intent);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        });


                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday");
                request.setParameters(parameters);
                request.executeAsync();

                /**
                 * AccessTokenTracker to manage logout
                 */
                accessTokenTracker = new AccessTokenTracker() {
                    @Override
                    protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken,
                                                               AccessToken currentAccessToken) {
                        if (currentAccessToken == null) {
                            Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
                            intent.putExtra("name", "false");
                            intent.putExtra("pic","false");
                            startActivity(intent);
                        }
                    }
                };
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });

    }


    public void logout() {

        LoginManager.getInstance().logOut();

        if(mGoogleApiClient != null) {
            if (mGoogleApiClient.isConnected()) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                updateUI(false);
            }
        }
    }

    @Override
    public void onClick(View v) {
        if (v == fb) {
            loginButton.performClick();
        }
        int id = v.getId();

        switch (id) {
//            case R.id.btn_sign_in:
//                signIn();
//                break;

//            case R.id.btn_sign_out:
//                logout();
//                break;
        }
    }

    private void setInitView(){
        editName=(EditText)findViewById(R.id.editName);
        editPassword=(EditText)findViewById(R.id.editPassword);
        btnSignIn = (Button) findViewById(R.id.btnSignIn);
        tvRegister = (TextView)findViewById(R.id.tv_Register);
    }

    private void checkLogin() {

    }

    private void debugHashKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.revenue_express.ziamfoods",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }

    //-----Login user general-----//
    private class AsyncDataClass extends AsyncTask<String, Void, String> {

        @Override

        protected String doInBackground(String... params) {

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);
            HttpClient httpClient = new DefaultHttpClient(httpParameters);
            HttpPost httpPost = new HttpPost(params[0]);
            String jsonResult = "";

            try {
                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
                System.out.println("Email Value: " +  params[1]);
                nameValuePairs.add(new BasicNameValuePair("m_user", params[1]));
                nameValuePairs.add(new BasicNameValuePair("m_pass", params[2]));
                nameValuePairs.add(new BasicNameValuePair("api_key", params[3]));
                httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
                HttpResponse response = httpClient.execute(httpPost);
                jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return jsonResult;
        }

        @Override

        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override

        protected void onPostExecute(String result) {

            super.onPostExecute(result);
            System.out.println("Resulted Value: " + result);

            if(result.equals("") || result == null){
                Toast.makeText(SignInActivity.this, "Server connection failed", Toast.LENGTH_LONG).show();
                return;
            }

            int jsonResult = returnParsedJsonObject(result);

            if(jsonResult == 0){
                msg = "Invalid username or password";
                showLocationDialog(msg);
                return;
            }
            if(jsonResult == 1){
                String enteredEmail = "";
                Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
                intent.putExtra("name",enteredEmail);
                String pic = "";
                intent.putExtra("pic",pic);
                startActivity(intent);
            }
        }

        private StringBuilder inputStreamToString(InputStream is) {

            String rLine = "";
            StringBuilder answer = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            try {

                while ((rLine = br.readLine()) != null) {
                    answer.append(rLine);
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return answer;
        }
    }

    private int returnParsedJsonObject(String result){

        JSONObject resultObject = null;
        int returnedResult = 0;

        try {
//            System.out.println("Result Array: "+result);
//            resultObject = new JSONObject(result);
            if(result != null) {
//              returnedResult = resultObject.getInt("success");
                returnedResult = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnedResult;
    }
    //-----End login user general-----//

    //-----Login user Google+-----//
    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    private void handleSignInResult(GoogleSignInResult result) {
        Log.d(TAG, "handleSignInResult:" + result.isSuccess());
        if (result.isSuccess()) {
            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount acct = result.getSignInAccount();

            Log.e(TAG, "display name: " + acct.getDisplayName());

            String personName = acct.getDisplayName();
            String personPhotoUrl = acct.getPhotoUrl().toString();
            String email = acct.getEmail();

            Log.e(TAG, "Name: " + personName + ", email: " + email
                    + ", Image: " + personPhotoUrl);

            updateUI(true);

            Intent intent = new Intent(getApplicationContext(), MainAppActivity.class);
            intent.putExtra("name",personName);
            intent.putExtra("pic",personPhotoUrl);
            startActivity(intent);

        } else {
            updateUI(false);
        }
    }

    public void check(){
        signOut();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(mGoogleApiClient);
        if (opr.isDone()) {
            // If the user's cached credentials are valid, the OptionalPendingResult will be "done"
            // and the GoogleSignInResult will be available instantly.
            Log.d(TAG, "Got cached sign-in");
            GoogleSignInResult result = opr.get();
            handleSignInResult(result);
        } else {
            // If the user has not previously signed in on this device or the sign-in has expired,
            // this asynchronous branch will attempt to sign in the user silently.  Cross-device
            // single sign-on will occur in this branch.
            showProgressDialog();
            opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    hideProgressDialog();
                    handleSignInResult(googleSignInResult);
                }
            });
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
    }

    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("loading");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
//            btnSignOut.setVisibility(View.VISIBLE);
        } else {
//            btnSignOut.setVisibility(View.GONE);
        }
    }
    //-----End login user Google+-----//

    //-----Show dialog box-----//
    private void showLocationDialog(String msg) {
        AlertDialog.Builder builder = new AlertDialog.Builder(SignInActivity.this);
        builder.setTitle("Login ZiamFoods");
        builder.setMessage(msg);

        String positiveText = getString(android.R.string.ok);
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

        AlertDialog dialog = builder.create();
        // Display dialog
        dialog.show();
    }
    //-----End show dialog box-----//

}

