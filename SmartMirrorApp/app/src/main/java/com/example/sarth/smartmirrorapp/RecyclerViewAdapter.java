package com.example.sarth.smartmirrorapp;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.barteksc.pdfviewer.PDFView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable{
    private static final String TAG = "Logcat_RecyclerView";
    private Context mContext;
    public static List<Poster> posterList = new ArrayList<>();
    private List<Poster> searchList = new ArrayList<>();
    private String origin;

    public class ViewHolder extends RecyclerView.ViewHolder {
        PDFView pdfView;
        TextView titleView;
        TextView categoryView;
        TextView nameView;
        TextView statusView;
        RelativeLayout parentLayout;



        public ViewHolder(View itemView) {
            super(itemView);
            pdfView = itemView.findViewById(R.id.recyler_item_pdf);
            titleView = itemView.findViewById(R.id.recyler_item_title);
            categoryView = itemView.findViewById(R.id.recyler_item_category);
            nameView = itemView.findViewById(R.id.recycler_item_name);
            statusView = itemView.findViewById(R.id.recycler_status_content);
            parentLayout = itemView.findViewById(R.id.recycler_layout);
        }
    }

    public RecyclerViewAdapter(Context mContext, List<Poster> posterList, String origin) {
        this.mContext = mContext;
        this.posterList = new ArrayList<>(posterList);
        this.searchList = new ArrayList<>(posterList);
        this.origin = origin;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycleritem,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Log.i(TAG,"OnBindViewHolder is called");

        final Poster poster = posterList.get(position);
        holder.setIsRecyclable(false);
        holder.pdfView.fromBytes(poster.data).load();
        holder.titleView.setText(String.format("Title: %s", poster.title));
        holder.categoryView.setText(String.format("Category: %s", poster.category));
        holder.nameView.setText(String.format("By: %s", poster.name));
        if (poster.status.equals("pending")) {
            holder.statusView.setText("Pending");
            holder.statusView.setTextColor(Color.RED);
        } else if (poster.status.equals("approved")) {
            holder.statusView.setText("Accepted");
            holder.statusView.setTextColor(Color.GREEN  );
        } else if (poster.status.equals("posted")) {
            holder.statusView.setText("On Display");
            holder.statusView.setTextColor(Color.BLUE);
        } else if (poster.status.equals("expired")) {
            holder.statusView.setText("Expired");
            holder.statusView.setTextColor(Color.GRAY);
        } else if (poster.status.equals("rejected")) {
            holder.statusView.setText("Rejected");
            holder.statusView.setTextColor(Color.GRAY);
        }
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent judgementTime = new Intent(mContext, JudgementActivity.class);
                judgementTime.putExtra("Position", position);
                judgementTime.putExtra("Origin",origin);
                mContext.startActivity(judgementTime);
            }
        });
    }

    @Override
    public int getItemCount() {
        return posterList.size();
    }

    @Override
    public Filter getFilter() {
        return everythingFilter;
    }
    public Filter getCategoryFilter() {
        return categoryFilter;
    }

    public Filter getNameFilter() {
        return nameFilter;
    }

    public Filter getTitleFilter() {
        return titleFilter;
    }

    public Filter getStatusFilter() {
        return  statusFilter;
    }

    private Filter everythingFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Poster> filtered_list = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                Log.i(TAG, String.valueOf(searchList.size()));
                filtered_list.addAll(searchList);
            } else {
                String criteria = constraint.toString().toLowerCase().trim();
                for (Poster p : searchList) {
                    if (p.everything.toLowerCase().contains(criteria)) {
                        filtered_list.add(p);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered_list;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            posterList.clear();
            posterList.addAll((List<Poster>)results.values);
            notifyDataSetChanged();
        }
    };

    private Filter titleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Poster> filtered_list = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered_list.addAll(searchList);
            } else {
                String criteria = constraint.toString().toLowerCase().trim();
                for (Poster p : searchList) {
                    if (p.title.toLowerCase().contains(criteria)) {
                        filtered_list.add(p);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered_list;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            posterList.clear();
            posterList.addAll((List<Poster>)results.values);
            notifyDataSetChanged();
        }
    };

    private Filter categoryFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Poster> filtered_list = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered_list.addAll(searchList);
            } else {
                String criteria = constraint.toString().toLowerCase().trim();
                for (Poster p : searchList) {
                    if (p.category.toLowerCase().startsWith(criteria)) {
                        filtered_list.add(p);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered_list;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            posterList.clear();
            posterList.addAll((List<Poster>)results.values);
            notifyDataSetChanged();
        }
    };

    private Filter nameFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Poster> filtered_list = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered_list.addAll(searchList);
            } else {
                String criteria = constraint.toString().toLowerCase().trim();
                for (Poster p : searchList) {
                    if (p.name.toLowerCase().contains(criteria)) {
                        filtered_list.add(p);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered_list;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            posterList.clear();
            posterList.addAll((List<Poster>)results.values);
            notifyDataSetChanged();
        }
    };


    private Filter statusFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<Poster> filtered_list = new ArrayList<>();

            if (constraint == null || constraint.length() == 0) {
                filtered_list.addAll(searchList);
            } else {
                String criteria = constraint.toString().toLowerCase().trim();
                for (Poster p : searchList) {
                    if (p.status_filter.toLowerCase().contains(criteria)) {
                        filtered_list.add(p);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filtered_list;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            posterList.clear();
            posterList.addAll((List<Poster>)results.values);
            notifyDataSetChanged();
        }
    };

    public void swap(List<Poster> posters) {
        posterList.clear();
        posterList.addAll(posters);
        Collections.sort(searchList,Poster.TitleAscending);
        notifyDataSetChanged();
    }

    public void sort(Comparator<Poster> comparator) {
        Collections.sort(posterList,comparator);
        Collections.sort(searchList,comparator);
        notifyDataSetChanged();
    }



}
