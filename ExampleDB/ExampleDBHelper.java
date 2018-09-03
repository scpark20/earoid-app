package ExampleDB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ExampleDBHelper extends SQLiteOpenHelper {

	public ExampleDBHelper(Context context) {
		super(context, "melodyexm.db", null, 2);
		// TODO Auto-generated constructor stub
		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		db.execSQL("create table melodyexm (_id INTEGER PRIMARY KEY AUTOINCREMENT, level INTEGER, beats INTEGER, beat_type INTEGER, tempo INTEGER, fifth INTEGER, mode INTEGER, clef INTEGER, atonal INTEGER, time TEXT, readed INTEGER);");	
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		db.execSQL("drop table if exists rhythmexm");
		onCreate(db);
	}

}
