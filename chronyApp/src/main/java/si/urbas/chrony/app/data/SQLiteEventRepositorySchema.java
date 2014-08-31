package si.urbas.chrony.app.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import si.urbas.chrony.util.ReflectiveUpgrader;

public class SQLiteEventRepositorySchema {

  public static final int DATABASE_EVENTS_VERSION = 3;
  public static final String DATABASE_EVENTS = "events";
  public static final String TABLE_EVENTS = "events";
  public static final String TABLE_EVENT_SAMPLES = "eventSamples";
  public static final String EVENTS_COLUMN_EVENT_NAME = "eventName";
  public static final String EVENT_SAMPLES_COLUMN_EVENT_NAME = "eventName";
  public static final String EVENT_SAMPLES_COLUMN_TIMESTAMP = "timestamp";
  private static final String UPGRADE_METHOD_NAME_PREFIX = "upgradeDbToVersion";
  private final ReflectiveUpgrader<SQLiteDatabase> databaseUpgrader = new ReflectiveUpgrader<SQLiteDatabase>(UPGRADE_METHOD_NAME_PREFIX, this);

  public void create(SQLiteDatabase db) {
    databaseUpgrader.upgrade(db, 0, DATABASE_EVENTS_VERSION);
  }

  public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    databaseUpgrader.upgrade(db, oldVersion, newVersion);
  }

  @SuppressWarnings("UnusedDeclaration")
  public void upgradeDbToVersion1(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE events (eventName TEXT, timestamp INTEGER)");
  }

  @SuppressWarnings("UnusedDeclaration")
  public void upgradeDbToVersion2(SQLiteDatabase db) {
    db.execSQL("CREATE INDEX idx_events_eventName ON events (eventName)");
  }

  @SuppressWarnings("UnusedDeclaration")
  public void upgradeDbToVersion3(SQLiteDatabase db) {
    db.execSQL("ALTER TABLE events RENAME TO oldEvents");
    db.execSQL("ALTER TABLE oldEvents ADD COLUMN data REAL DEFAULT NULL");
    db.execSQL("CREATE TABLE events (eventName PRIMARY KEY, dataType INTEGER)");
    db.execSQL("CREATE TABLE eventSamples (" +
               "eventName TEXT REFERENCES events (eventName) ON DELETE CASCADE ON UPDATE CASCADE, " +
               "timestamp INTEGER NOT NULL, " +
               "data REAL DEFAULT NULL)");
    db.execSQL("CREATE INDEX idx_eventSamples_eventName ON eventSamples (eventName)");
    transferEventsFromVersion1(db);
    transferTimestampsFromVersion1(db);
    db.execSQL("DROP INDEX idx_events_eventName");
    db.execSQL("DROP TABLE oldEvents");
  }

  private void transferEventsFromVersion1(SQLiteDatabase db) {
    Cursor cursor = db.rawQuery("SELECT eventName FROM oldEvents GROUP BY eventName", null);
    ContentValues rowValues = new ContentValues();
    while (cursor.moveToNext()) {
      rowValues.put("eventName", cursor.getString(0));
      db.insert("events", null, rowValues);
    }
    cursor.close();
  }

  private void transferTimestampsFromVersion1(SQLiteDatabase db) {
    Cursor cursor = db.rawQuery("SELECT eventName, timestamp FROM oldEvents", null);
    ContentValues rowValues = new ContentValues();
    while (cursor.moveToNext()) {
      rowValues.put("eventName", cursor.getString(0));
      rowValues.put("timestamp", cursor.getLong(1));
      db.insert("eventSamples", null, rowValues);
    }
    cursor.close();
  }
}