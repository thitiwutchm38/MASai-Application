package com.example.bookthiti.masai2.mobileapplicationscanningscreen.scanresultscreen;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;
import com.example.bookthiti.masai2.database.MasaiViewModel;
import com.example.bookthiti.masai2.internet.MasaiServerAPI;
import com.example.bookthiti.masai2.internet.RetrofitClientInstance;
import com.example.bookthiti.masai2.mainscreen.MainActivity;
import com.example.bookthiti.masai2.mobileapplicationscanningscreen.appsearchscreen.TargetApplicationInfo;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bookthiti.masai2.utils.LogConstants.TAG_INFO;

public class MobileApplicationScanningResultFragment extends Fragment {
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TARGET_APP_INFO = "targetAppInfo";

    private Context mContext;
    private TargetApplicationInfo mTargetApplicationInfo;
    private TargetApplicationScanningResult mTargetApplicationScanningResult;

    private OnFragmentInteractionListener mListener;

    private Call<TargetApplicationScanningResult> mTargetApplicationScanningResultCall;

    private TextView mTextViewProgress;
    private Button mButtonRefresh;

    private ConstraintLayout mConstraintLayoutAppVulnerContainer;
    private ConstraintLayout mConstraintLayoutAndroidPermissions;
    private TextView mTextViewAndroidFindingHighRisk;
    private TextView mTextViewAndroidFindingWarning;
    private TextView mTextViewAndroidFindingLowRisk;
    private TextView mTextViewAndroidFindingInfo;

    private TextView mTextViewPermissionNormal;
    private TextView mTextViewPermissionSignature;
    private TextView mTextViewPermissionDangerous;
    private TextView mTextViewPermissionSpecial;

    private TextView mTextViewAverageCVSSScore;

    private ConstraintLayout mContainerAndroidFindingHighRisk;
    private ConstraintLayout mContainerAndroidFindingWarning;
    private ConstraintLayout mContainerAndroidFindingLowRisk;
    private ConstraintLayout mContainerAndroidFindingInfo;

    private ConstraintLayout mContainerPermissionNormal;
    private ConstraintLayout mContainerPermissionSignature;
    private ConstraintLayout mContainerPermissionDangerous;
    private ConstraintLayout mContainerPermissionSpecial;

    private RecyclerView mRecyclerViewAppVulners;
    private RecyclerView mRecyclerViewOwaspCategory;

    private AppVulnerabilityOwaspRecyclerAdapter mAppVulnerabilityOwaspRecyclerAdapter;
    private OwaspCategoryRecyclerAdapter mOwaspCategoryRecyclerAdapter;
    private List<AppVulnerability> mAppVulnerabilityList;
    private Map<String, List<AppVulnerability>> mOwaspSummary;

    public MobileApplicationScanningResultFragment() {
        // Required empty public constructor
    }

