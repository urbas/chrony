package si.urbas.chrony.app.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import si.urbas.chrony.Event;
import si.urbas.chrony.EventRepository;
import si.urbas.chrony.EventSample;
import si.urbas.chrony.util.ChangeListener;
import si.urbas.chrony.util.ConcurrentChangeListenersList;

import java.util.ArrayList;
import java.util.List;

import static si.urbas.chrony.app.data.SQLiteEventRepositorySchema.*;

public class SQLiteEventRepository extends SQLiteOpenHelper implements EventRepository {

  private final ConcurrentChangeListenersList concurrentChangeListenersList = new ConcurrentChangeListenersList();
  private final SQLiteEventRepositorySchema sqliteEventRepositorySchema = new SQLiteEventRepositorySchema();

  public SQLiteEventRepository(Context context) {
    super(context, DATABASE_EVENTS, null, DATABASE_EVENTS_VERSION);
  }

  @Override
  public void onCreate(SQLiteDatabase db) {
    sqliteEventRepositorySchema.create(db);
  }

  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    sqliteEventRepositorySchema.upgrade(db, oldVersion, newVersion);
  }

  @Override
  public void addEvent(Event event) {
    SQLiteDatabase dbWriter = getWritableDatabase();
    try {
      addEvent(event.getEventName(), event.getDataType(), dbWriter);
    } finally {
      dbWriter.close();
    }
    concurrentChangeListenersList.notifyChangeListeners();
  }

  @Override
  public Event getEvent(String eventName) {
    SQLiteDatabase dbReader = getReadableDatabase();
    Event event = getEvent(eventName, dbReader);
    dbReader.close();
    return event;
  }

  @Override
  public void addEventSample(EventSample eventSample) {
    SQLiteDatabase dbWriter = getWritableDatabase();
    try {
      addTimestamp(eventSample.getEventName(), eventSample.getTimestamp(), eventSample.getData(), dbWriter);
    } finally {
      dbWriter.close();
    }
    concurrentChangeListenersList.notifyChangeListeners();
  }

  @Override
  public ArrayList<Event> allEvents() {
    SQLiteDatabase dbReader = getReadableDatabase();
    Cursor cursor = dbReader.rawQuery("SELECT " + EVENTS_COLUMN_EVENT_NAME + ", " + EVENTS_COLUMN_DATA_TYPE + " FROM " + TABLE_EVENTS, null);
    try {
      ArrayList<Event> allEVents = new ArrayList<Event>();
      while (cursor.moveToNext()) {
        allEVents.add(new Event(cursor.getString(0), cursor.getInt(1)));
      }
      return allEVents;
    } finally {
      closeDb(dbReader, cursor);
    }
  }

  @Override
  public List<EventSample> samplesOf(String eventName) {
    SQLiteDatabase dbReader = getReadableDatabase();
    Cursor cursor = dbReader.rawQuery("SELECT " + EVENT_SAMPLES_COLUMN_TIMESTAMP + ", " + EVENT_SAMPLES_COLUMN_DATA + " FROM " + TABLE_EVENT_SAMPLES + " WHERE " + EVENT_SAMPLES_COLUMN_EVENT_NAME + " = ?", new String[]{eventName});
    try {
      ArrayList<EventSample> eventSamples = new ArrayList<EventSample>();
      while (cursor.moveToNext()) {
        int indexOfSampleData = 1;
        Double eventSampleDataAsNumber = getEventSampleDataAsNumber(cursor, indexOfSampleData);
        eventSamples.add(new EventSample(eventName, cursor.getLong(0), eventSampleDataAsNumber));
      }
      return eventSamples;
    } finally {
      closeDb(dbReader, cursor);
    }
  }

  private static Double getEventSampleDataAsNumber(Cursor cursor, int indexOfSampleData) {return cursor.isNull(indexOfSampleData) ? null : cursor.getDouble(indexOfSampleData);}

  @Override
  public void removeEvent(String eventName) {
    SQLiteDatabase dbWriter = getWritableDatabase();
    dbWriter.execSQL("DELETE FROM " + TABLE_EVENTS + " WHERE " + EVENTS_COLUMN_EVENT_NAME + " = ?", new Object[]{eventName});
    dbWriter.close();
    concurrentChangeListenersList.notifyChangeListeners();
  }

  @Override
  public void removeTimestamp(String eventName, Long timestamp) {
    SQLiteDatabase dbWriter = getWritableDatabase();
    dbWriter.execSQL("DELETE FROM " + TABLE_EVENT_SAMPLES + " WHERE " + EVENT_SAMPLES_COLUMN_EVENT_NAME + " = ? AND " + EVENT_SAMPLES_COLUMN_TIMESTAMP + " = ?", new Object[]{eventName, timestamp});
    dbWriter.close();
    concurrentChangeListenersList.notifyChangeListeners();
  }

  @Override
  public void clear() {
    SQLiteDatabase dbWriter = getWritableDatabase();
    dbWriter.execSQL("DELETE FROM " + TABLE_EVENTS);
    dbWriter.close();
    concurrentChangeListenersList.notifyChangeListeners();
  }

  @Override
  public void registerChangeListener(final ChangeListener changeListener) {
    concurrentChangeListenersList.registerChangeListener(changeListener);
  }

  private void addEvent(String eventName, int dataType, SQLiteDatabase dbWriter) {
    ContentValues values = new ContentValues();
    values.put(EVENTS_COLUMN_EVENT_NAME, eventName);
    values.put(EVENTS_COLUMN_DATA_TYPE, dataType);
    dbWriter.insertWithOnConflict(TABLE_EVENTS, null, values, SQLiteDatabase.CONFLICT_IGNORE);
  }

  private static Event getEvent(String eventName, SQLiteDatabase dbReader) {
    Cursor cursor = dbReader.rawQuery("SELECT " + EVENTS_COLUMN_EVENT_NAME + ", " + EVENTS_COLUMN_DATA_TYPE + " FROM " + TABLE_EVENTS + " WHERE " + EVENT_SAMPLES_COLUMN_EVENT_NAME + " = ?", new String[]{eventName});
    Event event = null;
    if (cursor.moveToNext()) {
      event = new Event(cursor.getString(0), cursor.getInt(1));
    }
    cursor.close();
    return event;
  }

  private void addTimestamp(String eventName, long timestamp, Object data, SQLiteDatabase dbWriter) {
    ContentValues values = new ContentValues();
    Event event = getEvent(eventName, dbWriter);
    assertEventExists(event, eventName);
    assertRightDataPresent(event, data);
    values.put(EVENT_SAMPLES_COLUMN_EVENT_NAME, eventName);
    values.put(EVENT_SAMPLES_COLUMN_TIMESTAMP, timestamp);
    dbWriter.insert(TABLE_EVENT_SAMPLES, null, values);
  }

  private static void assertEventExists(Event event, String eventName) {
    if (event == null) {
      throw new IllegalArgumentException("Cannot add a sample for the event '" + eventName + "'. The event does not exist.");
    }
  }

  private static void assertRightDataPresent(Event event, Object data) {
    if (event.getDataType() != Event.NO_DATA_TYPE && data == null) {
      throw new IllegalArgumentException("The event '" + event.getEventName() + "' requires data.");
    }
  }

  private static void closeDb(SQLiteDatabase dbReader, Cursor cursor) {
    cursor.close();
    dbReader.close();
  }

}
