package com.example.bookthiti.masai2.mobileapplicationscanningscreen.scanresultscreen;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.bookthiti.masai2.R;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OwaspCategoryRecyclerAdapter extends RecyclerView.Adapter<OwaspCategoryRecyclerAdapter.ViewHolder> {

    private Context context;
    private Map<String, List<AppVulnerability>> owaspCategory;
    private List<Map.Entry<String, List<AppVulnerability>>> entryList;

    public OwaspCategoryRecyclerAdapter(Context context, Map<String, List<AppVulnerability>> owaspCategory) {
        this.context = context;
        setOwaspCategory(owaspCategory);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vulner_on_owasp, viewGroup, false);
        return new OwaspCategoryRecyclerAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Map.Entry<String, List<AppVulnerability>> entry = entryList.get(i);
        String owaspId = entry.getKey();
        List<AppVulnerability> list = entry.getValue();
        Pattern pattern = Pattern.compile("\\[([MI]\\d+).+\\]");
        Matcher matcher = pattern.matcher(owaspId);
        if (matcher.matches()) {
            viewHolder.textViewOwaspId.setText(matcher.group(1));
        }
        double avg = 0;
        for (AppVulnerability appVulnerability : list) {
            avg += appVulnerability.getCvss();
        }
        avg /= list.size();
        viewHolder.progressBar.setMax(10);
        viewHolder.progressBar.setProgress((int) Math.round(avg));
        viewHolder.textViewOwaspScore.setText(String.format("%.1f", avg));
    }

    @Override
    public int getItemCount() {
        return owaspCategory.size();
    }

    public void setOwaspCategory(Map<String, List<AppVulnerability>> owaspCategory) {
        this.owaspCategory = owaspCategory;
        this.entryList = new ArrayList<>();
        Iterator<Map.Entry<String, List<AppVulnerability>>> iterator = owaspCategory.entrySet().iterator();
        while(iterator.hasNext()) {
            this.entryList.add(iterator.next());
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewOwaspId;
        private TextView textViewOwaspScore;
        private ProgressBar progressBar;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOwaspId = itemView.findViewById(R.id.text_owasp_category);
            textViewOwaspScore = itemView.findViewById(R.id.text_owasp_category_score);
            progressBar = itemView.findViewById(R.id.progress_owasp_category);
        }
    }
}
