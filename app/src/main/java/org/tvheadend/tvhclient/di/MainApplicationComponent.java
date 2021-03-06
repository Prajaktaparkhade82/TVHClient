package org.tvheadend.tvhclient.di;

import android.content.Context;
import android.content.SharedPreferences;

import org.tvheadend.tvhclient.MainApplication;
import org.tvheadend.tvhclient.data.repository.AppRepository;
import org.tvheadend.tvhclient.data.service.HtspIntentService;
import org.tvheadend.tvhclient.data.service.HtspService;
import org.tvheadend.tvhclient.di.modules.MainApplicationModule;
import org.tvheadend.tvhclient.di.modules.RepositoryModule;
import org.tvheadend.tvhclient.di.modules.SharedPreferencesModule;
import org.tvheadend.tvhclient.ui.base.BaseFragment;
import org.tvheadend.tvhclient.ui.features.MainActivity;
import org.tvheadend.tvhclient.ui.features.channels.BaseChannelViewModel;
import org.tvheadend.tvhclient.ui.features.download.DownloadRecordingManager;
import org.tvheadend.tvhclient.ui.features.dvr.recordings.RecordingViewModel;
import org.tvheadend.tvhclient.ui.features.dvr.series_recordings.SeriesRecordingViewModel;
import org.tvheadend.tvhclient.ui.features.dvr.timer_recordings.TimerRecordingViewModel;
import org.tvheadend.tvhclient.ui.features.epg.EpgViewPagerFragment;
import org.tvheadend.tvhclient.ui.features.playback.external.BasePlaybackActivity;
import org.tvheadend.tvhclient.ui.features.playback.external.ExternalPlayerViewModel;
import org.tvheadend.tvhclient.ui.features.playback.internal.PlaybackActivity;
import org.tvheadend.tvhclient.ui.features.playback.internal.PlayerViewModel;
import org.tvheadend.tvhclient.ui.features.programs.ProgramViewModel;
import org.tvheadend.tvhclient.ui.features.settings.BasePreferenceFragment;
import org.tvheadend.tvhclient.ui.features.settings.ConnectionViewModel;
import org.tvheadend.tvhclient.ui.features.settings.SettingsConnectionBaseFragment;
import org.tvheadend.tvhclient.ui.features.settings.SettingsListConnectionsFragment;
import org.tvheadend.tvhclient.ui.features.startup.StartupFragment;
import org.tvheadend.tvhclient.util.MigrateUtils;
import org.tvheadend.tvhclient.ui.common.MenuUtils;

import javax.inject.Singleton;

import dagger.Component;

@Component(modules = {
        MainApplicationModule.class,
        SharedPreferencesModule.class,
        RepositoryModule.class})
@Singleton
public interface MainApplicationComponent {

    Context context();

    SharedPreferences sharedPreferences();

    AppRepository appRepository();

    void inject(MainApplication mainApplication);

    void inject(RecordingViewModel recordingViewModel);

    void inject(BaseFragment baseFragment);

    void inject(BasePreferenceFragment basePreferenceFragment);

    void inject(ProgramViewModel programViewModel);

    void inject(TimerRecordingViewModel timerRecordingViewModel);

    void inject(SeriesRecordingViewModel seriesRecordingViewModel);

    void inject(BasePlaybackActivity basePlayActivity);

    void inject(MenuUtils menuUtils);

    void inject(SettingsListConnectionsFragment settingsListConnectionsFragment);

    void inject(ConnectionViewModel connectionViewModel);

    void inject(MigrateUtils migrateUtils);

    void inject(DownloadRecordingManager downloadRecordingManager);

    void inject(MainActivity mainActivity);

    void inject(StartupFragment startupFragment);

    void inject(EpgViewPagerFragment epgViewPagerFragment);

    void inject(SettingsConnectionBaseFragment settingsConnectionBaseFragment);

    void inject(BaseChannelViewModel baseChannelViewModel);

    void inject(HtspService htspService);

    void inject(HtspIntentService htspIntentService);

    void inject(PlaybackActivity playbackActivity);

    void inject(PlayerViewModel playerViewModel);

    void inject(ExternalPlayerViewModel externalPlayerViewModel);
}
