package org.tvheadend.tvhclient.adapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.tvheadend.tvhclient.Constants;
import org.tvheadend.tvhclient.DataStorage;
import org.tvheadend.tvhclient.R;
import org.tvheadend.tvhclient.interfaces.FragmentStatusInterface;
import org.tvheadend.tvhclient.model.Channel2;
import org.tvheadend.tvhclient.model.Recording2;
import org.tvheadend.tvhclient.utils.MiscUtils;
import org.tvheadend.tvhclient.utils.Utils;

import java.util.Comparator;
import java.util.List;

public class RecordingListAdapter extends ArrayAdapter<Recording2> {

    private final Activity context;
    private final List<Recording2> list;
    private int selectedPosition = 0;
    private final int layout;

    public RecordingListAdapter(Activity context, List<Recording2> list, int layout) {
        super(context, layout, list);
        this.context = context;
        this.layout = layout;
        this.list = list;
    }

    public void sort(final int type) {
        switch (type) {
        case Constants.RECORDING_SORT_ASCENDING:
            sort(new Comparator<Recording2>() {
                public int compare(Recording2 x, Recording2 y) {
                    if (y.start == x.start) {
                        return 1;
                    } else if (x.start < y.start){
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
        break;
        case Constants.RECORDING_SORT_DESCENDING:
            sort(new Comparator<Recording2>() {
                public int compare(Recording2 x, Recording2 y) {
                    if (y.start == x.start) {
                        return 1;
                    } else if (x.start > y.start){
                        return -1;
                    } else {
                        return 1;
                    }
                }
            });
            break;
        }
    }

    public void setPosition(int pos) {
        selectedPosition = pos;
    }

    public List<Recording2> getAllItems() {
        return list;
    }

    static class ViewHolder {
        public ImageView icon;
        public TextView icon_text;
        public TextView title;
        public TextView subtitle;
        public ImageView state;
        public TextView is_series_recording;
        public TextView is_timer_recording;
        public TextView channel;
        public TextView time;
        public TextView date;
        public TextView duration;
        public TextView summary;
        public TextView description;
        public TextView failed_reason;
        public TextView isEnabled;
        public ImageView dual_pane_list_item_selection;
    }
    
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        ViewHolder holder;

        if (view == null) {
            view = context.getLayoutInflater().inflate(layout, parent, false);
            holder = new ViewHolder();
            holder.icon = view.findViewById(R.id.icon);
            holder.icon_text = view.findViewById(R.id.icon_text);
            holder.title = view.findViewById(R.id.title);
            holder.subtitle = view.findViewById(R.id.subtitle);
            holder.state = view.findViewById(R.id.state);
            holder.is_series_recording = view.findViewById(R.id.is_series_recording);
            holder.is_timer_recording = view.findViewById(R.id.is_timer_recording);
            holder.channel = view.findViewById(R.id.channel);
            holder.time = view.findViewById(R.id.time);
            holder.date = view.findViewById(R.id.date);
            holder.duration = view.findViewById(R.id.duration);
            holder.summary = view.findViewById(R.id.summary);
            holder.description = view.findViewById(R.id.description);
            holder.failed_reason = view.findViewById(R.id.failed_reason);
            holder.isEnabled = view.findViewById(R.id.enabled);
            holder.dual_pane_list_item_selection = view.findViewById(R.id.dual_pane_list_item_selection);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        if (holder.dual_pane_list_item_selection != null) {
            // Set the correct indication when the dual pane mode is active
            // If the item is selected the the arrow will be shown, otherwise
            // only a vertical separation line is displayed.                
            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
            final boolean lightTheme = prefs.getBoolean("lightThemePref", true);

            if (selectedPosition == position) {
                final int icon = (lightTheme) ? R.drawable.dual_pane_selector_active_light : R.drawable.dual_pane_selector_active_dark;
                holder.dual_pane_list_item_selection.setBackgroundResource(icon);
            } else {
                final int icon = R.drawable.dual_pane_selector_inactive;
                holder.dual_pane_list_item_selection.setBackgroundResource(icon);
            }
        }

        // Get the program and assign all the values
        final Recording2 rec = getItem(position);
        if (rec != null) {
            Channel2 channel = DataStorage.getInstance().getChannelFromArray(rec.channel);
            holder.title.setText(rec.title);
            if (holder.channel != null && channel != null) {
                holder.channel.setText(channel.channelName);
            }

            if (rec.subtitle != null && rec.subtitle.length() > 0) {
                holder.subtitle.setVisibility(View.VISIBLE);
                holder.subtitle.setText(rec.subtitle);
            } else {
                holder.subtitle.setVisibility(View.GONE);
            }

            // Show the channel icon if available and set in the preferences.
            // If not chosen, hide the imageView and show the channel name.
            if (holder.icon != null && holder.icon_text != null) {
                SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
                boolean showChannelIcons = prefs.getBoolean("showIconPref", true);

                Bitmap iconBitmap = MiscUtils.getCachedIcon(context, channel.channelIcon);
                // Show the icon or a blank one if it does not exist
                holder.icon.setImageBitmap(iconBitmap);
                holder.icon_text.setText(channel.channelName);
                // Show the channels icon if set in the preferences.
                // If not then hide the icon and show the channel name as a placeholder
                holder.icon.setVisibility(showChannelIcons ? ImageView.VISIBLE : ImageView.GONE);
                holder.icon_text.setVisibility(showChannelIcons ? ImageView.GONE : ImageView.VISIBLE);
            }


            Utils.setDate2(holder.date, rec.start);
            Utils.setTime2(holder.time, rec.start, rec.stop);
            Utils.setDuration2(holder.duration, rec.start, rec.stop);
            Utils.setDescription(null, holder.summary, rec.summary);
            Utils.setDescription(null, holder.description, rec.description);
            // TODO Utils.setFailedReason(holder.failed_reason, rec);
            
            // Show only the recording icon
            if (holder.state != null) {
                if (rec.isRecording()) {
                    holder.state.setImageResource(R.drawable.ic_rec_small);
                    holder.state.setVisibility(ImageView.VISIBLE);
                } else {
                    holder.state.setVisibility(ImageView.GONE);
                }
            }

            // Show the information if the recording belongs to a series recording
            if (holder.is_series_recording != null) {
                if (rec.autorecId != null) {
                    holder.is_series_recording.setVisibility(ImageView.VISIBLE);
                } else {
                    holder.is_series_recording.setVisibility(ImageView.GONE);
                }
            }
            // Show the information if the recording belongs to a series recording
            if (holder.is_timer_recording != null) {
                if (rec.timerecId != null) {
                    holder.is_timer_recording.setVisibility(ImageView.VISIBLE);
                } else {
                    holder.is_timer_recording.setVisibility(ImageView.GONE);
                }
            }

            // If activated in the settings allow playing the recording by  
            // selecting the channel icon if the recording is completed or currently
            // being recorded
            if (holder.icon != null && (rec.isCompleted() || rec.isRecording())) {
                holder.icon.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (context instanceof FragmentStatusInterface) {
                            ((FragmentStatusInterface) context).onListItemSelected(position, rec, Constants.TAG_CHANNEL_ICON);
                        }
                    }
                });
            }

            if (holder.isEnabled != null) {
                holder.isEnabled.setVisibility((DataStorage.getInstance().getProtocolVersion() >= Constants.MIN_API_VERSION_DVR_FIELD_ENABLED && rec.enabled > 0) ? View.VISIBLE : View.GONE);
                holder.isEnabled.setText(rec.enabled > 0? R.string.recording_enabled : R.string.recording_disabled);
            }
        }
        return view;
    }

    public void update(Recording2 rec) {
        int length = list.size();

        // Go through the list of programs and find the
        // one with the same id. If its been found, replace it.
        for (int i = 0; i < length; ++i) {
            if (list.get(i).id == rec.id) {
                list.set(i, rec);
                break;
            }
        }
    }
    
    public Recording2 getSelectedItem() {
        if (list.size() > 0 && list.size() > selectedPosition) {
            return list.get(selectedPosition);
        }
        return null;
    }
}