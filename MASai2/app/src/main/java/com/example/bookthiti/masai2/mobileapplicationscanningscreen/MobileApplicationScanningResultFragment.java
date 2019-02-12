package com.example.bookthiti.masai2.mobileapplicationscanningscreen;

import android.content.Context;
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
import android.widget.TextView;

import com.example.bookthiti.masai2.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.bookthiti.masai2.LogConstants.TAG_INFO;

public class MobileApplicationScanningResultFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TARGET_APP_INFO = "targetAppInfo";
//    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;

    private TargetApplicationInfo mTargetApplicationInfo;
    private TargetApplicationScanningResult mTargetApplicationScanningResult;

    private OnFragmentInteractionListener mListener;

    private Call<TargetApplicationScanningResult> mTargetApplicationScanningResultCall;

    private TextView mTextViewProgress;
    private Button mButtonRefresh;

    private ConstraintLayout mConstraintLayout;
    private TextView mTextViewAndroidFindingHighRisk;
    private TextView mTextViewAndroidFindingWarning;
    private TextView mTextViewAndroidFindingLowRisk;
    private TextView mTextViewAndroidFindingInfo;

    private TextView mTextViewPermissionNormal;
    private TextView mTextViewPermissionSignature;
    private TextView mTextViewPermissionDangerous;
    private TextView mTextViewPermissionSpecial;

    private RecyclerView mRecyclerViewAppVulners;

    private AppVulnerabilityOwaspRecyclerAdapter mAppVulnerabilityOwaspRecyclerAdapter;
    private List<AppVulnerability> mAppVulnerabilityList;

    public MobileApplicationScanningResultFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static MobileApplicationScanningResultFragment newInstance(TargetApplicationInfo targetApplicationInfo) {
        MobileApplicationScanningResultFragment fragment = new MobileApplicationScanningResultFragment();
        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
        args.putParcelable(ARG_TARGET_APP_INFO, targetApplicationInfo);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
            mTargetApplicationInfo = getArguments().getParcelable(ARG_TARGET_APP_INFO);
        }
        if (mTargetApplicationInfo != null) {
            String packageName = mTargetApplicationInfo.getAppId();
            int versionCode = mTargetApplicationInfo.getAppVersionCode();
            MasaiServerAPI masaiServerAPI = RetrofitClientInstance.getRetrofitInstance().create(MasaiServerAPI.class);
            mTargetApplicationScanningResultCall = masaiServerAPI.getAppScanningResult(packageName, versionCode);
            Log.i(TAG_INFO, packageName);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mobile_application_scanning_result, container, false);
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

        mTextViewAndroidFindingHighRisk = view.findViewById(R.id.text_summary_high);
        mTextViewAndroidFindingWarning = view.findViewById(R.id.text_summary_medium);
        mTextViewAndroidFindingLowRisk = view.findViewById(R.id.text_summary_low);
        mTextViewAndroidFindingInfo = view.findViewById(R.id.text_summary_warning);

        mTextViewPermissionNormal = view.findViewById(R.id.text_permission_normal);
        mTextViewPermissionSignature = view.findViewById(R.id.text_permission_signature);
        mTextViewPermissionDangerous = view.findViewById(R.id.text_permission_dangerous);
        mTextViewPermissionSpecial = view.findViewById(R.id.text_permission_special);

        mConstraintLayout = view.findViewById(R.id.layout_container_app_result);

        mRecyclerViewAppVulners = view.findViewById(R.id.rv_android_finding);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL, false);
        mRecyclerViewAppVulners.setLayoutManager(linearLayoutManager);
        mAppVulnerabilityList = new ArrayList<AppVulnerability>();
        mAppVulnerabilityOwaspRecyclerAdapter = new AppVulnerabilityOwaspRecyclerAdapter(getContext(), mAppVulnerabilityList);
        mRecyclerViewAppVulners.setAdapter(mAppVulnerabilityOwaspRecyclerAdapter);
        refreshCall();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    // TODO: Rename method, update argument and hook method into UI event
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void refreshCall() {
        if (mTargetApplicationScanningResultCall != null) {
            if (!mTargetApplicationScanningResultCall.isExecuted()) {
                sendRequest();
            } else {
                mTargetApplicationScanningResultCall = mTargetApplicationScanningResultCall.clone();
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
                if(mTargetApplicationScanningResult.getStatus().equals("finish")) {
                    mTextViewProgress.setVisibility(View.GONE);
                    mButtonRefresh.setVisibility(View.GONE);
                    mConstraintLayout.setVisibility(View.VISIBLE);
                    mRecyclerViewAppVulners.setVisibility(View.VISIBLE);

                    int[] findingSummary = TargetApplicationScanningResult.getFindingSummary(mTargetApplicationScanningResult);
                    int[] permissionSummary = TargetApplicationScanningResult.getPermissionSummary(mTargetApplicationScanningResult);
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
                    mAppVulnerabilityOwaspRecyclerAdapter.updateOwaspAppVulnerMap();
                    mAppVulnerabilityOwaspRecyclerAdapter.notifyDataSetChanged();

//                    mAppVulnerabilityOwaspRecyclerAdapter = new AppVulnerabilityOwaspRecyclerAdapter(getContext(), mTargetApplicationScanningResult.getAppVulnerabilityList());
//                    mRecyclerViewAppVulners.setAdapter(mAppVulnerabilityOwaspRecyclerAdapter);


                } else {
                    mTextViewProgress.setVisibility(View.VISIBLE);
                    mButtonRefresh.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<TargetApplicationScanningResult> call, Throwable t) {
                mTextViewProgress.setVisibility(View.VISIBLE);
                mButtonRefresh.setVisibility(View.VISIBLE);
            }
        });
    }
}
