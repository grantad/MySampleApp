package com.amazonaws.mobilehelper.auth.signin;
//
// Copyright 2017 Amazon.com, Inc. or its affiliates (Amazon). All Rights Reserved.
//
// Code generated by AWS Mobile Hub. Amazon gives unlimited permission to 
// copy, distribute and modify it.
//
// Source code generated from template: aws-my-sample-app-android v0.16
//

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v4.BuildConfig;
import android.view.View;

import com.amazonaws.mobilehelper.auth.IdentityManager;
import com.amazonaws.mobilehelper.auth.IdentityProvider;
import com.amazonaws.mobilehelper.auth.IdentityProviderType;
import com.amazonaws.mobilehelper.auth.SignInResultHandler;
import com.amazonaws.mobilehelper.util.ThreadUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * The SignInManager is a singleton component, which orchestrates sign-in and sign-up flows. It is responsible for
 * discovering the previously signed-in provider and that provider's credentials, as well as initializing sign-in
 * buttons with the providers.
 */
public class SignInManager {
    private static final String LOG_TAG = SignInManager.class.getSimpleName();
    private final Map<IdentityProviderType, SignInProvider> signInProviders = new HashMap<>();
    private volatile static SignInManager singleton = null;
    private final IdentityManager identityManager;
    private volatile SignInResultHandler signInResultHandler;

    /**
     * Constructor.
     */
    private SignInManager(final Context context, final IdentityManager identityManager) {
        if (BuildConfig.DEBUG && singleton != null) {
            throw new AssertionError();
        }

        for (Class<? extends SignInProvider> providerClass : identityManager.getSignInProviderClasses()) {
            final SignInProvider provider;
            try {
                provider = providerClass.newInstance();
            } catch (final IllegalAccessException ex) {
                throw new RuntimeException(ex);
            } catch (final InstantiationException ex) {
                throw new RuntimeException(ex);
            }
            provider.initialize(context, identityManager.getHelperConfiguration());

            addSignInProvider(provider);
        }

        singleton = this;
        this.identityManager = identityManager;
    }

    /**
     * Gets the singleton instance of this class.
     * @return instance
     */
    public synchronized static SignInManager getInstance() {
        return singleton;
    }

    /**
     * Gets the singleton instance of this class.
     * @return instance
     */
    public synchronized static SignInManager getInstance(final Context context, @NonNull final IdentityManager identityManager) {
        if (singleton == null) {
            singleton = new SignInManager(context, identityManager);
        }
        return singleton;
    }

    public void setResultHandler(final SignInResultHandler signInResultHandler) {
        this.signInResultHandler = signInResultHandler;
    }

    public SignInResultHandler getResultHandler() {
        return signInResultHandler;
    }

    /**
     * @return the identityManager used by this SignInManager.
     */
    public IdentityManager getIdentityManager() {
        return identityManager;
    }

    public synchronized static void dispose() {
        singleton = null;
    }

    /**
     * Adds a sign-in identity provider.
     * @param signInProvider sign-in provider
     */
    public void addSignInProvider(final SignInProvider signInProvider) {
        signInProviders.put(signInProvider.getProviderType(), signInProvider);
    }

    /**
     * Call getPreviouslySignedInProvider to determine if the user was left signed-in when the app
     * was last running.  This should be called on a background thread since it may perform file
     * i/o.  If the user is signed in with a provider, this will return the provider for which the
     * user is signed in.  Subsequently, refreshCredentialsWithProvider should be called with the
     * provider returned from this method.
     * @return false if not already signed in, true if the user was signed in with a provider.
     */
    public SignInProvider getPreviouslySignedInProvider() {

        for (final SignInProvider provider : signInProviders.values()) {
            // Note: This method may block. This loop could potentially be sped
            // up by running these calls in parallel using an executorService.
            if (provider.isUserSignedIn()) {
                return provider;
            }
        }
        return null;
    }

    private class SignInProviderResultsAdapter implements SignInProviderResultsHandler {
        final private SignInProviderResultsHandler handler;
        final private Activity activity;

        private SignInProviderResultsAdapter(final Activity activity,
                                            final SignInProviderResultsHandler handler) {
            this.handler = handler;
            this.activity = activity;
        }

        private Activity getActivity() {
            return activity;
        }

        /** {@inheritDoc} */
        @Override
        public void onSuccess(final IdentityProvider provider) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler.onSuccess(provider);
                }
            });
        }

        /** {@inheritDoc} */
        @Override
        public void onCancel(final IdentityProvider provider) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler.onCancel(provider);
                }
            });
        }

        /** {@inheritDoc} */
        @Override
        public void onError(final IdentityProvider provider, final Exception ex) {
            ThreadUtils.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    handler.onError(provider, ex);
                }
            });
        }
    }

    private SignInProviderResultsAdapter resultsAdapter;

    /**
     * Refresh Cognito credentials with a provider.  Results handlers are always called on the main
     * thread.
     * @param activity the calling activity.
     * @param provider the sign-in provider that was previously signed in.
     * @param resultsHandler the handler to receive results for credential refresh.
     */
    public void refreshCredentialsWithProvider(final Activity activity,
                                               final SignInProvider provider,
                                               final SignInProviderResultsHandler resultsHandler) {

        if (provider == null) {
            throw new IllegalArgumentException("The sign-in provider cannot be null.");
        }

        if (provider.getToken() == null) {
            resultsHandler.onError(provider,
                new IllegalArgumentException("Given provider not previously logged in."));
        }

        resultsAdapter = new SignInProviderResultsAdapter(activity, resultsHandler);
        identityManager.setProviderResultsHandler(resultsAdapter);

        identityManager.federateWithProvider(provider);
    }

    /**
     * Sets the results handler results from sign-in with a provider. Results handlers are
     * always called on the UI thread.
     * @param activity the calling activity.
     * @param resultsHandler the handler for results from sign-in with a provider.
     */
    public void setProviderResultsHandler(final Activity activity,
                                          final SignInProviderResultsHandler resultsHandler) {
        resultsAdapter = new SignInProviderResultsAdapter(activity, resultsHandler);
        // Set the final results handler with the identity manager.
        identityManager.setProviderResultsHandler(resultsAdapter);
    }

    /**
     * Call initializeSignInButton to initialize the logic for sign-in for a specific provider.
     * @param providerType the SignInProvider class.
     * @param buttonView the view for the button associated with this provider.
     * @return the onClickListener for the button to be able to override the listener.
     */
    public View.OnClickListener initializeSignInButton(final IdentityProviderType providerType,
                                                       final View buttonView) {
        final SignInProvider provider = findProvider(providerType);

        // Initialize the sign in button with the identity manager's results adapter.
        return provider.initializeSignInButton(resultsAdapter.getActivity(),
            buttonView,
            identityManager.getResultsAdapter());
    }

    private SignInProvider findProvider(IdentityProviderType providerType) {

        final SignInProvider provider = signInProviders.get(providerType);

        if (provider == null) {
            throw new IllegalArgumentException("No such provider : " + providerType.name());
        }

        return provider;
    }

    /**
     * Handle the Activity result for login providers.
     * @param requestCode the request code.
     * @param resultCode the result code.
     * @param data result intent.
     * @return true if the sign-in manager handle the result, otherwise false.
     */
    public boolean handleActivityResult(final int requestCode, final int resultCode, final Intent data) {

        for (final SignInProvider provider : signInProviders.values()) {
            if (provider.isRequestCodeOurs(requestCode)) {
                provider.handleActivityResult(requestCode, resultCode, data);
                return true;
            }
        }

        return false;
    }
}
