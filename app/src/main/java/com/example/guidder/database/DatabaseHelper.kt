package com.example.guidder.database

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import com.example.guidder.model.ObjekPariwisata
import org.json.JSONArray
import org.json.JSONObject

class DatabaseHelper(context : Context) : SQLiteOpenHelper(context, "objekpariwisata.db", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        val createObjekPariwisataTableQuery = """
            CREATE TABLE IF NOT EXISTS objekpariwisata (
                id_objek_pariwisata INTEGER PRIMARY KEY AUTOINCREMENT,
                nama TEXT NOT NULL,
                gambar TEXT,
                deskripsi TEXT,
                lokasi TEXT
            );
        """.trimIndent()

        val createUsersTableQuery = """
            CREATE TABLE IF NOT EXISTS users (
                id_user INTEGER PRIMARY KEY AUTOINCREMENT,
                email TEXT UNIQUE NOT NULL,
                nama TEXT NOT NULL,
                password TEXT NOT NULL
            );
        """.trimIndent()

        val createFavoriteTableQuery = """
            CREATE TABLE IF NOT EXISTS favorites (
                id_user INTEGER NOT NULL,
                id_objek_pariwisata INTEGER NOT NULL,
                PRIMARY KEY (id_user, id_objek_pariwisata),
                FOREIGN KEY (id_user) REFERENCES users(id_user) ON DELETE CASCADE,
                FOREIGN KEY (id_objek_pariwisata) REFERENCES objekpariwisata(id_objek_pariwisata) ON DELETE CASCADE
            );
        """.trimIndent()
        db?.execSQL(createObjekPariwisataTableQuery)
        db?.execSQL(createUsersTableQuery)
        db?.execSQL(createFavoriteTableQuery)
    }

    fun registerUser(email: String, nama: String, password: String): Boolean {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("email", email)
            put("nama", nama)
            put("password", password)
        }
        val result = db.insert("users", null, values)
        db.close()
        return result != -1L
    }

    fun checkUser(email: String, password: String): Boolean {
        val db = readableDatabase
        val query = "SELECT * FROM users WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))
        val exists = cursor.count > 0
        cursor.close()
        db.close()
        return exists
    }

    fun getUserDetails(email: String, password: String): Pair<Int, String>? {
        val db = readableDatabase
        val query = "SELECT id_user, nama FROM users WHERE email = ? AND password = ?"
        val cursor = db.rawQuery(query, arrayOf(email, password))

        return if (cursor.moveToFirst()) {
            val userId = cursor.getInt(cursor.getColumnIndexOrThrow("id_user"))
            val userName = cursor.getString(cursor.getColumnIndexOrThrow("nama"))
            cursor.close()
            db.close()
            Pair(userId, userName)
        } else {
            cursor.close()
            db.close()
            null
        }
    }

    fun testingInsertObjekPariwisata() {
        val query = """
            INSERT INTO objekpariwisata (id_objek_pariwisata, nama, gambar, deskripsi, lokasi) 
            VALUES (
                6, 
                'Pulau Komodo', 
                'pulau_komodo.png', 
                'Pulau Komodo adalah rumah bagi spesies kadal terbesar di dunia, yaitu Komodo. Terletak di Nusa Tenggara Timur, Indonesia, pulau ini merupakan bagian dari Taman Nasional Komodo yang terkenal dengan ekosistem uniknya. Selain satwa liar, Pulau Komodo juga menawarkan keindahan alam yang luar biasa, dengan pantai pasir merah muda yang langka (Pink Beach), perairan yang jernih, serta lokasi menyelam kelas dunia yang kaya akan keanekaragaman hayati laut.', 
                '-8.5666°S 119.4902°E'
            );
        """.trimIndent()

        val db = writableDatabase
        db.execSQL(query)
        db.close()

    }

    fun getAllObjekPariwisata() : List<ObjekPariwisata> {
        val objekPariwisataList = arrayListOf<ObjekPariwisata>()
        val db = readableDatabase
        val query = "SELECT * FROM objekpariwisata"
        val cursor = db.rawQuery(query, null)
        cursor.moveToFirst()

        if (cursor.count > 0) {
            do {
                val objekPariwisata = ObjekPariwisata(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id_objek_pariwisata")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nama")),
                    cursor.getString(cursor.getColumnIndexOrThrow("gambar")),
                    cursor.getString(cursor.getColumnIndexOrThrow("deskripsi")),
                    cursor.getString(cursor.getColumnIndexOrThrow("lokasi")),
                )

                objekPariwisataList.add(objekPariwisata)
            } while (cursor.moveToNext())
        }
        cursor.close()
        db.close()
        return objekPariwisataList
    }

    fun getObjekPariwisata(id_objek_pariwisata: Int): ObjekPariwisata? {
        val db = writableDatabase
        val query = "SELECT * FROM objekpariwisata WHERE id_objek_pariwisata = ?"
        val cursor = db.rawQuery(query, arrayOf(id_objek_pariwisata.toString()))

        var objekPariwisata: ObjekPariwisata? = null

        if (cursor.moveToFirst()) {
            objekPariwisata = ObjekPariwisata(
                cursor.getInt(cursor.getColumnIndexOrThrow("id_objek_pariwisata")),
                cursor.getString(cursor.getColumnIndexOrThrow("nama")),
                cursor.getString(cursor.getColumnIndexOrThrow("gambar")),
                cursor.getString(cursor.getColumnIndexOrThrow("deskripsi")),
                cursor.getString(cursor.getColumnIndexOrThrow("lokasi"))
            )
        }

        cursor.close()
        return objekPariwisata
    }

    fun insertObjekPariwisata(objekPariwisata : ObjekPariwisata) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nama", objekPariwisata.nama)
            put("gambar", objekPariwisata.gambar)
            put("deskripsi", objekPariwisata.deskripsi)
            put("lokasi", objekPariwisata.lokasi)
        }
        db.insert("objekpariwisata", null, values)
        db.close()
    }

    fun updateObjekPariwisata(objekPariwisata: ObjekPariwisata) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nama", objekPariwisata.nama)
            put("gambar", objekPariwisata.gambar)
            put("deskripsi", objekPariwisata.deskripsi)
            put("lokasi", objekPariwisata.lokasi)
        }
        db.update("objekpariwisata", values, "id = ?", arrayOf(objekPariwisata.id_objek_pariwisata.toString()))
        db.close()
    }

    fun deleteObjekPariwisata(id_objek_pariwisata : String) {
        val db = writableDatabase
        db.delete("item", "id = ?", arrayOf(id_objek_pariwisata))
        db.close()
    }

    fun insertDataFromJson(jsonArray: JSONArray) {
        val db = writableDatabase
        for (i in 0 until jsonArray.length()) {
            val jsonObj: JSONObject = jsonArray.getJSONObject(i)

            val id = jsonObj.getInt("id_objek_pariwisata")
            val nama = jsonObj.getString("nama")
            val gambar = jsonObj.getString("gambar")
            val deskripsi = jsonObj.getString("deskripsi")
            val lokasi = jsonObj.getString("lokasi")

            val insertQuery = """
                INSERT INTO objekpariwisata (id_objek_pariwisata, nama, gambar, deskripsi, lokasi)
                VALUES (?, ?, ?, ?, ?);
            """.trimIndent()

            val statement = db.compileStatement(insertQuery)
            statement.bindLong(1, id.toLong())
            statement.bindString(2, nama)
            statement.bindString(3, gambar)
            statement.bindString(4, deskripsi)
            statement.bindString(5, lokasi)
            statement.executeInsert()
        }
    }

    fun deleteAllObjekPariwisata() {
        val db = writableDatabase
        db.execSQL("DELETE FROM objekpariwisata")
    }

    fun insertFavorite(id_user: Int, id_objek_pariwisata: Int) {
        val db = writableDatabase
        val query = """
            INSERT INTO favorites (id_user, id_objek_pariwisata) 
            VALUES (?, ?);
        """.trimIndent()

        val statement = db.compileStatement(query)
        statement.bindLong(1, id_user.toLong())
        statement.bindLong(2, id_objek_pariwisata.toLong())
        statement.executeInsert()
    }

    fun deleteFavorite(id_user: Int, id_objek_pariwisata: Int) {
        val db = writableDatabase
        val query = """
            DELETE FROM favorites 
            WHERE id_user = ? AND id_objek_pariwisata = ?;
        """.trimIndent()

        val statement = db.compileStatement(query)
        statement.bindLong(1, id_user.toLong())
        statement.bindLong(2, id_objek_pariwisata.toLong())
        statement.executeUpdateDelete()
    }

    fun checkFavorite(id_user: Int, id_objek_pariwisata: Int): Boolean {
        val db = writableDatabase
        val query = """
            SELECT COUNT(*) FROM favorites 
            WHERE id_user = ? AND id_objek_pariwisata = ?;
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(id_user.toString(), id_objek_pariwisata.toString()))

        var isFavorite = false

        if (cursor.moveToFirst()) {
            isFavorite = cursor.getInt(0) > 0
        }

        cursor.close()
        return isFavorite
    }


    fun getAllFavoriteFromUser(id_user: Int): List<Int> {
        val db = writableDatabase
        val favoriteList = mutableListOf<Int>()
        val query = """
            SELECT id_objek_pariwisata FROM favorites 
            WHERE id_user = ?;
        """.trimIndent()

        val cursor = db.rawQuery(query, arrayOf(id_user.toString()))

        if (cursor.moveToFirst()) {
            do {
                val objekWisataId = cursor.getInt(cursor.getColumnIndexOrThrow("id_objek_pariwisata"))
                favoriteList.add(objekWisataId)
            } while (cursor.moveToNext())
        }

        cursor.close()
        return favoriteList
    }
    fun deleteAllFavorites() {
        val db = writableDatabase
        db.execSQL("DELETE FROM favorites")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS objekpariwisata")
        onCreate(db)
    }

}