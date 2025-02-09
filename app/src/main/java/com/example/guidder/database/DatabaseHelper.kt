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

        db?.execSQL(createObjekPariwisataTableQuery)
        db?.execSQL(createUsersTableQuery)
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

    fun updateUsername(id_user: Int, newUsername: String) {
        val db = writableDatabase
        val values = ContentValues().apply {
            put("nama", newUsername)
        }
        db.update("users", values, "id_user = ?", arrayOf(id_user.toString()))
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

    fun deleteAllObjekPariwisata() {
        val db = writableDatabase
        db.execSQL("DELETE FROM objekpariwisata")
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS objekpariwisata")
        onCreate(db)
    }

}