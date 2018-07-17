package org.tvheadend.tvhclient.features.epg;

import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.tvheadend.tvhclient.R;
import org.tvheadend.tvhclient.data.entity.Program;
import org.tvheadend.tvhclient.data.entity.Recording;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EpgViewPagerRecyclerViewAdapter extends RecyclerView.Adapter {

    private final float pixelsPerMinute;
    private final long fragmentStartTime;
    private final long fragmentStopTime;
    private final FragmentActivity activity;
    private Map<Integer, List<Program>> programList = new HashMap<>();
    private Map<Integer, List<Recording>> recordingList = new HashMap<>();

    EpgViewPagerRecyclerViewAdapter(FragmentActivity activity, float pixelsPerMinute, long fragmentStartTime, long fragmentStopTime) {
        this.activity = activity;
        this.pixelsPerMinute = pixelsPerMinute;
        this.fragmentStartTime = fragmentStartTime;
        this.fragmentStopTime = fragmentStopTime;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(viewType, parent, false);
        return new EpgViewPagerViewHolder(activity, view, pixelsPerMinute, fragmentStartTime, fragmentStopTime);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        List<Program> programs = programList.get(position);
        List<Recording> recordings = recordingList.get(position);
        ((EpgViewPagerViewHolder) holder).bindData(programs, recordings);
    }

    void addItems(int position, List<Program> list) {
        programList.remove(position);
        if (list != null) {
            programList.put(position, list);
        }
        notifyDataSetChanged();
    }

    void addRecordings(int position, List<Recording> list) {
        recordingList.remove(position);
        if (list != null) {
            recordingList.put(position, list);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return programList != null ? programList.size() : 0;
    }

    @Override
    public int getItemViewType(final int position) {
        return R.layout.epg_program_list_adapter;
    }

    public void clearItems() {
        programList.clear();
    }
}