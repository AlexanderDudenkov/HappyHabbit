package com.dudencov.happyhabit.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("""
            CREATE TABLE ReminderTime_new (
                id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                name TEXT NOT NULL,
                reminder_time TEXT NOT NULL DEFAULT '09:00:00',
                is_reminder_on INTEGER NOT NULL DEFAULT 0
            )
        """)

            db.execSQL("""
            INSERT INTO ReminderTime_new (id, name, reminder_time, is_reminder_on)
            SELECT id, name, 
                   COALESCE(reminder_time, '09:00:00'), 
                   0
            FROM ReminderTime
        """)

            db.execSQL("DROP TABLE ReminderTime")

            db.execSQL("ALTER TABLE ReminderTime_new RENAME TO ReminderTime")
        }
    }
}