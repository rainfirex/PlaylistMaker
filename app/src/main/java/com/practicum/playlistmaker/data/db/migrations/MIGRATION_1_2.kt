package com.practicum.playlistmaker.data.db.migrations

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object MIGRATION_1_2 : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL(
            "ALTER TABLE track ADD COLUMN created INTEGER DEFAULT (strftime('%s', 'now') * 1000)"
        )
    }
}