package si.urbas.chrony.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.util.ChangeListener;
import si.urbas.chrony.util.ConcurrentChangeListenersList;
import si.urbas.chrony.util.ReflectiveUpgrader;

import java.util.ArrayList;
import java.util.List;

public class SQLiteEventRepository extends SQLiteOpenHelper implements EventRepository {

  private static final int EVENTS_DB_VERSION = 2;
  private static final String EVENTS_DB_NAME = "events";
  private static final String EVENTS_TABLE_NAME = "events";
  private static final String EVENTS_COLUMN_EVENT_NAME = "eventName";
  private static final String EVENTS_COLUMN_TIMESTAMP = "timestamp";
  private static final String UPGRADE_METHOD_NAME_PREFIX = "upgradeDbToVersion";

  private final ConcurrentChangeListenersList concurrentChangeListenersList = new ConcurrentChangeListenersList();
  private final ReflectiveUpgrader<SQLiteDatabase> databaseUpgrader = new ReflectiveUpgrader<SQLiteDatabase>(UPGRADE_METHOD_NAME_PREFIX, this);

  public SQLiteEventRepository(Context context) {
    super(context, EVENTS_DB_NAME, null, EVENTS_DB_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    Log.i("DBUPGRADE", "UPGRADING to first version!");
    databaseUpgrader.upgrade(db, 0, EVENTS_DB_VERSION);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.i("DBUPGRADE", "UPGRADING! From: " + oldVersion + " to " + newVersion);
    databaseUpgrader.upgrade(db, oldVersion, newVersion);
  }

  @Override
  public void addEvent(Event event) {
    ContentValues values = new ContentValues();
    values.put(EVENTS_COLUMN_EVENT_NAME, event.name);
    values.put(EVENTS_COLUMN_TIMESTAMP, event.timestamp);
    SQLiteDatabase dbWriter = getWritableDatabase();
    try {
      dbWriter.insert(EVENTS_DB_NAME, null, values);
    } finally {
      dbWriter.close();
    }
    concurrentChangeListenersList.notifyChangeListeners();
  }

  @Override
  public ArrayList<String> allEvents() {
    SQLiteDatabase dbReader = getReadableDatabase();
    Cursor cursor = dbReader.rawQuery("SELECT " + EVENTS_COLUMN_EVENT_NAME + " FROM " + EVENTS_TABLE_NAME + " GROUP BY " + EVENTS_COLUMN_EVENT_NAME, null);
    try {
      ArrayList<String> allEVents = new ArrayList<String>();
      while (cursor.moveToNext()) {
        allEVents.add(cursor.getString(0));
      }
      return allEVents;
    } finally {
      closeDb(dbReader, cursor);
    }
  }

  @Override
  public List<Long> timestampsOf(String eventName) {
    SQLiteDatabase dbReader = getReadableDatabase();
    Cursor cursor = dbReader.rawQuery("SELECT " + EVENTS_COLUMN_TIMESTAMP + " FROM " + EVENTS_TABLE_NAME + " WHERE " + EVENTS_COLUMN_EVENT_NAME + " = ?", new String[]{eventName});
    try {
      ArrayList<Long> eventTimestamps = new ArrayList<Long>();
      while (cursor.moveToNext()) {
        eventTimestamps.add(cursor.getLong(0));
      }
      return eventTimestamps;
    } finally {
      closeDb(dbReader, cursor);
    }
  }

  @Override
  public void removeTimestamp(String eventName, Long timestamp) {
    SQLiteDatabase dbWriter = getWritableDatabase();
    dbWriter.execSQL("DELETE FROM " + EVENTS_TABLE_NAME + " WHERE " + EVENTS_COLUMN_EVENT_NAME + " = ? AND " + EVENTS_COLUMN_TIMESTAMP + " = ?", new Object[]{eventName, timestamp});
    dbWriter.close();
    concurrentChangeListenersList.notifyChangeListeners();
  }

  @Override
  public void clear() {
    SQLiteDatabase dbWriter = getWritableDatabase();
    dbWriter.execSQL("DELETE FROM " + EVENTS_TABLE_NAME);
    dbWriter.close();
    concurrentChangeListenersList.notifyChangeListeners();
  }

  @Override
  public void registerChangeListener(final ChangeListener changeListener) {
    concurrentChangeListenersList.registerChangeListener(changeListener);
  }

  private static void closeDb(SQLiteDatabase dbReader, Cursor cursor) {
    cursor.close();
    dbReader.close();
  }

  @SuppressWarnings("UnusedDeclaration")
  public void upgradeDbToVersion1(SQLiteDatabase db) {
    db.execSQL("CREATE TABLE " + EVENTS_TABLE_NAME + " (" + EVENTS_COLUMN_EVENT_NAME + " TEXT, " + EVENTS_COLUMN_TIMESTAMP + " INTEGER)");
  }

  @SuppressWarnings("UnusedDeclaration")
  public void upgradeDbToVersion2(SQLiteDatabase db) {
    db.execSQL("CREATE INDEX idx_events_eventName ON " + EVENTS_TABLE_NAME + " (" + EVENTS_COLUMN_EVENT_NAME + " )");
  }

}
