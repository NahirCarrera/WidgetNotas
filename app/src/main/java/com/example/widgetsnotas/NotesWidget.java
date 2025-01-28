package com.example.widgetsnotas;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

public class NotesWidget extends AppWidgetProvider {

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_layout);

            // Configurar el adaptador remoto para el ListView
            Intent intent = new Intent(context, NotesWidgetService.class);
            views.setRemoteAdapter(R.id.widget_list, intent);

            // Configurar evento para el botón de añadir nota
            Intent addNoteIntent = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, addNoteIntent, PendingIntent.FLAG_IMMUTABLE);
            views.setOnClickPendingIntent(R.id.add_note_icon, pendingIntent);

            appWidgetManager.updateAppWidget(appWidgetId, views);
        }
    }
}
