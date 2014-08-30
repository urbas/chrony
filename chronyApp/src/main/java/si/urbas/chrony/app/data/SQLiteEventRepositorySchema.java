package si.urbas.chrony.app.data;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import si.urbas.chrony.util.ReflectiveUpgrader;

public class SQLiteEventRepositorySchema {

  private static final String UPGRADE_METHOD_NAME_PREFIX = "upgradeDbToVersion";
  public static final int EVENTS_DB_VERSION = 2;
  public static final String EVENTS_DB_NAME = "events";
  public static final String EVENTS_TABLE_NAME = "events";
  public static final String EVENTS_COLUMN_EVENT_NAME = "eventName";
  public static final String EVENTS_COLUMN_TIMESTAMP = "timestamp";
  private final ReflectiveUpgrader<SQLiteDatabase> databaseUpgrader = new ReflectiveUpgrader<SQLiteDatabase>(UPGRADE_METHOD_NAME_PREFIX, null);

  public SQLiteEventRepositorySchema() { }

  public void create(SQLiteDatabase db) {
    Log.i("DBUPGRADE", "UPGRADING to first version!");
    databaseUpgrader.upgrade(db, 0, EVENTS_DB_VERSION);
  }

  public void upgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    Log.i("DBUPGRADE", "UPGRADING! From: " + oldVersion + " to " + newVersion);
    databaseUpgrader.upgrade(db, oldVersion, newVersion);
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