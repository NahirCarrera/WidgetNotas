package com.example.widgetsnotas;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import java.util.ArrayList;

public class NotesWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new NotesRemoteViewsFactory(getApplicationContext());
    }

    class NotesRemoteViewsFactory implements RemoteViewsFactory {
        private Context context;
        private ArrayList<String> notes;

        NotesRemoteViewsFactory(Context context) {
            this.context = context;
        }

        @Override
        public void onCreate() {
            notes = new ArrayList<>();
        }


        @Override
        public void onDataSetChanged() {
            SharedPreferences sharedPreferences = context.getSharedPreferences("NotesWidget", Context.MODE_PRIVATE);
            notes.clear();
            int size = sharedPreferences.getInt("note_count", 0);
            for (int i = 0; i < size; i++) {
                notes.add(sharedPreferences.getString("note_" + i, ""));
            }
        }

        @Override
        public void onDestroy() {
            notes.clear();
        }

        @Override
        public int getCount() {
            return notes.size();
        }

        @Override
        public RemoteViews getViewAt(int position) {
            RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_note_item);
            views.setTextViewText(R.id.note_text, notes.get(position));
            return views;
        }

        @Override
        public RemoteViews getLoadingView() {
            return null;
        }

        @Override
        public int getViewTypeCount() {
            return 1;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }
    }
}
