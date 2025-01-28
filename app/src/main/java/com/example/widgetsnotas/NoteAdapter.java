package com.example.widgetsnotas;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.appwidget.AppWidgetManager; // Import necesario
import android.content.ComponentName;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


import java.util.ArrayList;

public class NoteAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> notes;

    public NoteAdapter(Context context, ArrayList<String> notes) {
        this.context = context;
        this.notes = notes;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.note_item, parent, false);
        }

        TextView noteText = convertView.findViewById(R.id.note_text);
        Button deleteButton = convertView.findViewById(R.id.delete_note_button);

        // Configurar el texto de la nota
        noteText.setText(notes.get(position));

        // Mostrar la nota completa al hacer clic en ella
        noteText.setOnClickListener(v -> {
            // Crear un cuadro de diálogo para mostrar la nota completa
            new AlertDialog.Builder(context)
                    .setTitle("Nota completa")
                    .setMessage(notes.get(position))
                    .setPositiveButton("Cerrar", (dialog, which) -> {
                        dialog.dismiss(); // Cerrar el cuadro de diálogo
                    })
                    .show();
        });

        // Configurar el botón de eliminar
        deleteButton.setOnClickListener(v -> {
            // Crear un cuadro de diálogo para confirmar la eliminación
            new AlertDialog.Builder(context)
                    .setTitle("Confirmar eliminación")
                    .setMessage("¿Estás seguro de que quieres eliminar esta nota?")
                    .setPositiveButton("Eliminar", (dialog, which) -> {
                        // Eliminar la nota si el usuario confirma
                        notes.remove(position);
                        notifyDataSetChanged();
                        saveNotesToSharedPreferences();
                        updateWidget();
                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> {
                        // Cerrar el diálogo sin hacer nada
                        dialog.dismiss();
                    })
                    .show();
        });

        return convertView;
    }

    private void saveNotesToSharedPreferences() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("NotesWidget", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("note_count", notes.size());
        for (int i = 0; i < notes.size(); i++) {
            editor.putString("note_" + i, notes.get(i));
        }

        editor.apply();

        // Notificar al widget que los datos han cambiado
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName widget = new ComponentName(context, NotesWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widget);
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);
    }


    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
        ComponentName widget = new ComponentName(context, NotesWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widget);
        NotesWidget widgetProvider = new NotesWidget();
        widgetProvider.onUpdate(context, appWidgetManager, appWidgetIds);
    }
}
