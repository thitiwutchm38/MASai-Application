package com.example.bookthiti.masai2;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookthiti.masai2.iotinformationscreen.OwaspMobileActivity;
import com.example.bookthiti.masai2.iotinformationscreen.OwaspModel;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScanResultActivity extends AppCompatActivity implements OnRecyclerViewItemClickListener {

    LinearLayout line_layout;
    NotificationCompat.Builder mBuilder;


    //Show app detail
    TextView app_data_name;
    TextView app_data_id;
    TextView app_data_version;
    TextView app_data_type;

    String app_name;
    String app_id;
    String app_version;
    String app_type;


    //Show amount Android rule
    TextView textViewWarningResult;
    TextView textViewLowRiskResult;
    TextView textViewMediumResult;
    TextView textViewHighRiskResult;

    RelativeLayout RelativeLayoutScoreWarning;
    RelativeLayout RelativeLayoutLowRisk;
    RelativeLayout RelativeLayoutMediumRisk;
    RelativeLayout RelativeLayoutHighRisk;

    //Show amount permission rule

    TextView textViewSignatureResult;
    TextView textViewSignatureSysResult;
    TextView textViewNormalResult;
    TextView textViewHighResult;

    RelativeLayout RelativeLayoutSignature;
    RelativeLayout RelativeLayoutSignatureSys;
    RelativeLayout RelativeLayoutNormal;
    RelativeLayout RelativeLayoutHighResult;

    Button ButtonOwapsSummary;

    //Pop up result
    RelativeLayout relativeLayout_popup;

    //POP UP card
    TextView textView_popup_show_found;

    LinearLayout popup;

    //Android rule

    int warningResult;
    int lowResult;
    int mediumResult;
    int highResult;


    //Permission rule

    int signatureResult;
    int sysResult;
    int normalResult;
    int highPResult;

    private Context mContext;

    popUp_result_adapter mainRecyclerAdapter;


    popUp_permission_adapter mainRecyclerPAdapter;

    private JSONArray valuesWarning = null;
    private JSONArray valuesLow = null;
    private JSONArray valuesMedium = null;
    private JSONArray valuesHigh = null;

    private JSONArray valuesSignature = null;
    private JSONArray valuesSys = null;
    private JSONArray valuesNormal = null;
    private JSONArray valuesHighP = null;



    //Android rule
    private ArrayList<AndroidRuleModel> mainModelArrayListWarning = new ArrayList<>();
    private ArrayList<AndroidRuleModel> mainModelArrayListLow = new ArrayList<>();
    private ArrayList<AndroidRuleModel> mainModelArrayListMedium  = new ArrayList<>();
    private ArrayList<AndroidRuleModel> mainModelArrayListHigh  = new ArrayList<>();


    // Permission
    private ArrayList<PermissionModel> mainModelArrayListSignature = new ArrayList<>();
    private ArrayList<PermissionModel> mainModelArrayListSys = new ArrayList<>();
    private ArrayList<PermissionModel> mainModelArrayListNormal  = new ArrayList<>();
    private ArrayList<PermissionModel> mainModelArrayListHighP  = new ArrayList<>();



    @Override
    protected void onCreate( Bundle savedInstanceState ) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_result);

        mContext = getApplicationContext();
        //Recycler Adapter
//        ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.layout_pop_up_result);
        final RecyclerView mainRecyclerView = findViewById(R.id.recycle_popup_list);

 //       Toast.makeText(this," "+mainRecyclerView,Toast.LENGTH_SHORT).show();
