package com.example.todo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.Objects;

public class ToDoActivity extends AppCompatActivity {

    ToDoDbAdapter DbAdapter;
    ToDoSimpleCursorAdapter ToDoAdapter;
    Context context;
    ListView listView;
    AlertDialog.Builder builder;
    private int chosenToDo;


    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = ToDoActivity.this;
        DbAdapter = new ToDoDbAdapter(context);
        DbAdapter.open();

        Cursor cursor = DbAdapter.fetchAllToDos();
        ToDoAdapter = new ToDoSimpleCursorAdapter(
                context,
                R.layout.todo_row,
                cursor,
                new String[] {"content"},
                new int[] {R.id.row_text},
                0);

        listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(ToDoAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                chosenToDo = (int)id;
                //TODO Show (Alert Dialog with items) here
          }
        });

        //TODO Build (Alert Dialog with items) here and make an onclicklistner on the dialog items
        //TODO check which item pressed: if the first item (Edit ToDo) show your (Custom Edit Dialog), otherwise, put the next code in it.
        /*
            TODO Put the next lines if the choice was (Delete ToDo)

            DbAdapter.deleteToDoById(chosenToDo);
            Cursor newCursor = DbAdapter.fetchAllToDos();
            ToDoAdapter.changeCursor(newCursor);
         */
        //Remember: if none of the items is chosen, don't do anything
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        if (id == R.id.exit) {
            DbAdapter.close();
            finish();
        }
        else if(id == R.id.new_todo) {
            //TODO Put here custom dialog for (new ToDo)
        }

        return super.onOptionsItemSelected(item);
    }
}
