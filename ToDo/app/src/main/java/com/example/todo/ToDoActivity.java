package com.example.todo;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class ToDoActivity extends AppCompatActivity {

    private ToDoDbAdapter DbAdapter;
    private Context context;
    ListView listView;

    protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        context = getApplicationContext();
        DbAdapter = new ToDoDbAdapter(context);

        DbAdapter.open();

        listView = (ListView)findViewById(R.id.list_view);
//        listView.setAdapter(new ToDoSimpleCursorAdapter(context, R.layout.todo_row, DbAdapter.fetchAllToDos(), null, null, 0));
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
            setContentView(R.layout.todo_row);
        }

        return super.onOptionsItemSelected(item);
    }
}
