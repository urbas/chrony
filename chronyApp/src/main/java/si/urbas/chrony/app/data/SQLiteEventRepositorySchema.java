package si.urbas.chrony.app.data;

import android.database.sqlite.SQLiteDatabase;
import si.urbas.chrony.util.ReflectiveUpgrader;

public class SQLiteEventRepositorySchema {

  public static final int EVENTS_DB_VERSION = 2;
  public static final String EVENTS_DB_NAME = "events";
  public static final String EVENTS_TABLE_NAME = "events";
  public static final String EVENTS_COLUMN_EVENT_NAME = "eventName";
  public static final String EVENTS_COLUMN_TIMESTAMP = "timestamp";
  private static final String UPGRADE_METHOD_NAME_PREFIX = "upgradeDbToVersion";
  private final ReflectiveUpgrader<SQLiteDatabase> databaseUpgrader = new ReflectiveUpgrader<SQLiteDatabase>(UPGRADE_METHOD_NAME_PREFIX, this);

  public void create(SQLiteDatabase db) {
    databaseUpgrader.upgrade(db, 0, EVENTS_DB_VERSION);
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
}