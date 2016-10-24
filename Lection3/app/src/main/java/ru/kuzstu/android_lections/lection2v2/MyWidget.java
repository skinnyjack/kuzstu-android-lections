package ru.kuzstu.android_lections.lection2v2;

import java.util.Arrays;

import android.app.PendingIntent;
import android.appwidget.AppWidgetHost;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.RemoteViews;

/**
 * Created by dustwind2 on 23.10.2016.
 */

public class MyWidget extends AppWidgetProvider {

    final String LOG_TAG = "myLogs";
    private static final String UberiteEtoOtsuda = "uberiteEtoOtsuda";
    public final static String WIDGET_COUNT = "widget_count_";
    public final static String WIDGET_PREF = "widget_pref";

    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Log.d(LOG_TAG, "onEnabled");
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager,
                         int[] appWidgetIds) {
        super.onUpdate(context, appWidgetManager, appWidgetIds);
        for (int i : appWidgetIds) {
            updateWidget(context, appWidgetManager, i);
        }

        Log.d(LOG_TAG, "onUpdate " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        super.onDeleted(context, appWidgetIds);
        Log.d(LOG_TAG, "onDeleted " + Arrays.toString(appWidgetIds));
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Log.d(LOG_TAG, "onDisabled");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);

        String action = intent.getAction();
        String actionName = UberiteEtoOtsuda;
        if (intent.getAction().equalsIgnoreCase(actionName)) {

            int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
            Bundle extras = intent.getExtras();
            if (extras != null) {
                mAppWidgetId = extras.getInt(
                        AppWidgetManager.EXTRA_APPWIDGET_ID,
                        AppWidgetManager.INVALID_APPWIDGET_ID);

            }
            if (mAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
                SharedPreferences sp = context.getSharedPreferences(
                        MyWidget.WIDGET_PREF, Context.MODE_PRIVATE);
                int cnt = sp.getInt(MyWidget.WIDGET_COUNT + mAppWidgetId, 0);
                sp.edit().putInt(MyWidget.WIDGET_COUNT + mAppWidgetId,
                        ++cnt).commit();

                // Обновляем виджет
                updateWidget(context, AppWidgetManager.getInstance(context),
                        mAppWidgetId);
            }
        }
    }

    static void updateWidget(Context ctx, AppWidgetManager appWidgetManager,
                             int widgetID) {
        SharedPreferences sp = ctx.getSharedPreferences(
                MyWidget.WIDGET_PREF, Context.MODE_PRIVATE);

        String count = String.valueOf(sp.getInt(MyWidget.WIDGET_COUNT
                + widgetID, 0));

        RemoteViews widgetView = new RemoteViews(ctx.getPackageName(),
                R.layout.widget);
        widgetView.setTextViewText(R.id.tv, ctx.getString(R.string.widget_text)+" "+ count);

        Intent intent = new Intent(ctx, MyWidget.class);
        intent.setAction(UberiteEtoOtsuda);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, widgetID);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx,
                widgetID, intent, 0);
        widgetView.setOnClickPendingIntent(R.id.btnwidget, pendingIntent);

        // Обновляем виджет
        appWidgetManager.updateAppWidget(widgetID, widgetView);
    }

}