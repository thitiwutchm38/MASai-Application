package com.example.bookthiti.masai2.mobileapplicationscanningscreen;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MobileApplicationScanningResultFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MobileApplicationScanningResultFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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

    private ProgressBar mProgressBar;
    private TextView mTextViewProgress;
    private Button mButtonRefresh;

    private ConstraintLayout mConstraintLayout;

    public MobileApplicationScanningResultFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MobileApplicationScanningResultFragment.
     */
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
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_mobile_application_scanning_result, container, false);
        mProgressBar = view.findViewById(R.id.progress_app_scan_result);
        mTextViewProgress = view.findViewById(R.id.text_progress);
        mButtonRefresh = view.findViewById(R.id.button_refresh);
        mButtonRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mProgressBar.setVisibility(View.VISIBLE);
                refreshCall();
            }
        });
        mTextViewProgress.setVisibility(View.INVISIBLE);
        mButtonRefresh.setVisibility(View.INVISIBLE);

        refreshCall();

        return view;
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
        mTargetApplicationScanningResultCall.enqueue(new Callback<TargetApplicationScanningResult>() {
            @Override
            public void onResponse(Call<TargetApplicationScanningResult> call, Response<TargetApplicationScanningResult> response) {
                mTargetApplicationScanningResult = response.body();

            }

            @Override
            public void onFailure(Call<TargetApplicationScanningResult> call, Throwable t) {

            }
        });
    }
}
