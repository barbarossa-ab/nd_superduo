package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.concurrent.ExecutionException;

import barqsoft.footballscores.R;
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

                data = getContentResolver().query(scoresUri,
                        null,
                        null,
                        null,
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

//                int weatherId = data.getInt(INDEX_WEATHER_CONDITION_ID);
//                int weatherArtResourceId = Utility.getIconResourceForWeatherCondition(weatherId);
//                Bitmap weatherArtImage = null;
//                if ( !Utility.usingLocalGraphics(DetailWidgetRemoteViewsService.this) ) {
//                    String weatherArtResourceUrl = Utility.getArtUrlForWeatherCondition(
//                            DetailWidgetRemoteViewsService.this, weatherId);
//                    try {
//                        weatherArtImage = Glide.with(DetailWidgetRemoteViewsService.this)
//                                .load(weatherArtResourceUrl)
//                                .asBitmap()
//                                .error(weatherArtResourceId)
//                                .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
//                    } catch (InterruptedException | ExecutionException e) {
//                        Log.e(LOG_TAG, "Error retrieving large icon from " + weatherArtResourceUrl, e);
//                    }
//                }
//                String description = data.getString(INDEX_WEATHER_DESC);
//                long dateInMillis = data.getLong(INDEX_WEATHER_DATE);
//                String formattedDate = Utility.getFriendlyDayString(
//                        DetailWidgetRemoteViewsService.this, dateInMillis, false);
//                double maxTemp = data.getDouble(INDEX_WEATHER_MAX_TEMP);
//                double minTemp = data.getDouble(INDEX_WEATHER_MIN_TEMP);
//                String formattedMaxTemperature =
//                        Utility.formatTemperature(DetailWidgetRemoteViewsService.this, maxTemp);
//                String formattedMinTemperature =
//                        Utility.formatTemperature(DetailWidgetRemoteViewsService.this, minTemp);
//                if (weatherArtImage != null) {
//                    views.setImageViewBitmap(R.id.widget_icon, weatherArtImage);
//                } else {
//                    views.setImageViewResource(R.id.widget_icon, weatherArtResourceId);
//                }
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH_MR1) {
//                    setRemoteContentDescription(views, description);
//                }
//                views.setTextViewText(R.id.widget_date, formattedDate);
//                views.setTextViewText(R.id.widget_description, description);
//                views.setTextViewText(R.id.widget_high_temperature, formattedMaxTemperature);
//                views.setTextViewText(R.id.widget_low_temperature, formattedMinTemperature);
//
//                final Intent fillInIntent = new Intent();
//                String locationSetting =
//                        Utility.getPreferredLocation(DetailWidgetRemoteViewsService.this);
//                Uri weatherUri = WeatherContract.WeatherEntry.buildWeatherLocationWithDate(
//                        locationSetting,
//                        dateInMillis);
//                fillInIntent.setData(weatherUri);
//                views.setOnClickFillInIntent(R.id.widget_list_item, fillInIntent);

                return views;
            }

            @Override
            public RemoteViews getLoadingView() {
                return new RemoteViews(getPackageName(), R.layout.widget_list_item);
            }

            @Override
            public long getItemId(int position) {
//                if (data.moveToPosition(position))
//                    return data.getLong(INDEX_WEATHER_ID);
                return position;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

        };
    }
}
