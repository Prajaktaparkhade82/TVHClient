package org.tvheadend.tvhclient.features.dvr.recordings;

import android.app.SearchManager;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.View;

import org.tvheadend.tvhclient.R;
import org.tvheadend.tvhclient.features.search.SearchActivity;
import org.tvheadend.tvhclient.features.search.SearchRequestInterface;

public class CompletedRecordingListFragment extends RecordingListFragment implements SearchRequestInterface {

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        toolbarInterface.setTitle(getString(R.string.completed_recordings));

        RecordingViewModel viewModel = ViewModelProviders.of(activity).get(RecordingViewModel.class);
        viewModel.getCompletedRecordings().observe(this, recordings -> {

            recyclerView.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.GONE);

            recyclerViewAdapter.addItems(recordings);
            if (!TextUtils.isEmpty(searchQuery)) {
                recyclerViewAdapter.getFilter().filter(searchQuery, this);
            }

            toolbarInterface.setSubtitle(getResources().getQuantityString(R.plurals.recordings, recyclerViewAdapter.getItemCount(), recyclerViewAdapter.getItemCount()));

            if (isDualPane && recyclerViewAdapter.getItemCount() > 0) {
                showRecordingDetails(selectedListPosition);
            }
            activity.invalidateOptionsMenu();
        });
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // Show the casting icon when finished recordings are available.
        menu.findItem(R.id.media_route_menu_item).setVisible(true);
    }

    @Override
    public void onSearchRequested(String query) {
        Intent searchIntent = new Intent(activity, SearchActivity.class);
        searchIntent.putExtra(SearchManager.QUERY, query);
        searchIntent.setAction(Intent.ACTION_SEARCH);
        searchIntent.putExtra("type", "completed_recordings");
        startActivity(searchIntent);
    }
}
