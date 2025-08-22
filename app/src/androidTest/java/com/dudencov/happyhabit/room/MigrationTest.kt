package com.dudencov.happyhabit.room

import androidx.room.testing.MigrationTestHelper
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.dudencov.happyhabit.data.db.AppDatabase
import com.dudencov.happyhabit.data.db.DatabaseMigrations.MIGRATION_3_4
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    @get:Rule
    val helper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        AppDatabase::class.java,
    )

    @Test
    fun migrate1To2() {
        var db = helper.createDatabase("test_db", 1)

        db.execSQL("INSERT INTO Habit (id, name) VALUES (1, 'Test Habit')")
        db.execSQL("INSERT INTO SelectedDate (id, habitId, date) VALUES (1, 1, '2025-08-21')")
        db.close()

        db = helper.runMigrationsAndValidate(
            "test_db",
            2,
            true
        )

        val cursor = db.query("SELECT id, name FROM Habit")
        assertTrue(cursor.moveToFirst())
        assertEquals("Test Habit", cursor.getString(cursor.getColumnIndexOrThrow("name")))
        cursor.close()

        val cursorDate = db.query("SELECT id, habitId, date FROM SelectedDate")
        assertTrue(cursorDate.moveToFirst())
        assertEquals(1, cursorDate.getInt(cursorDate.getColumnIndexOrThrow("habitId")))
        assertEquals("2025-08-21", cursorDate.getString(cursorDate.getColumnIndexOrThrow("date")))
        cursorDate.close()

        db.close()
    }

    @Test
    fun migrate2To3() {
        var db = helper.createDatabase("test_db2", 2)

        db.execSQL("INSERT INTO Habit (id, name) VALUES (1, 'Test Habit')")
        db.execSQL("INSERT INTO SelectedDate (id, habitId, date) VALUES (1, 1, '2025-08-21')")
        db.close()

        db = helper.runMigrationsAndValidate(
            "test_db2",
            3,
            true
        )

        val cursorHabit = db.query("SELECT id, name FROM Habit")
        assertTrue(cursorHabit.moveToFirst())
        assertEquals("Test Habit", cursorHabit.getString(cursorHabit.getColumnIndexOrThrow("name")))
        cursorHabit.close()

        val cursorDate = db.query("SELECT id, habitId, date FROM SelectedDate")
        assertTrue(cursorDate.moveToFirst())
        assertEquals(1, cursorDate.getInt(cursorDate.getColumnIndexOrThrow("habitId")))
        assertEquals("2025-08-21", cursorDate.getString(cursorDate.getColumnIndexOrThrow("date")))
        cursorDate.close()

        db.execSQL("INSERT INTO ReminderTime (id, name, reminder_time) VALUES (1, 'Morning Reminder', '08:00')")
        val cursorReminder = db.query("SELECT id, name, reminder_time FROM ReminderTime")
        assertTrue(cursorReminder.moveToFirst())
        assertEquals(
            "Morning Reminder",
            cursorReminder.getString(cursorReminder.getColumnIndexOrThrow("name"))
        )
        assertEquals(
            "08:00",
            cursorReminder.getString(cursorReminder.getColumnIndexOrThrow("reminder_time"))
        )
        cursorReminder.close()

        db.close()
    }

    @Test
    fun migrate3To4() {
        var db = helper.createDatabase("test_db3", 3)

        db.execSQL("INSERT INTO ReminderTime (id, name, reminder_time) VALUES (1, 'Test Habit', NULL)")
        db.close()

        db = helper.runMigrationsAndValidate(
            "test_db3",
            4,
            true,
            MIGRATION_3_4
        )

        val cursor = db.query("SELECT id, name, reminder_time, is_reminder_on FROM ReminderTime")
        assertTrue(cursor.moveToFirst())
        assertEquals("09:00:00", cursor.getString(cursor.getColumnIndexOrThrow("reminder_time")))
        assertEquals(1, cursor.getInt(cursor.getColumnIndexOrThrow("is_reminder_on")))
        cursor.close()
        db.close()
    }

    @Test
    fun migrate1To4() {
        var db = helper.createDatabase("test_db4", 1)

        db.execSQL("INSERT INTO Habit (id, name) VALUES (1, 'From v1')")
        db.execSQL("INSERT INTO SelectedDate (id, habitId, date) VALUES (1, 1, '2025-01-01')")

        db.close()

        db = helper.runMigrationsAndValidate("test_db4", 4, true)

        val cursor = db.query("SELECT id, name FROM Habit WHERE id = 1")
        assertTrue(cursor.moveToFirst())
        assertEquals("From v1", cursor.getString(cursor.getColumnIndexOrThrow("name")))
        cursor.close()

        val reminderCursor = db.query("SELECT reminder_time, is_reminder_on FROM ReminderTime")
        if (reminderCursor.moveToFirst()) {
            assertEquals(
                "09:00:00",
                reminderCursor.getString(reminderCursor.getColumnIndexOrThrow("reminder_time"))
            )
            assertEquals(
                1,
                reminderCursor.getInt(reminderCursor.getColumnIndexOrThrow("is_reminder_on"))
            )
        }
        reminderCursor.close()

        db.close()
    }

}

