package com.example.bookthiti.masai2;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.bookthiti.masai2.networksearchingscreen.OnRecyclerViewItemClickListener;
import com.kyleduo.blurpopupwindow.library.BlurPopupWindow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

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


    //Show amount score
    TextView textViewWarningResult;
    TextView textViewLowRiskResult;
    TextView textViewMediumResult;
    TextView textViewHighRiskResult;

    RelativeLayout RelativeLayoutScoreWarning;
    RelativeLayout RelativeLayoutLowRisk;
    RelativeLayout RelativeLayoutMediumRisk;
    RelativeLayout RelativeLayoutHighRisk;



    //Pop up result
    RelativeLayout relativeLayout_popup;

    //POP UP card

    TextView textView_popup_show_found;

    LinearLayout popup;

    int warning_result;
    int low_risk_result;
    int medium_result;
    int high_risk_result;

    private Context mContext;

    popUp_result_adapter mainRecyclerAdapter;



    private JSONArray valuesWarning = null;
    private JSONArray valuesLow = null;
    private JSONArray valuesMedium = null;
    private JSONArray valuesHigh = null;



    private ArrayList<popUp_result> mainModelArrayListWarning = new ArrayList<>();
    private ArrayList<popUp_result> mainModelArrayListLow = new ArrayList<>();
    private ArrayList<popUp_result> mainModelArrayListMedium  = new ArrayList<>();
    private ArrayList<popUp_result> mainModelArrayListHigh  = new ArrayList<>();



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
        app_data_name = (TextView) findViewById(R.id.textView_data_ip_address);
        app_data_id = (TextView) findViewById(R.id.app_data_id);
        app_data_version = (TextView) findViewById(R.id.app_data_version);
        app_data_type = (TextView) findViewById(R.id.app_data_type);


        //Show amount score
        textViewWarningResult = (TextView) findViewById(R.id.textView_warning_result);
        textViewLowRiskResult = (TextView) findViewById(R.id.textView_low_risk_result);
        textViewMediumResult = (TextView) findViewById(R.id.textView_medium_result);
        textViewHighRiskResult = (TextView) findViewById(R.id.textView_high_risk_result);


        popup = (LinearLayout) findViewById(R.id.relativeLayout_popup);



        RelativeLayoutScoreWarning = (RelativeLayout) findViewById(R.id.score_warning);
        RelativeLayoutLowRisk = (RelativeLayout) findViewById(R.id.score_low_risk);
        RelativeLayoutMediumRisk  = (RelativeLayout) findViewById(R.id.score_medium_risk);
        RelativeLayoutHighRisk  = (RelativeLayout) findViewById(R.id.score_high_risk);


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
        textViewWarningResult.setText(Integer.toString(warning_result));
        textViewLowRiskResult.setText(Integer.toString(low_risk_result));
        textViewMediumResult.setText(Integer.toString(medium_result));
        textViewHighRiskResult.setText(Integer.toString(high_risk_result));



        RelativeLayoutScoreWarning.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {


//                BlurPopupWindow blurPopupWindow =  new BlurPopupWindow.Builder(v.getContext()).setContentView(R.layout.pop_up_result)
//                        .bindClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(v.getContext(), "Click Button", Toast.LENGTH_SHORT).show();
//                            }
//                        }, R.id.relativeLayout_popup)
//                        .setGravity(Gravity.CENTER)
//                        .setScaleRatio(0.2f)
//                        .setBlurRadius(10)
//                        .setTintColor(0x30000000)
//                        .build();
//                blurPopupWindow.show();
                BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(v.getContext());
                builder.setContentView(R.layout.pop_up_result);
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
                textViewNumResult.setText("Warning Risk: "+warning_result+" Founded");
                popup.setBackgroundColor(Color.parseColor("#13CE66"));

                //Toast.makeText(mContext, ""+recyclerView, Toast.LENGTH_SHORT).show();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                popUp_result mainModel = new popUp_result();
                
                
            

                mainRecyclerAdapter = new popUp_result_adapter(mContext,mainModelArrayListWarning);

                mainRecyclerAdapter.setOnRecyclerViewItemClickListener(ScanResultActivity.this);
                recyclerView.setAdapter(mainRecyclerAdapter);
            }
        });



        RelativeLayoutLowRisk.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {


                BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(v.getContext());
                builder.setContentView(R.layout.pop_up_result);
                builder.setGravity(Gravity.CENTER);
                builder.setScaleRatio(0.2f);
                builder.setBlurRadius(10);
                builder.setTintColor(0x3000000);
                BlurPopupWindow blurPopupWindow = builder.build();
                blurPopupWindow.show();

                RecyclerView recyclerView = blurPopupWindow.findViewById(R.id.recycle_popup_list);
                popup = (LinearLayout)blurPopupWindow.findViewById(R.id.relativeLayout_popup);


                TextView textViewNumResult = (TextView)blurPopupWindow.findViewById(R.id.textView_popup_show_found);
                textViewNumResult.setText("LOW Risk: "+low_risk_result+" Founded !");

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
                builder.setContentView(R.layout.pop_up_result);
                builder.setGravity(Gravity.CENTER);
                builder.setScaleRatio(0.2f);
                builder.setBlurRadius(10);
                builder.setTintColor(0x3000000);
                BlurPopupWindow blurPopupWindow = builder.build();
                blurPopupWindow.show();

                RecyclerView recyclerView = blurPopupWindow.findViewById(R.id.recycle_popup_list);


                TextView textViewNumResult = (TextView)blurPopupWindow.findViewById(R.id.textView_popup_show_found);
                textViewNumResult.setText("Medium Risk: "+medium_result+" Founded !");

                popup = (LinearLayout)blurPopupWindow.findViewById(R.id.relativeLayout_popup);

                popup.setBackgroundResource(R.drawable.shape_score);
                popup.setBackgroundColor(Color.parseColor("#FFBA5C"));

                //Toast.makeText(mContext, ""+recyclerView, Toast.LENGTH_SHORT).show();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                        LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                popUp_result mainModel = new popUp_result();

                mainRecyclerAdapter = new popUp_result_adapter(mContext,mainModelArrayListMedium);

                mainRecyclerAdapter.setOnRecyclerViewItemClickListener(ScanResultActivity.this);
                recyclerView.setAdapter(mainRecyclerAdapter);
            }
        });


        RelativeLayoutHighRisk.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {

                BlurPopupWindow.Builder builder = new BlurPopupWindow.Builder(v.getContext());
                builder.setContentView(R.layout.pop_up_result);
                builder.setGravity(Gravity.CENTER);
                builder.setScaleRatio(0.2f);
                builder.setBlurRadius(10);
                builder.setTintColor(0x3000000);
                BlurPopupWindow blurPopupWindow = builder.build();
                blurPopupWindow.show();

                RecyclerView recyclerView = blurPopupWindow.findViewById(R.id.recycle_popup_list);
                popup = (LinearLayout)blurPopupWindow.findViewById(R.id.relativeLayout_popup);

                TextView textViewNumResult = (TextView)blurPopupWindow.findViewById(R.id.textView_popup_show_found);
                textViewNumResult.setText("High Risk : "+high_risk_result+" Founded !");

                popup.setBackgroundResource(R.drawable.shape_score);
                popup.setBackgroundColor(Color.parseColor("#F95F62"));

                //Toast.makeText(mContext, ""+recyclerView, Toast.LENGTH_SHORT).show();
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext,
                        LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                popUp_result mainModel = new popUp_result();

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
            app_poten_security_temp = reader.getJSONObject("Poten_security");
         } catch (JSONException e) {
                e.printStackTrace();
         }


        // ---------------- JSON scan Warning result   ---------------- //

        try {
            valuesWarning = app_poten_security_temp.getJSONArray("warning");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        warning_result = valuesWarning.length();



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

            popUp_result mainModel = new popUp_result();


            mainModel.setProblem(problem);
            mainModel.setOwasp_num(OWASP);

            mainModelArrayListWarning.add(mainModel);


        }

        // ---------------- JSON scan Low result   ---------------- //

        try {
            valuesLow = app_poten_security_temp.getJSONArray("low");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        low_risk_result = valuesLow.length();

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

            popUp_result mainModel = new popUp_result();

            mainModel.setProblem(problem);
            mainModel.setOwasp_num(OWASP);

            mainModelArrayListLow.add(mainModel);


        }

        // ---------------- JSON scan Medium result   ---------------- //

        try {
            valuesMedium = app_poten_security_temp.getJSONArray("medium");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        medium_result = valuesMedium.length();

        for (int i = 0; i < valuesMedium.length(); i++) {

            String problem = null;
            String OWASP= null;

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

            popUp_result mainModel = new popUp_result();

            mainModel.setProblem(problem);
            mainModel.setOwasp_num(OWASP);

            mainModelArrayListMedium.add(mainModel);


        }

        // ---------------- JSON scan High result   ---------------- //

        try {
            valuesHigh = app_poten_security_temp.getJSONArray("high");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        high_risk_result = valuesHigh.length();

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

            popUp_result mainModel = new popUp_result();

            mainModel.setProblem(problem);
            mainModel.setOwasp_num(OWASP);

            mainModelArrayListHigh.add(mainModel);


        }

    }

    @Override
    public void onItemClick( int position, View view ) {
        popUp_result  mainModel = (popUp_result) view.getTag();
        switch (view.getId()) {
            case R.id.layout_pop_up_result:
                Toast.makeText(this, "Position clicked: " + String.valueOf(position) + ", " + mainModel.getProblem(), Toast.LENGTH_LONG).show();
                //openActivityPortInfo(position);

                Intent intent = new Intent(this,OwaspMobileActivity.class);

                //Bundle
                Bundle bundle = new Bundle();

                switch(mainModel.getOwasp_num()) {
                    case "M1" :
                        bundle.putString("M", "M1");
                        break;
                    case "M2" :
                        bundle.putString("M", "M2");
                        break;
                    case "M3" :
                        bundle.putString("M", "M3");
                        break;
                    case "M4" :
                        bundle.putString("M", "M4");
                        break;
                    case "M5" :
                        bundle.putString("M", "M5");
                        break;
                    case "M6" :
                        bundle.putString("M", "M6");
                        break;
                    case "M7" :
                        bundle.putString("M", "M7");
                        break;
                    case "M8" :
                        bundle.putString("M", "M8");
                        break;
                    case "M9" :
                        bundle.putString("M", "M9");
                        break;
                    case "M10" :
                        bundle.putString("M", "M10");
                        break;

                    default :
                }
                intent.putExtras(bundle);

                startActivity(intent);
                break;
        }
    }


}
