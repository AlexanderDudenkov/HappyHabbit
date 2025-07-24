package com.dudencov.happyhabit.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {

    val MIGRATION_3_4 = object : Migration(3, 4) {
        override fun migrate(db: SupportSQLiteDatabase) {
            db.execSQL("ALTER TABLE ReminderTime ADD COLUMN is_reminder_on INTEGER NOT NULL DEFAULT 0")
        }
    }
}