package com.example.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;



public class ToDoDbAdapter {

    //these are the column names
    public static final String COL_ID = "_id";
    public static final String COL_CONTENT = "content";
    public static final String COL_IMPORTANT = "important";
    //these are the corresponding indices
    public static final int INDEX_ID = 0;
    public static final int INDEX_CONTENT = INDEX_ID + 1;
    public static final int INDEX_IMPORTANT = INDEX_ID + 2;
    //used for logging
    private static final String TAG = "ToDoDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private static final String DATABASE_NAME = "dba_todos";
    private static final String TABLE_NAME = "tbl_todos";
    private static final int DATABASE_VERSION = 1;
    private final Context mCtx;
    //SQL statement used to create the database
    private static final String DATABASE_CREATE =
            "CREATE TABLE if not exists " + TABLE_NAME + " ( " +
                    COL_ID + " INTEGER PRIMARY KEY autoincrement, " +
                    COL_CONTENT + " TEXT, " +
                    COL_IMPORTANT + " INTEGER );";


    public ToDoDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }
    //open
    public void open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
    }
    //close
    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }


    //TODO implement the function createToDo() which take the name as the content of the ToDo and boolean important...note that the id will be created for you automatically
    //DONE-->REHAM
    public void createToDo(String name, boolean important) {
        int imp = important? 1:0;
        mDb.execSQL("INSERT INTO "+ TABLE_NAME+" ( " +COL_CONTENT+" , " +COL_IMPORTANT+ " ) VALUES ( '"+ name+"' , "+ imp+" );" );
    }
    //TODO overloaded to take a todo
    public long createToDo(ToDo todo) {
        mDb.execSQL("INSERT INTO "+ TABLE_NAME+" ( " +COL_CONTENT+" , " +COL_IMPORTANT+ " ) VALUES " +
                "( '"+ todo.getContent()+"' , "+ todo.getImportant()+" );" );
        return (1);
    }

    //TODO implement the function fetchToDoById() to get a certain todo given its id
    //DONE-->REHAM
    public ToDo fetchToDoById(int id) {
        ToDo todo = new ToDo();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + TABLE_NAME+ " WHERE "+COL_ID+" =?",new String[]{id+" "});
        cursor.moveToFirst();
        todo.setContent(cursor.getString(cursor.getColumnIndex(COL_CONTENT)));
        todo.setImportant(cursor.getInt(cursor.getColumnIndex(COL_IMPORTANT)));
        todo.setId(cursor.getInt(cursor.getColumnIndex(COL_ID)));
       return todo;
    }


    //TODO implement the function fetchAllToDos() which get all todos
    //DONE
    public Cursor fetchAllToDos() {
        return mDb.rawQuery("SELECT * FROM " + TABLE_NAME,null);
    }

    //TODO implement the function updateToDo() to update a certain todo
    //DONE-->REHAM
    public void updateToDo(ToDo todo) {
        mDb.execSQL("UPDATE "+ TABLE_NAME+" SET "+ COL_CONTENT + " = '"+ todo.getContent()+"' , "
                + COL_IMPORTANT+ " = "+ todo.getImportant()+ " WHERE "+ COL_ID+" = "+todo.getId()+" ;");
    }
    //TODO implement the function deleteToDoById() to delete a certain todo given its id
    //DONE
    public void deleteToDoById(int nId) {
        mDb.execSQL("DELETE FROM " + TABLE_NAME + " where "+ COL_ID +" = " + nId);
    }

    //TODO implement the function deleteAllToDos() to delete all todos
    //DONE
    public void deleteAllToDos() {
        mDb.execSQL("DELETE FROM " + TABLE_NAME);
    }


    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.w(TAG, DATABASE_CREATE);
            db.execSQL(DATABASE_CREATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
            onCreate(db);
        }
    }


}
