package com.example.bookthiti.masai2.iotinformationscreen;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class OwaspRecyclerAdapter extends RecyclerView.Adapter<OwaspRecyclerAdapter.Holder>{
    private List<OwaspModel> owaspModelList;
    private Context context;
    private static HashSet<String> supportedId = new HashSet<String>(Arrays.asList(new String[]{"I2", "I3", "I4", "I7", "M1", "M2", "M3", "M4", "M5", "M9"}));


    public OwaspRecyclerAdapter(Context context, List<OwaspModel> owaspModelList) {
        this.context = context;
        this.owaspModelList = owaspModelList;
    }

    @Override
    public OwaspRecyclerAdapter.Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_owasp, viewGroup, false);
        return new OwaspRecyclerAdapter.Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder Holder, final int position) {
        OwaspModel owaspModel = owaspModelList.get(position);

        Holder.mOwaspIcon.setText(owaspModel.getTopicId());
        Holder.mOwaspTopic.setText(owaspModel.getTopic());
        Spanned htmlAsSpanned = Html.fromHtml(owaspModel.getGeneralDetail());
        Holder.mOwaspDetails.setText(htmlAsSpanned);

        int colorCode = getMatColor("500", Holder.itemView.getResources());

//        String color = String.format("#ff%06X", (0xeeeeee & owaspModel.getTopicId().hashCode()));
        ((GradientDrawable) Holder.mOwaspIcon.getBackground()).setColor(colorCode);
        Holder.mRelativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, OwaspContentActivity.class);
                intent.putExtra("owaspModel", owaspModelList.get(position));
                context.startActivity(intent);
            }
        });
        if (!supportedId.contains(owaspModel.getTopicId())) {
            Holder.mOwaspIconSupported.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public int getItemCount() {
        return owaspModelList.size();
    }

    public class Holder extends RecyclerView.ViewHolder{

        TextView mOwaspIcon;
        TextView mOwaspTopic;
        TextView mOwaspDetails;
        ImageView mOwaspIconSupported;
        RelativeLayout mRelativeLayout;

        public Holder(View view) {
            super(view);
            mOwaspIcon = view.findViewById(R.id.tvOwaspIcon);
            mOwaspTopic = view.findViewById(R.id.tvOwaspTopic);
            mOwaspDetails = view.findViewById(R.id.tvOwaspDetails);
            mOwaspIconSupported = view.findViewById(R.id.ic_is_supported);
            mRelativeLayout = view.findViewById(R.id.layout_owasp);
        }
    }
    private int getMatColor(String typeColor, Resources resources)
    {
        int returnColor = Color.BLACK;
        int arrayId = resources.getIdentifier("mdcolor_" + typeColor, "array", context.getPackageName());

        if (arrayId != 0)
        {
            TypedArray colors = resources.obtainTypedArray(arrayId);
            int index = (int) (Math.random() * colors.length());
            returnColor = colors.getColor(index, Color.BLACK);
            colors.recycle();
        }
        return returnColor;
    }
}

