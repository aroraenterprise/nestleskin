package com.sajarora.skintracker;

import android.app.Activity;
import android.util.Log;

import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataService;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthResultHolder;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by sajarora on 3/10/17.
 */

public class SajHealthService {
    private static final String TAG = MainApplication.class.getSimpleName();
    private HealthDataStore mStore;
    Set<HealthPermissionManager.PermissionKey> mKeySet;
    private static SajHealthService mInstance;
    private Activity mActivity;

    private SajHealthService(){
        mInstance = this;
    }

    static SajHealthService initialize(Activity activity){
        if (mInstance == null){
            mInstance = new SajHealthService();
            mInstance.initializeSHealth(activity);
        }
        return mInstance;
    }

    public static SajHealthService getInstance() {
        assert mInstance != null;
        return mInstance;
    }

    public static HealthDataStore getStore(){
        assert mInstance != null;
        return mInstance.mStore;
    }

    private void initializeSHealth(Activity activity){
        if (mStore != null){
            return;
        }
        mActivity = activity;
        mKeySet = new HashSet<>();
        mKeySet.add(new HealthPermissionManager.PermissionKey(HealthConstants.StepCount.HEALTH_DATA_TYPE,
                HealthPermissionManager.PermissionType.READ));
        HealthDataService healthDataService = new HealthDataService();
        try {
            healthDataService.initialize(mActivity);
            mStore = new HealthDataStore(mActivity, mConnectionListener);
            mStore.connectService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HealthResultHolder.ResultListener<HealthPermissionManager.PermissionResult> mPermissionListener =
            new HealthResultHolder.ResultListener<HealthPermissionManager.PermissionResult>() {
        @Override
        public void onResult(HealthPermissionManager.PermissionResult permissionResult) {
            Log.d(TAG, "Permission callback recieved");
            Map<HealthPermissionManager.PermissionKey, Boolean> resultMap = permissionResult.getResultMap();
            if (resultMap.containsValue(Boolean.FALSE)) {
                //show snackbar
            } else {
                // get steps...etc.
            }
        }
    };

    private final HealthDataStore.ConnectionListener mConnectionListener = new HealthDataStore.ConnectionListener() {
        @Override
        public void onConnected() {
            Log.d(TAG, "Health service connected");
            HealthPermissionManager pmsManager = new HealthPermissionManager(mStore);

            try {
                Map<HealthPermissionManager.PermissionKey, Boolean> resultMap = pmsManager.isPermissionAcquired(mKeySet);
                if (resultMap.containsValue(Boolean.FALSE)){
                    pmsManager.requestPermissions(mKeySet, mActivity).setResultListener(mPermissionListener);
                }
            } catch (Exception e) {
                Log.d(TAG, "Failed to get permissions");
            }
        }

        @Override
        public void onConnectionFailed(HealthConnectionErrorResult healthConnectionErrorResult) {
            Log.d(TAG, "Health service not available");
        }

        @Override
        public void onDisconnected() {
            Log.d(TAG, "Health Service disconnected");
        }
    };


    public void destroy() {
        mStore.disconnectService();
        mInstance = null;
    }
}
