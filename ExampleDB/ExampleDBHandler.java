package ExampleDB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class ExampleDBHandler {
	private Context context;
	private ExampleDBHelper helper;
	private SQLiteDatabase db;
	
	public ExampleDBHandler(Context context_) {
		this.context = context_;
		helper = new ExampleDBHelper(context);
		db = helper.getWritableDatabase();
	}
	
	public static ExampleDBHandler open(Context context_) throws SQLException {
		ExampleDBHandler handler = new ExampleDBHandler(context_);
		return handler;
	}
	
	public void close() {
		helper.close();
	}
	
	public long insert(ExampleDBRecord record)
	{
		ContentValues values = new ContentValues();
		values.put("level", record.level);
		values.put("beats", record.beats);
		values.put("beat_type", record.beat_type);
		values.put("tempo", record.tempo);
		values.put("fifth", record.fifth);
		values.put("mode", record.mode);
		values.put("clef", record.clef);
		values.put("atonal", record.atonal);
		values.put("time", record.time);
		values.put("readed", 0);
		
		long result = db.insert("melodyexm", null, values);
		return result;
	}
	
	public long update(String time)
	{
		ContentValues values = new ContentValues();
		values.put("readed", 1);
		long result = db.update("melodyexm", values, "time=?", new String[]{String.valueOf(time)});
		return result;
	}
	
	public Cursor selectAll()
	{
		String column[] = {"_id", "level", "tempo", "beats", "beat_type", "fifth", "mode", "clef", "atonal", "time", "readed"};
		String orderBy = "_id desc"; 
		Cursor c = db.query("melodyexm", column, null, null, null, null, orderBy);
		return c;
	}
	
	public int deleteAll()
	{
		return db.delete("melodyexm", null , null);
	}
	
	public int delete(String key)
	{
		return db.delete("melodyexm", "time = ?", new String[]{key});
	}
}
