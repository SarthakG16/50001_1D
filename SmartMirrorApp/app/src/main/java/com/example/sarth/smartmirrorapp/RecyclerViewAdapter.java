package com.example.sarth.smartmirrorapp;

import android.content.Context;
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
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> implements Filterable{
    private static final String TAG = "Logcat_RecyclerView";
    private Context mContext;
    private List<Poster> posterList = new ArrayList<>();
    private List<Poster> searchList = new ArrayList<>();

    public class ViewHolder extends RecyclerView.ViewHolder {
        PDFView pdfView;
        TextView titleView;
        TextView categoryView;
        TextView nameView;
        RelativeLayout parentLayout;



        public ViewHolder(View itemView) {
            super(itemView);
            pdfView = itemView.findViewById(R.id.recyler_item_pdf);
            titleView = itemView.findViewById(R.id.recyler_item_title);
            categoryView = itemView.findViewById(R.id.recyler_item_category);
            nameView = itemView.findViewById(R.id.recycler_item_name);
            parentLayout = itemView.findViewById(R.id.recycler_layout);

        }
    }

    public RecyclerViewAdapter(Context mContext, List<Poster> posterList) {
        this.mContext = mContext;
        this.posterList = posterList;
        this.searchList = new ArrayList<>(posterList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_recycleritem,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.i(TAG,"OnBindViewHolder is called");
        Poster poster = posterList.get(position);
        holder.pdfView.fromBytes(poster.data).load();
        holder.titleView.setText(poster.title);
        holder.categoryView.setText(String.format("Category: %s", poster.category));
        holder.nameView.setText(String.format("By: %s", poster.name));
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Intent to Approve/Reject OR DialogBox to Approve/Reject.
                Toast.makeText(mContext, "Go To Request Approval Page", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return posterList.size();
    }

    @Override
    public Filter getFilter() {
        return titleFilter;
    }
    public Filter getCategoryFilter() {
        return categoryFilter;
    }

    public Filter getNameFilter() {
        return nameFilter;
    }

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



}
