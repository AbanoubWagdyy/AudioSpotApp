package com.audiospotapplication.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.facebook.*;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.share.widget.ShareDialog;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Collection;

/**
 * FacebookHelper.java
 */
public class FacebookHelper {

    private Collection<String> permissions = Arrays.asList("public_profile ", "email");
    private CallbackManager callbackManager;
    private LoginManager loginManager;
    private ShareDialog shareDialog;
    private AppCompatActivity activity;
    private Fragment fragment;
    private OnFbSignInListener fbSignInListener;

    public void logout() {
        loginManager.logOut();
    }

    /**
     * Interface to listen the Facebook login
     */
    public interface OnFbSignInListener {
        void OnFbSignInComplete(GraphResponse graphResponse, String error);
    }

    public FacebookHelper(AppCompatActivity activity, OnFbSignInListener fbSignInListener) {
        this.activity = activity;
        this.fbSignInListener = fbSignInListener;
    }

    public FacebookHelper(Fragment fragment, OnFbSignInListener fbSignInListener) {
        this.fragment = fragment;
        this.fbSignInListener = fbSignInListener;
    }

    public FacebookHelper(Activity activity) {
        shareDialog = new ShareDialog(activity);
    }

    public FacebookHelper(Fragment fragment) {
        shareDialog = new ShareDialog(fragment);
    }

    public void connect() {
        callbackManager = CallbackManager.Factory.create();
        loginManager = LoginManager.getInstance();
        if (activity != null)
            loginManager.logInWithReadPermissions(activity, permissions);
        else
            loginManager.logInWithReadPermissions(fragment, permissions);

        loginManager.registerCallback(callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        if (loginResult != null) {
                            callGraphAPI(loginResult.getAccessToken());
                        }
                    }

                    @Override
                    public void onCancel() {
                        fbSignInListener.OnFbSignInComplete(null, "User cancelled.");
                    }

                    @Override
                    public void onError(FacebookException exception) {
                        if (exception instanceof FacebookAuthorizationException) {
                            if (AccessToken.getCurrentAccessToken() != null) {
                                LoginManager.getInstance().logOut();
                            }
                        }
                        fbSignInListener.OnFbSignInComplete(null, exception.getMessage());
                    }
                });

    }

    private void callGraphAPI(AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        fbSignInListener.OnFbSignInComplete(response,null);
                    }
                });
        Bundle parameters = new Bundle();
        //Explicitly we need to specify the fields to get values else some values will be null.
        parameters.putString("fields", "id,email,first_name,last_name");
        request.setParameters(parameters);
        request.executeAsync();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (callbackManager != null)
            callbackManager.onActivityResult(requestCode, resultCode, data);
    }
}
