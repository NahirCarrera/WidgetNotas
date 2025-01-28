package com.example.widgetsnotas;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ArrayList<String> notes;
    private NoteAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Inicializar la lista de notas
        notes = new ArrayList<>();

        // Configurar el adaptador personalizado
        adapter = new NoteAdapter(this, notes);

        // Configurar el ListView
        ListView notesList = findViewById(R.id.notes_list);
        notesList.setAdapter(adapter);

        // Referencias a los elementos de la interfaz
        EditText noteInput = findViewById(R.id.note_input);
        Button addNoteButton = findViewById(R.id.add_note_button);

        // Listener para el botón de añadir nota
        addNoteButton.setOnClickListener(v -> {
            String note = noteInput.getText().toString();
            if (!note.isEmpty()) {
                notes.add(note);
                adapter.notifyDataSetChanged();
                noteInput.setText("");

                // Guardar las notas en SharedPreferences
                saveNotesToSharedPreferences();

                // Actualizar el widget
                updateWidget();

                // Mostrar mensaje de confirmación
                Toast.makeText(MainActivity.this, "Nota añadida", Toast.LENGTH_SHORT).show();
            }
        });

        // Cargar notas guardadas al iniciar la actividad
        loadNotesFromSharedPreferences();
    }

    private void loadNotesFromSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("NotesWidget", MODE_PRIVATE);
        notes.clear();
        int size = sharedPreferences.getInt("note_count", 0);
        for (int i = 0; i < size; i++) {
            notes.add(sharedPreferences.getString("note_" + i, ""));
        }
        adapter.notifyDataSetChanged();
    }

    private void saveNotesToSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("NotesWidget", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putInt("note_count", notes.size());
        for (int i = 0; i < notes.size(); i++) {
            editor.putString("note_" + i, notes.get(i));
        }

        editor.apply();
    }


    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName widget = new ComponentName(this, NotesWidget.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(widget);

        // Notificar al widget que los datos han cambiado
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_list);

        NotesWidget widgetProvider = new NotesWidget();
        widgetProvider.onUpdate(this, appWidgetManager, appWidgetIds);
    }

}
