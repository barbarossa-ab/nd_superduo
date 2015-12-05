package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.text.SimpleDateFormat;
import java.util.Date;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.Utilies;
import barqsoft.footballscores.data.DatabaseContract;

/**
 * Created by barbarossa on 02/12/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScoresRemoteViewsService extends RemoteViewsService {
    public static final int COL_DATE = 1;
    public static final int COL_MATCHTIME = 2;
    public static final int COL_HOME = 3;
    public static final int COL_AWAY = 4;
    public static final int COL_LEAGUE = 5;
    public static final int COL_HOME_GOALS = 6;
    public static final int COL_AWAY_GOALS = 7;
    public static final int COL_ID = 8;
    public static final int COL_MATCHDAY = 9;

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RemoteViewsFactory() {
            private Cursor data = null;

            @Override
            public void onCreate() {
                // Nothing to do
            }

            @Override
            public void onDataSetChanged() {
                if (data != null) {
                    data.close();
                }
                // This method is called by the app hosting the widget (e.g., the launcher)
                // However, our ContentProvider is not exported so it doesn't have access to the
                // data. Therefore we need to clear (and finally restore) the calling identity so
                // that calls use our process and permission
                final long identityToken = Binder.clearCallingIdentity();

                Uri scoresUri = DatabaseContract.ScoresTable.buildScoreWithDate();

                Date date = new Date(System.currentTimeMillis());
                SimpleDateFormat mformat = new SimpleDateFormat("yyyy-MM-dd");

                data = getContentResolver().query(scoresUri,
                        null,
                        null,
                        new String[]{mformat.format(date)},
//                        new String[]{"2015-12-02"},
                        DatabaseContract.ScoresTable.DATE_COL + " ASC");

                Binder.restoreCallingIdentity(identityToken);
            }

            @Override
            public void onDestroy() {
                if (data != null) {
                    data.close();
                    data = null;
                }
            }

            @Override
            public int getCount() {
                return data == null ? 0 : data.getCount();
            }

            public int getViewTypeCount() {
                return 1;
            }

            @Override
            public RemoteViews getViewAt(int position) {
                if (position == AdapterView.INVALID_POSITION ||
                        data == null || !data.moveToPosition(position)) {
                    return null;
                }
                RemoteViews views = new RemoteViews(getPackageName(),
                        R.layout.widget_list_item);

                views.setTextViewText(R.id.matchTime, data.getString(COL_MATCHTIME));
                views.setTextViewText(R.id.matchTeams, data.getString(COL_HOME)
                                                        + " - "
                                                        + data.getString(COL_AWAY));

                views.setTextViewText(R.id.matchScore, Utilies.getScores(
                        data.getInt(COL_HOME_GOALS), data.getInt(COL_AWAY_GOALS))
                );

                if(position % 2 == 0) {
                    views.setInt(R.id.matchInfo, "setBackgroundColor",
                            getResources().getColor(R.color.white));
                } else {
                    views.setInt(R.id.matchInfo, "setBackgroundColor",
                            getResources().getColor(R.color.light_grey));
                }

                Intent fillInIntent = new Intent(getApplicationContext(), MainActivity.class);
                views.setOnClickFillInIntent(R.id.matchInfo, fillInIntent);

                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
                    String matchInfo = getString(R.string.cd_match_detail,
                            data.getString(COL_MATCHTIME),
                            data.getString(COL_HOME),
                            data.getString(COL_AWAY),
                            Utilies.getScoresContentDescription(ScoresRemoteViewsService.this,
                                    data.getInt(COL_HOME_GOALS),
                                    data.getInt(COL_AWAY_GOALS)));

                    views.setContentDescription(R.id.matchInfo, matchInfo);
                }

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

        };
    }
}
