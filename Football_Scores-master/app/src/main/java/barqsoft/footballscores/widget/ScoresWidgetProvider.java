package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.TaskStackBuilder;
import android.widget.RemoteViews;

import barqsoft.footballscores.MainActivity;
import barqsoft.footballscores.R;
import barqsoft.footballscores.service.FetchService;

/**
 * Created by barbarossa on 12/1/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScoresWidgetProvider extends AppWidgetProvider {

    @Override
    public void onReceive(@NonNull Context context, @NonNull Intent intent) {
        super.onReceive(context, intent);
        if (FetchService.ACTION_DATA_UPDATED.equals(intent.getAction())) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(
                    new ComponentName(context, getClass()));

            appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_matches_list);
        }
    }


    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        Intent service_start = new Intent(context, FetchService.class);
        context.startService(service_start);

        // update each of the widgets with the remote adapter
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_today_matches);

            Intent intent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            views.setOnClickPendingIntent(R.id.widget, pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                setRemoteAdapter(context, views);
            } else {
                setRemoteAdapterV11(context, views);
            }

            Intent clickIntentTemplate = new Intent(context, MainActivity.class);
            PendingIntent clickPendingIntentTemplate = TaskStackBuilder.create(context)
                    .addNextIntentWithParentStack(clickIntentTemplate)
                    .getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_matches_list, clickPendingIntentTemplate);

            views.setEmptyView(R.id.widget_matches_list, R.id.widget_no_matches_text);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }


    @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
    private void setRemoteAdapter(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(R.id.widget_matches_list,
                new Intent(context, ScoresRemoteViewsService.class));
    }

    @SuppressWarnings("deprecation")
    private void setRemoteAdapterV11(Context context, @NonNull final RemoteViews views) {
        views.setRemoteAdapter(0, R.id.widget_matches_list,
                new Intent(context, ScoresRemoteViewsService.class));
    }

}