//

        //Show app detail
        app_data_name = (TextView) findViewById(R.id.text_ip_address);
        app_data_id = (TextView) findViewById(R.id.text_mac_address);
        app_data_version = (TextView) findViewById(R.id.app_data_version);
        app_data_type = (TextView) findViewById(R.id.app_data_type);


        //Show amount score
        textViewWarningResult = (TextView) findViewById(R.id.textView_warning_result);
        textViewLowRiskResult = (TextView) findViewById(R.id.textView_low_risk_result);
        textViewMediumResult = (TextView) findViewById(R.id.textView_medium_result);
        textViewHighRiskResult = (TextView) findViewById(R.id.textView_high_risk_result);


        RelativeLayoutScoreWarning = (RelativeLayout) findViewById(R.id.score_warning);
        RelativeLayoutLowRisk = (RelativeLayout) findViewById(R.id.score_low_risk);
        RelativeLayoutMediumRisk  = (RelativeLayout) findViewById(R.id.score_medium_risk);
        RelativeLayoutHighRisk  = (RelativeLayout) findViewById(R.id.score_high_risk);


        //Show amount permission rule

        textViewSignatureResult  = (TextView) findViewById(R.id.textView_signature_result);
        textViewSignatureSysResult = (TextView) findViewById(R.id.textView_sig_sys_result);
        textViewNormalResult = (TextView) findViewById(R.id.textView_normal_result);
        textViewHighResult = (TextView) findViewById(R.id.textView_high_result);


         RelativeLayoutSignature = (RelativeLayout) findViewById(R.id.permission_sig);
         RelativeLayoutSignatureSys = (RelativeLayout) findViewById(R.id.permission_sig_sys);
         RelativeLayoutNormal = (RelativeLayout) findViewById(R.id.permission_normal_risk);
         RelativeLayoutHighResult = (RelativeLayout) findViewById(R.id.permission_high_risk);


        ButtonOwapsSummary = (Button) findViewById(R.id.btt_OWAPS_sum);


        popup = (LinearLayout) findViewById(R.id.relativeLayout_popup);


        line_layout = (LinearLayout) findViewById(R.id.linearLayout_scan_result);

        line_layout.setVisibility(View.GONE);

        textView_popup_show_found = (TextView) findViewById(R.id.textView_popup_show_found);

        prepareList();

        //App detail
        app_data_name.setText(app_name);
        app_data_id.setText(app_id);
        app_data_version.setText(app_version);;
        app_data_type.setText(app_type);;

        //Amount score

        textViewWarningResult.setText(Integer.toString(warningResult));
        textViewLowRiskResult.setText(Integer.toString(lowResult));
        textViewMediumResult.setText(Integer.toString(mediumResult));
        textViewHighRiskResult.setText(Integer.toString(highResult));

        //Show amount permission rule

        textViewSignatureResult.setText(Integer.toString(signatureResult));
        textViewSignatureSysResult.setText(Integer.toString(sysResult));
        textViewNormalResult.setText(Integer.toString(normalResult));
        textViewHighResult.setText(Integer.toString(highPResult));


        ButtonOwapsSummary.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                Intent intent = new Intent(ScanResultActivity.this, OwaspSummaryActivity.class);
                startActivity(intent);

            }
        });


        RelativeLayoutSignature.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(v.getContext());
                builder.setContentView(R.layout.pop_up_password_suggestion);
                builder.setGravity(Gravity.CENTER);
                builder.setScaleRatio(0.2f);
                builder.setBlurRadius(5);
                builder.setTintColor(0x3000000);
                BlurPopupWindow blurPopupWindow = builder.build();
                blurPopupWindow.show();

                RecyclerView recyclerView = blurPopupWindow.findViewById(R.id.recycle_popup_permission_list);

                popup = (LinearLayout)blurPopupWindow.findViewById(R.id.relativeLayout_popup);
                popup.setBackgroundResource(R.drawable.shape_score);

                TextView textViewNumResult = (TextView)blurPopupWindow.findViewById(R.id.textView_popup_show_found);

                textViewNumResult.setText("SIGNATURE: "+ signatureResult +"  permissions found");

                popup.setBackgroundColor(Color.parseColor("#D3E3F5"));

                //Toast.makeText(mContext, ""+recyclerView, Toast.LENGTH_SHORT).show();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                        LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);

                PermissionModel mainModel = new PermissionModel();

                mainRecyclerPAdapter = new popUp_permission_adapter(mContext,mainModelArrayListSignature);

                mainRecyclerPAdapter.setOnRecyclerViewItemClickListener(ScanResultActivity.this);
                recyclerView.setAdapter(mainRecyclerPAdapter);
            }
        });


        RelativeLayoutSignatureSys.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(v.getContext());
                builder.setContentView(R.layout.pop_up_password_suggestion);
                builder.setGravity(Gravity.CENTER);
                builder.setScaleRatio(0.2f);
                builder.setBlurRadius(5);
                builder.setTintColor(0x3000000);
                BlurPopupWindow blurPopupWindow = builder.build();
                blurPopupWindow.show();

                RecyclerView recyclerView = blurPopupWindow.findViewById(R.id.recycle_popup_permission_list);

                popup = (LinearLayout)blurPopupWindow.findViewById(R.id.relativeLayout_popup);
                popup.setBackgroundResource(R.drawable.shape_score);

                TextView textViewNumResult = (TextView)blurPopupWindow.findViewById(R.id.textView_popup_show_found);

                textViewNumResult.setText("SYSTEM: "+ sysResult +"  permissions found");

                popup.setBackgroundColor(Color.parseColor("#D3E3F5"));

                //Toast.makeText(mContext, ""+recyclerView, Toast.LENGTH_SHORT).show();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                        LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);

                PermissionModel mainModel = new PermissionModel();

                mainRecyclerPAdapter = new popUp_permission_adapter(mContext,mainModelArrayListSys);

                mainRecyclerPAdapter.setOnRecyclerViewItemClickListener(ScanResultActivity.this);
                recyclerView.setAdapter(mainRecyclerPAdapter);
            }
        });

        RelativeLayoutNormal.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(v.getContext());
                builder.setContentView(R.layout.pop_up_password_suggestion);
                builder.setGravity(Gravity.CENTER);
                builder.setScaleRatio(0.2f);
                builder.setBlurRadius(5);
                builder.setTintColor(0x3000000);
                BlurPopupWindow blurPopupWindow = builder.build();
                blurPopupWindow.show();

                RecyclerView recyclerView = blurPopupWindow.findViewById(R.id.recycle_popup_permission_list);

                popup = (LinearLayout)blurPopupWindow.findViewById(R.id.relativeLayout_popup);
                popup.setBackgroundResource(R.drawable.shape_score);

                TextView textViewNumResult = (TextView)blurPopupWindow.findViewById(R.id.textView_popup_show_found);

                textViewNumResult.setText("NORMAL: "+ normalResult +"  permissions found");

                popup.setBackgroundColor(Color.parseColor("#D3E3F5"));

                //Toast.makeText(mContext, ""+recyclerView, Toast.LENGTH_SHORT).show();

                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                        LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);

                PermissionModel mainModel = new PermissionModel();

                mainRecyclerPAdapter = new popUp_permission_adapter(mContext,mainModelArrayListNormal);

                mainRecyclerPAdapter.setOnRecyclerViewItemClickListener(ScanResultActivity.this);
                recyclerView.setAdapter(mainRecyclerPAdapter);
            }
        });

        RelativeLayoutHighResult.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(v.getContext());
                builder.setContentView(R.layout.pop_up_password_suggestion);
                builder.setGravity(Gravity.CENTER);
                builder.setScaleRatio(0.2f);
                builder.setBlurRadius(5);
                builder.setTintColor(0x3000000);
                BlurPopupWindow blurPopupWindow = builder.build();
                blurPopupWindow.show();

                RecyclerView recyclerView = blurPopupWindow.findViewById(R.id.recycle_popup_permission_list);

                popup = (LinearLayout)blurPopupWindow.findViewById(R.id.relativeLayout_popup);
                popup.setBackgroundResource(R.drawable.shape_score);

                TextView textViewNumResult = (TextView)blurPopupWindow.findViewById(R.id.textView_popup_show_found);

                textViewNumResult.setText("HIGH RISK: "+ highPResult +"  permissions found");

                popup.setBackgroundColor(Color.parseColor("#D3E3F5"));


                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                        LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);

                PermissionModel mainModel = new PermissionModel();

                mainRecyclerPAdapter = new popUp_permission_adapter(mContext,mainModelArrayListHighP);

                mainRecyclerPAdapter.setOnRecyclerViewItemClickListener(ScanResultActivity.this);
                recyclerView.setAdapter(mainRecyclerPAdapter);
            }
        });










        RelativeLayoutScoreWarning.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {


                BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(v.getContext());
                builder.setContentView(R.layout.pop_up_android_rule_result);
                builder.setGravity(Gravity.CENTER);
                builder.setScaleRatio(0.2f);
                builder.setBlurRadius(10);
                builder.setTintColor(0x3000000);
                BlurPopupWindow blurPopupWindow = builder.build();
                blurPopupWindow.show();

                RecyclerView recyclerView = blurPopupWindow.findViewById(R.id.recycle_popup_list);
                popup = (LinearLayout)blurPopupWindow.findViewById(R.id.relativeLayout_popup);

                popup.setBackgroundResource(R.drawable.shape_score);

                TextView textViewNumResult = (TextView)blurPopupWindow.findViewById(R.id.textView_popup_show_found);
                textViewNumResult.setText("Warning Risk: "+ warningResult +" Founded");
                popup.setBackgroundColor(Color.parseColor("#13CE66"));

                //Toast.makeText(mContext, ""+recyclerView, Toast.LENGTH_SHORT).show();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                AndroidRuleModel mainModel = new AndroidRuleModel();
                


                mainRecyclerAdapter = new popUp_result_adapter(mContext,mainModelArrayListWarning);

                mainRecyclerAdapter.setOnRecyclerViewItemClickListener(ScanResultActivity.this);
                recyclerView.setAdapter(mainRecyclerAdapter);
            }
        });



        RelativeLayoutLowRisk.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {


                BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(v.getContext());
                builder.setContentView(R.layout.pop_up_android_rule_result);
                builder.setGravity(Gravity.CENTER);
                builder.setScaleRatio(0.2f);
                builder.setBlurRadius(10);
                builder.setTintColor(0x3000000);
                BlurPopupWindow blurPopupWindow = builder.build();
                blurPopupWindow.show();

                RecyclerView recyclerView = blurPopupWindow.findViewById(R.id.recycle_popup_list);
                popup = (LinearLayout)blurPopupWindow.findViewById(R.id.relativeLayout_popup);


                TextView textViewNumResult = (TextView)blurPopupWindow.findViewById(R.id.textView_popup_show_found);
                textViewNumResult.setText("LOW Risk: "+lowResult+" Founded !");

                popup.setBackground(mContext.getResources().getDrawable(R.drawable.shape_score));
                popup.setBackgroundColor(Color.parseColor("#F0E544"));

                //        Toast.makeText(mContext, ""+recyclerView, Toast.LENGTH_SHORT).show();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                        LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);

                mainRecyclerAdapter = new popUp_result_adapter(mContext,mainModelArrayListLow);

                mainRecyclerAdapter.setOnRecyclerViewItemClickListener(ScanResultActivity.this);
                recyclerView.setAdapter(mainRecyclerAdapter);
            }
        });


        RelativeLayoutMediumRisk.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(v.getContext());
                builder.setContentView(R.layout.pop_up_android_rule_result);
                builder.setGravity(Gravity.CENTER);
                builder.setScaleRatio(0.2f);
                builder.setBlurRadius(10);
                builder.setTintColor(0x3000000);
                BlurPopupWindow blurPopupWindow = builder.build();
                blurPopupWindow.show();

                RecyclerView recyclerView = blurPopupWindow.findViewById(R.id.recycle_popup_list);


                TextView textViewNumResult = (TextView)blurPopupWindow.findViewById(R.id.textView_popup_show_found);
                textViewNumResult.setText("Medium Risk: "+mediumResult+" Founded !");

                popup = (LinearLayout)blurPopupWindow.findViewById(R.id.relativeLayout_popup);

                popup.setBackgroundResource(R.drawable.shape_score);
                popup.setBackgroundColor(Color.parseColor("#FFBA5C"));

                //Toast.makeText(mContext, ""+recyclerView, Toast.LENGTH_SHORT).show();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                        LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                AndroidRuleModel mainModel = new AndroidRuleModel();

                mainRecyclerAdapter = new popUp_result_adapter(mContext,mainModelArrayListMedium);

                mainRecyclerAdapter.setOnRecyclerViewItemClickListener(ScanResultActivity.this);
                recyclerView.setAdapter(mainRecyclerAdapter);
            }
        });


        RelativeLayoutHighRisk.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(v.getContext());
                builder.setContentView(R.layout.pop_up_android_rule_result);
                builder.setGravity(Gravity.CENTER);
                builder.setScaleRatio(0.2f);
                builder.setBlurRadius(10);
                builder.setTintColor(0x3000000);
                BlurPopupWindow blurPopupWindow = builder.build();
                blurPopupWindow.show();

                RecyclerView recyclerView = blurPopupWindow.findViewById(R.id.recycle_popup_list);
                popup = (LinearLayout)blurPopupWindow.findViewById(R.id.relativeLayout_popup);

                TextView textViewNumResult = (TextView)blurPopupWindow.findViewById(R.id.textView_popup_show_found);
                textViewNumResult.setText("High Risk : "+highResult+" Founded !");

                popup.setBackgroundResource(R.drawable.shape_score);
                popup.setBackgroundColor(Color.parseColor("#F95F62"));

                //Toast.makeText(mContext, ""+recyclerView, Toast.LENGTH_SHORT).show();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                        LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                AndroidRuleModel mainModel = new AndroidRuleModel();

                mainRecyclerAdapter = new popUp_result_adapter(mContext,mainModelArrayListHigh);

                mainRecyclerAdapter.setOnRecyclerViewItemClickListener(ScanResultActivity.this);
                recyclerView.setAdapter(mainRecyclerAdapter);
            }
        });



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {


                final ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar_app_scan);
                progressBar.setVisibility(View.GONE);

                line_layout.setVisibility(View.VISIBLE);



            }
        }, 1000); // Millisecond 1000 = 1 sec
    }



    private void prepareList() {


        //Convert JSON File
        String json = null;
        Integer count = null;


        try {
            InputStream is = getAssets().open("appresult.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
        }


        JSONObject reader = null;
        try {
            reader = new JSONObject(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }



        JSONObject app_detail_temp = null;

        try {
            app_detail_temp = reader.getJSONObject("app_detail");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            app_name  = app_detail_temp.getString("App Name");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        try {
            app_id  = app_detail_temp.getString("App ID");
        } catch (JSONException e) {
            e.printStackTrace();
        }
         try {
             app_version  = app_detail_temp.getString("App Version");
         } catch (JSONException e) {
             e.printStackTrace();
         }
        try {
             app_type  = app_detail_temp.getString("App Type");
        } catch (JSONException e) {
            e.printStackTrace();
        }

            JSONObject app_poten_security_temp = null;

        try {
            app_poten_security_temp = reader.getJSONObject("android_rules");
         } catch (JSONException e) {
                e.printStackTrace();
         }


        // ---------------- JSON scan Warning result   ---------------- //

        try {
            valuesWarning = app_poten_security_temp.getJSONArray("warning");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        warningResult = valuesWarning.length();



        for (int i = 0; i < valuesWarning.length(); i++) {

            String problem = null;
            String OWASP= null;


            JSONObject jsonobject = null;

            try {
                jsonobject = valuesWarning.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                problem = jsonobject.getString("Problem");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                OWASP = jsonobject.getString("OWASP");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidRuleModel mainModel = new AndroidRuleModel();


            mainModel.setProblem(problem);
            mainModel.setOwasp_num(OWASP);

            mainModelArrayListWarning.add(mainModel);

            //Sort by M (OWASP)
            Collections.sort(mainModelArrayListWarning, new Comparator<AndroidRuleModel>() {
                        @Override
                        public int compare( AndroidRuleModel routerModel, AndroidRuleModel t1 ) {
                            return routerModel.getOwasp_num().compareTo(t1.getOwasp_num());
                        }
                    });
        }

        // ---------------- JSON scan Low result   ---------------- //

        try {
            valuesLow = app_poten_security_temp.getJSONArray("low");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        lowResult = valuesLow.length();

        for (int i = 0; i < valuesLow.length(); i++) {

            String problem = null;
            String OWASP= null;

            JSONObject jsonobject = null;

            try {
                jsonobject = valuesLow.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                problem = jsonobject.getString("Problem");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                OWASP = jsonobject.getString("OWASP");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidRuleModel mainModel = new AndroidRuleModel();

            mainModel.setProblem(problem);
            mainModel.setOwasp_num(OWASP);

            mainModelArrayListLow.add(mainModel);

            //Sort by M (OWASP)
            Collections.sort(mainModelArrayListLow, new Comparator<AndroidRuleModel>() {
                @Override
                public int compare( AndroidRuleModel routerModel, AndroidRuleModel t1 ) {
                    return routerModel.getOwasp_num().compareTo(t1.getOwasp_num());
                }
            });


        }

        // ---------------- JSON scan Medium result   ---------------- //

        try {
            valuesMedium = app_poten_security_temp.getJSONArray("medium");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        mediumResult = valuesMedium.length();

        for (int i = 0; i < valuesMedium.length(); i++) {

            String problem = null;
            String OWASP = null;

            JSONObject jsonobject = null;

            try {
                jsonobject = valuesMedium.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                problem = jsonobject.getString("Problem");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                OWASP = jsonobject.getString("OWASP");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidRuleModel mainModel = new AndroidRuleModel();

            mainModel.setProblem(problem);
            mainModel.setOwasp_num(OWASP);

            mainModelArrayListMedium.add(mainModel);

            //Sort by M (OWASP)
            Collections.sort(mainModelArrayListMedium, new Comparator<AndroidRuleModel>() {
                @Override
                public int compare( AndroidRuleModel routerModel, AndroidRuleModel t1 ) {
                    return routerModel.getOwasp_num().compareTo(t1.getOwasp_num());
                }
            });
        }

        // ---------------- JSON scan High result   ---------------- //

        try {
            valuesHigh = app_poten_security_temp.getJSONArray("high");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        highResult = valuesHigh.length();

        for (int i = 0; i < valuesHigh.length(); i++) {

            String problem = null;
            String OWASP= null;

            JSONObject jsonobject = null;

            try {
                jsonobject = valuesHigh.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                problem = jsonobject.getString("Problem");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                OWASP = jsonobject.getString("OWASP");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            AndroidRuleModel mainModel = new AndroidRuleModel();

            mainModel.setProblem(problem);
            mainModel.setOwasp_num(OWASP);

            mainModelArrayListHigh.add(mainModel);

            //Sort by M (OWASP)
            Collections.sort(mainModelArrayListHigh, new Comparator<AndroidRuleModel>() {
                @Override
                public int compare( AndroidRuleModel routerModel, AndroidRuleModel t1 ) {
                    return routerModel.getOwasp_num().compareTo(t1.getOwasp_num());
                }
            });

        }

        JSONObject app_permission_temp = null;
        try {
            app_permission_temp = reader.getJSONObject("android_permission");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // ---------------- JSON scan Signature result   ---------------- //

        try {
            valuesSignature = app_permission_temp.getJSONArray("signature");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        signatureResult = valuesSignature.length();

        for (int i = 0; i < valuesSignature.length(); i++) {

            String problem = null;
            String OWASP = null;
            String Information = null;
            String Description= null ;

            JSONObject jsonobject = null;

            try {
                jsonobject = valuesSignature.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                problem = jsonobject.getString("Problem");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                OWASP = jsonobject.getString("OWASP");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Information = jsonobject.getString("Info");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Description = jsonobject.getString("Description");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            PermissionModel mainModel = new PermissionModel();

            mainModel.setProblem(problem);
            mainModel.setOwasp_num(OWASP);
            mainModel.setDescription(Description);
            mainModel.setInformation(Information);

            mainModelArrayListSignature.add(mainModel);

            //Sort by M (OWASP)
            Collections.sort(mainModelArrayListSignature, new Comparator<PermissionModel>() {
                @Override
                public int compare( PermissionModel routerModel, PermissionModel t1 ) {
                    return routerModel.getOwasp_num().compareTo(t1.getOwasp_num());
                }
            });
        }


        // ---------------- JSON scan Sys result   ---------------- //

        try {
            valuesSys = app_permission_temp.getJSONArray("system");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        sysResult = valuesSys.length();

        for (int i = 0; i < valuesSys.length(); i++) {

            String problem = null;
            String OWASP = null;
            String Information = null;
            String Description= null ;

            JSONObject jsonobject = null;

            try {
                jsonobject = valuesSys.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                problem = jsonobject.getString("Problem");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                OWASP = jsonobject.getString("OWASP");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Information = jsonobject.getString("Info");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Description = jsonobject.getString("Description");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            PermissionModel mainModel = new PermissionModel();

            mainModel.setProblem(problem);
            mainModel.setOwasp_num(OWASP);
            mainModel.setDescription(Description);
            mainModel.setInformation(Information);

            mainModelArrayListSys.add(mainModel);

            //Sort by M (OWASP)
            Collections.sort(mainModelArrayListSys, new Comparator<PermissionModel>() {
                @Override
                public int compare( PermissionModel routerModel, PermissionModel t1 ) {
                    return routerModel.getOwasp_num().compareTo(t1.getOwasp_num());
                }
            });
        }
        // ---------------- JSON scan Signature result   ---------------- //

        try {
            valuesNormal = app_permission_temp.getJSONArray("normal");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        normalResult = valuesNormal.length();

        for (int i = 0; i < valuesNormal.length(); i++) {

            String problem = null;
            String OWASP = null;
            String Information = null;
            String Description= null ;

            JSONObject jsonobject = null;

            try {
                jsonobject = valuesNormal.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                problem = jsonobject.getString("Problem");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                OWASP = jsonobject.getString("OWASP");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Information = jsonobject.getString("Info");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Description = jsonobject.getString("Description");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            PermissionModel mainModel = new PermissionModel();

            mainModel.setProblem(problem);
            mainModel.setOwasp_num(OWASP);
            mainModel.setDescription(Description);
            mainModel.setInformation(Information);

            mainModelArrayListNormal.add(mainModel);

            //Sort by M (OWASP)
            Collections.sort(mainModelArrayListNormal, new Comparator<PermissionModel>() {
                @Override
                public int compare( PermissionModel routerModel, PermissionModel t1 ) {
                    return routerModel.getOwasp_num().compareTo(t1.getOwasp_num());
                }
            });
        }


        // ---------------- JSON scan High result   ---------------- //

        try {
            valuesHighP = app_permission_temp.getJSONArray("high");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        highPResult = valuesHighP.length();

        for (int i = 0; i < valuesHighP.length(); i++) {

            String problem = null;
            String OWASP = null;
            String Information = null;
            String Description= null ;

            JSONObject jsonobject = null;

            try {
                jsonobject = valuesHighP.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            try {
                problem = jsonobject.getString("Problem");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                OWASP = jsonobject.getString("OWASP");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Information = jsonobject.getString("Info");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            try {
                Description = jsonobject.getString("Description");
            } catch (JSONException e) {
                e.printStackTrace();
            }

            PermissionModel mainModel = new PermissionModel();

            mainModel.setProblem(problem);
            mainModel.setOwasp_num(OWASP);
            mainModel.setDescription(Description);
            mainModel.setInformation(Information);

            mainModelArrayListHighP.add(mainModel);

            //Sort by M (OWASP)
            Collections.sort(mainModelArrayListHighP, new Comparator<PermissionModel>() {
                @Override
                public int compare( PermissionModel routerModel, PermissionModel t1 ) {
                    return routerModel.getOwasp_num().compareTo(t1.getOwasp_num());
                }
            });
        }
    }

    @Override
    public void onItemClick( int position, View view ) {


        if (view.getTag().getClass()==PermissionModel.class) {

            PermissionModel mainModel = (PermissionModel) view.getTag();

            switch (view.getId()) {

                case R.id.layout_pop_up_permission_result:
                    Toast.makeText(this, "Position clicked: " + String.valueOf(position) + ", " + mainModel.getProblem(), Toast.LENGTH_LONG).show();
                    //openActivityPortInfo(position);
                    Intent intent = new Intent(this, OwaspMobileActivity.class);
                    String owaspTopicId = mainModel.getOwasp_num();
                    String owaspTopicIdPattern = "([MI])(\\d+)";
                    Pattern pattern = Pattern.compile(owaspTopicIdPattern);
                    Matcher matcher = pattern.matcher(owaspTopicId);
                    Resources res = getResources();
                    String[] topicIds = res.getStringArray(R.array.m_topic_id);
                    String[] topics = res.getStringArray(R.array.m_topic);
                    String[] details = res.getStringArray(R.array.m_detail);
                    String[] examples = res.getStringArray(R.array.m_example);
                    String[] guidelines = res.getStringArray(R.array.m_guideline);

                    if (matcher.find()) {
                        int id = Integer.parseInt(matcher.group(2)) - 1;
                        // TODO: set image id according to id
                        OwaspModel owaspModel = new OwaspModel(topicIds[id], topics[id], details[id], examples[id], guidelines[id], 0);
                        intent.putExtra("owaspModel", owaspModel);
                    }


                    startActivity(intent);
                    break;

            }

        }else{
                AndroidRuleModel mainModel = (AndroidRuleModel) view.getTag();

            switch (view.getId()) {

                case R.id.layout_pop_up_result:
                    Toast.makeText(this, "Position clicked: " + String.valueOf(position) + ", " + mainModel.getProblem(), Toast.LENGTH_LONG).show();
                    //openActivityPortInfo(position);
                    Intent intent_2 = new Intent(this,OwaspMobileActivity.class);

                    String owaspTopicId = mainModel.getOwasp_num();
                    String owaspTopicIdPattern = "([MI])(\\d+)";
                    Pattern pattern = Pattern.compile(owaspTopicIdPattern);
                    Matcher matcher = pattern.matcher(owaspTopicId);
                    Resources res = getResources();
                    String[] topicIds = res.getStringArray(R.array.m_topic_id);
                    String[] topics = res.getStringArray(R.array.m_topic);
                    String[] details = res.getStringArray(R.array.m_detail);
                    String[] examples = res.getStringArray(R.array.m_example);
                    String[] guidelines = res.getStringArray(R.array.m_guideline);

                    if (matcher.find()) {
                        int id = Integer.parseInt(matcher.group(2)) - 1;
                        // TODO: set image id according to id
                        OwaspModel owaspModel = new OwaspModel(topicIds[id], topics[id], details[id], examples[id], guidelines[id], 0);
                        intent_2.putExtra("owaspModel", owaspModel);
                    }
                    startActivity(intent_2);
                    break;

            }
            }

    }

}