    public static MobileApplicationScanningResultFragment newInstance(TargetApplicationInfo targetApplicationInfo) {
        MobileApplicationScanningResultFragment fragment = new MobileApplicationScanningResultFragment();
        Bundle args = new Bundle();
        args.putParcelable(ARG_TARGET_APP_INFO, targetApplicationInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mTargetApplicationInfo = getArguments().getParcelable(ARG_TARGET_APP_INFO);
        }
        if (mTargetApplicationInfo != null) {
            String packageName = mTargetApplicationInfo.getAppId();
            int versionCode = mTargetApplicationInfo.getAppVersionCode();
            MasaiServerAPI masaiServerAPI = RetrofitClientInstance.getRetrofitInstance().create(MasaiServerAPI.class);
            mTargetApplicationScanningResultCall = masaiServerAPI.getAppScanningResult(packageName, versionCode);
            Log.i(TAG_INFO, packageName);
            mContext = getActivity();
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mobile_application_scanning_result2, container, false);
        mTextViewProgress = view.findViewById(R.id.text_progress);
        mButtonRefresh = view.findViewById(R.id.button_refresh);
        mButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mTextViewProgress.setVisibility(View.INVISIBLE);
                mButtonRefresh.setVisibility(View.INVISIBLE);
                refreshCall();
            }
        });
        mTextViewProgress.setVisibility(View.INVISIBLE);
        mButtonRefresh.setVisibility(View.INVISIBLE);

        mTextViewAverageCVSSScore = view.findViewById(R.id.text_cvss_average);

        mTextViewAndroidFindingHighRisk = view.findViewById(R.id.text_summary_high);
        mTextViewAndroidFindingWarning = view.findViewById(R.id.text_summary_medium);
        mTextViewAndroidFindingLowRisk = view.findViewById(R.id.text_summary_low);
        mTextViewAndroidFindingInfo = view.findViewById(R.id.text_summary_warning);

        mTextViewPermissionNormal = view.findViewById(R.id.text_permission_normal);
        mTextViewPermissionSignature = view.findViewById(R.id.text_permission_signature);
        mTextViewPermissionDangerous = view.findViewById(R.id.text_permission_dangerous);
        mTextViewPermissionSpecial = view.findViewById(R.id.text_permission_special);

        mContainerAndroidFindingHighRisk = view.findViewById(R.id.layout_container_android_finding_high);
        mContainerAndroidFindingWarning = view.findViewById(R.id.layout_container_android_finding_warning);
        mContainerAndroidFindingLowRisk = view.findViewById(R.id.layout_container_android_finding_low);
        mContainerAndroidFindingInfo = view.findViewById(R.id.layout_container_android_finding_info);

        mContainerPermissionNormal = view.findViewById(R.id.layout_container_android_permission_normal);
        mContainerPermissionSignature = view.findViewById(R.id.layout_container_android_permission_signature);
        mContainerPermissionDangerous = view.findViewById(R.id.layout_container_android_permission_dangerous);
        mContainerPermissionSpecial = view.findViewById(R.id.layout_container_android_permission_special);

        mConstraintLayoutAppVulnerContainer = view.findViewById(R.id.layout_container_app_result);
        mConstraintLayoutAndroidPermissions = view.findViewById(R.id.layout_container_android_permissions);

        mOwaspSummary = new TreeMap<String, List<AppVulnerability>>();

        mRecyclerViewAppVulners = view.findViewById(R.id.rv_android_finding);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerViewAppVulners.setLayoutManager(linearLayoutManager);
        mAppVulnerabilityList = new ArrayList<AppVulnerability>();
        mAppVulnerabilityOwaspRecyclerAdapter = new AppVulnerabilityOwaspRecyclerAdapter(getContext(), mOwaspSummary);
        mRecyclerViewAppVulners.setAdapter(mAppVulnerabilityOwaspRecyclerAdapter);

        mRecyclerViewOwaspCategory = view.findViewById(R.id.rv_owasp_category);
        LinearLayoutManager linearLayoutManager1 = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerViewOwaspCategory.setLayoutManager(linearLayoutManager1);
        mOwaspCategoryRecyclerAdapter = new OwaspCategoryRecyclerAdapter(getContext(), mOwaspSummary);
        mRecyclerViewOwaspCategory.setAdapter(mOwaspCategoryRecyclerAdapter);


        refreshCall();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }

    private void refreshCall() {
        if (mTargetApplicationScanningResultCall != null) {
            if (!mTargetApplicationScanningResultCall.isExecuted()) {
                Log.i(TAG_INFO, "the call is not yet executed");
                sendRequest();
            } else {
                mTargetApplicationScanningResultCall = mTargetApplicationScanningResultCall.clone();
                Log.i(TAG_INFO, "the call is executed, cloning the call");
                sendRequest();
            }
        }
    }

    private void sendRequest() {
        mTargetApplicationScanningResultCall.enqueue(new Callback<TargetApplicationScanningResult>() {
            @Override
            public void onResponse(Call<TargetApplicationScanningResult> call, Response<TargetApplicationScanningResult> response) {
                mTargetApplicationScanningResult = response.body();
                Log.i(TAG_INFO, mTargetApplicationScanningResult.getStatus());
                if (mTargetApplicationScanningResult.getStatus().equals("finish")) {
                    mTextViewProgress.setVisibility(View.GONE);
                    mButtonRefresh.setVisibility(View.GONE);
                    mConstraintLayoutAppVulnerContainer.setVisibility(View.VISIBLE);
                    mConstraintLayoutAndroidPermissions.setVisibility(View.VISIBLE);
                    mRecyclerViewAppVulners.setVisibility(View.VISIBLE);

                    mTextViewAverageCVSSScore.setText("" + mTargetApplicationScanningResult.getAverageCvss());

                    int[] findingSummary = TargetApplicationScanningResult.getFindingSummary(mTargetApplicationScanningResult);
                    int[] permissionSummary = TargetApplicationScanningResult.getPermissionSummary(mTargetApplicationScanningResult);
                    mOwaspSummary = TargetApplicationScanningResult.getOwaspSummary(mTargetApplicationScanningResult);
                    mTextViewAndroidFindingHighRisk.setText(Integer.toString(findingSummary[0]));
                    mTextViewAndroidFindingWarning.setText(Integer.toString(findingSummary[1]));
                    mTextViewAndroidFindingLowRisk.setText(Integer.toString(findingSummary[2]));
                    mTextViewAndroidFindingInfo.setText(Integer.toString(findingSummary[3]));

                    mTextViewPermissionNormal.setText(Integer.toString(permissionSummary[0]));
                    mTextViewPermissionSignature.setText(Integer.toString(permissionSummary[1]));
                    mTextViewPermissionDangerous.setText(Integer.toString(permissionSummary[2]));
                    mTextViewPermissionSpecial.setText(Integer.toString(permissionSummary[3]));

                    mAppVulnerabilityList.clear();
                    mAppVulnerabilityList.addAll(mTargetApplicationScanningResult.getAppVulnerabilityList());
                    mAppVulnerabilityOwaspRecyclerAdapter.setOwaspAppVulnerMap(mOwaspSummary);
                    mAppVulnerabilityOwaspRecyclerAdapter.notifyDataSetChanged();

                    mOwaspCategoryRecyclerAdapter.setOwaspCategory(mOwaspSummary);
                    mOwaspCategoryRecyclerAdapter.notifyDataSetChanged();

                    mContainerAndroidFindingHighRisk.setOnClickListener(new ContainerAndroidFindingOnClickListener(mContext, mAppVulnerabilityList, "high"));
                    mContainerAndroidFindingWarning.setOnClickListener(new ContainerAndroidFindingOnClickListener(mContext, mAppVulnerabilityList, "warning"));
                    mContainerAndroidFindingLowRisk.setOnClickListener(new ContainerAndroidFindingOnClickListener(mContext, mAppVulnerabilityList, "good"));
                    mContainerAndroidFindingInfo.setOnClickListener(new ContainerAndroidFindingOnClickListener(mContext, mAppVulnerabilityList, "info"));

                    mContainerPermissionNormal.setOnClickListener(new ContainerAndroidPermissionOnClickListener(mContext, mTargetApplicationScanningResult.getPermissionList(), "normal"));
                    mContainerPermissionSignature.setOnClickListener(new ContainerAndroidPermissionOnClickListener(mContext, mTargetApplicationScanningResult.getPermissionList(), "signature"));
                    mContainerPermissionDangerous.setOnClickListener(new ContainerAndroidPermissionOnClickListener(mContext, mTargetApplicationScanningResult.getPermissionList(), "dangerous"));
                    mContainerPermissionSpecial.setOnClickListener(new ContainerAndroidPermissionOnClickListener(mContext, mTargetApplicationScanningResult.getPermissionList(), "special"));
//                    mAppVulnerabilityOwaspRecyclerAdapter = new AppVulnerabilityOwaspRecyclerAdapter(getContext(), mTargetApplicationScanningResult.getAppVulnerabilityList());
//                    mRecyclerViewAppVulners.setAdapter(mAppVulnerabilityOwaspRecyclerAdapter);
                } else {
                    mTextViewProgress.setVisibility(View.VISIBLE);
                    mButtonRefresh.setVisibility(View.VISIBLE);
                }
                saveToDatabase(mTargetApplicationScanningResult);
                Log.i(TAG_INFO, "On success");
            }

            @Override
            public void onFailure(Call<TargetApplicationScanningResult> call, Throwable t) {
                mTextViewProgress.setVisibility(View.VISIBLE);
                mButtonRefresh.setVisibility(View.VISIBLE);
                Log.i(TAG_INFO, "On failure was called" + t.getMessage());
            }
        });
    }

    private void saveToDatabase(TargetApplicationScanningResult targetApplicationScanningResult) {
        MasaiViewModel masaiViewModel = MainActivity.getViewModel();
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MASAI_SHARED_PREF", Context.MODE_PRIVATE);
        JsonObject payload = targetApplicationScanningResult.getJsonObject();
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("resultType", "mobileAppScan");
        jsonObject.add("payload", payload);
        Gson gson = new Gson();
        String json = gson.toJson(jsonObject);
        masaiViewModel.insertActivityLogEntity("Mobile App Scan", "finish", json, sharedPreferences.getLong("testing_id", 0), Calendar.getInstance().getTime(), Calendar.getInstance().getTime());
    }
}
