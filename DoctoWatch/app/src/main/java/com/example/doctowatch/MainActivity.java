package com.example.doctowatch;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;

public class MainActivity extends AppCompatActivity {

    AccountAuthParams authParams;
    AccountAuthService service;
    Button login;
    Task<AuthAccount> task;

    private AccountAuthService mAuthService;
    private AccountAuthParams mAuthParam;
    // Define the request code for signInIntent.
    private static final int REQUEST_CODE_SIGN_IN = 1000;
    // Define the log tag.
    private static final String TAG = "Account";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //login = (Button) findViewById(R.id.login);


        /*authParams = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM).setEmail().setAuthorizationCode().createParams();
        service = AccountAuthManager.getService(MainActivity.this, authParams);
        task = service.silentSignIn();*/

        /*task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
            @Override
            public void onSuccess(AuthAccount authAccount) {
                // Obtain the user's ID information.
                Log.i(TAG, "displayName:" + authAccount.getDisplayName());
                // Obtain the ID type (0: HUAWEI ID; 1: AppTouch ID).
                Log.i(TAG, "accountFlag:" + authAccount.getAccountFlag());
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // The sign-in failed. Try to sign in explicitly using getSignInIntent().
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    Log.i(TAG, "sign failed status:" + apiException.getStatusCode());
                }
            }
        });*/


        findViewById(R.id.login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignInUsingHwId();
            }
        });
    }

    private void SignInUsingHwId() {
        // If your app needs to obtain the user's email address, call setEmail() , similarly you  need to acess ID token or authorisation code setIDToken() and setAuthorisationCode()
        mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setEmail()
                .createParams();
        mAuthService = AccountAuthManager.getService(this, mAuthParam);
        Intent signInIntent = mAuthService.getSignInIntent();
        // startActivityForResult() method, we can get result from another activity
        startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
    }

    /*public void openpdfAct() {
        Intent intent = new Intent(this, pdfAct.class);
        startActivity(intent);
    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // Process the authorization result to obtain the authorization code from AuthAccount.
        super.onActivityResult(requestCode, resultCode, data);
       /* if (requestCode == 8888) {
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                Toast.makeText(getApplicationContext(),"Successfully Logged In!",Toast.LENGTH_SHORT).show();
                //openpdfAct();
                SignInUsingHwId();
                // The sign-in is successful, and the user's ID information and authorization code are obtained.
                AuthAccount authAccount = authAccountTask.getResult();
                Log.i(TAG, "serverAuthCode:" + authAccount.getAuthorizationCode());
                Log.i(TAG, "serverAuthCode:" + authAccount.getDisplayName());
                Log.i(TAG, "serverAuthCode:" + authAccount.getEmail());
            } else {
                // The sign-in failed.
                Log.e(TAG, "sign in failed:" + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
    }
*/

        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Log.i(TAG, "onActivitResult of sigInInIntent, request code: " + REQUEST_CODE_SIGN_IN);
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                // The sign-in is successful, and the authAccount object that contains the HUAWEI ID information is obtained.
                AuthAccount authAccount = authAccountTask.getResult();
                Log.i(TAG, "display name:" + authAccount.getDisplayName());
                Log.i(TAG, "photo uri string:" + authAccount.getAvatarUriString());
                Log.i(TAG, "photo uri:" + authAccount.getAvatarUri());
                Log.i(TAG, "email:" + authAccount.getEmail());
                Log.i(TAG, "openid:" + authAccount.getOpenId());
                Log.i(TAG, "unionid:" + authAccount.getAuthorizationCode());
                Log.i(TAG, "agerange:" + authAccount.getAgeRange());

// when the user login sucesfully , i will get all the details and i am passing all to the next activity via the intent .
                Intent intent = new Intent(MainActivity.this, pdfAct.class);
                intent.putExtra("name", authAccount.getDisplayName());
                intent.putExtra("email", authAccount.getEmail());
                intent.putExtra("photo_uri", authAccount.getAvatarUriString());
                intent.putExtra("auth", authAccount.getAuthorizationCode());

                startActivity(intent);
            } else {
                // The sign-in fails. Find the failure cause from the status code. For more information, please refer to the "Error Codes" section in the API Reference.
                Log.e(TAG, "sign in failed : " + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }

    }
}