package barqsoft.footballscores.widget;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.content.Intent;
import android.os.Build;

/**
 * Created by barbarossa on 02/12/15.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScoresWidgetIntentService extends IntentService {

    public ScoresWidgetIntentService() {
        super("ScoresWidgetIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }
}