package com.example.todo;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

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
                openTodoOptions(chosenToDo);
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
            openDialog("New",new ToDo());
        }

        return super.onOptionsItemSelected(item);
    }
    public void openDialog(String TodoType, final ToDo todo){
        final Dialog dialog = new Dialog(context);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_custom);

        // initialize the custom dialog components:
        TextView Title = (TextView) dialog.findViewById(R.id.Dtitle);
        final EditText TODO_NAME = (EditText) dialog.findViewById(R.id.DeditText);
        final CheckBox IMPORTANT = (CheckBox) dialog.findViewById(R.id.DcheckBox);
        final Button SaveButton = (Button) dialog.findViewById(R.id.DsaveButton);
        Button CancelButton = (Button) dialog.findViewById(R.id.DcancelButton);

        //setting the custom dialog components:
        CancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        
        TODO_NAME.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(TODO_NAME.getText().toString().isEmpty()) {SaveButton.setEnabled(false);}
                else {SaveButton.setEnabled(true);}
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        //new todo
        if(TodoType.equals("New")){
            Title.setText("New ToDo");
            SaveButton.setEnabled(false);
            SaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String TodoName = TODO_NAME.getText().toString();
                    DbAdapter.createToDo(TODO_NAME.getText().toString(),IMPORTANT.isChecked());
                    Cursor newCursor = DbAdapter.fetchAllToDos();
                    ToDoAdapter.changeCursor(newCursor);
                    dialog.dismiss();
                }
            });
        }
        //edit todo
        else {
            Title.setText("Edit ToDo");
            TODO_NAME.setText(todo.getContent());
            TODO_NAME.setSelection(TODO_NAME.getText().length());
            IMPORTANT.setChecked(todo.isImportant());
            SaveButton.setEnabled(true);
            SaveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    todo.setContent(TODO_NAME.getText().toString());
                    int Important = IMPORTANT.isChecked()? 1:0;
                    todo.setImportant(Important);
                    DbAdapter.updateToDo(todo);
                    Cursor newCursor = DbAdapter.fetchAllToDos();
                    ToDoAdapter.changeCursor(newCursor);
                    dialog.dismiss();
                }
            });
        }
        //show dialog:
        dialog.show();
    }
    public void openTodoOptions(final int CHOSEN){
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        // add a list
        String[] options = {"Edit ToDo", "Delete ToDo"};
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: // Edit ToDo
                        ToDo ChosenTodo = DbAdapter.fetchToDoById(CHOSEN);
                        openDialog("Edit",ChosenTodo);
                        break;
                    case 1: // Delete Todo
                        DbAdapter.deleteToDoById(CHOSEN);
                        Cursor newCursor = DbAdapter.fetchAllToDos();
                        ToDoAdapter.changeCursor(newCursor);
                        break;
                }
            }
        });
        // create and show the alert dialog
        AlertDialog OptionsDialog = builder.create();
        OptionsDialog.show();
    }
}
