package com.example.canopytemperature;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * 这个类的功能是实现SQLite数据库
 * 实现SQLiteOpenHelper必须要实现：
 * onOpen(SQLiteDatabase db)方法
 * onCreate(SQLiteDatabase db)方法
 */
public class UserOpenHelper extends SQLiteOpenHelper {
    /**
     * 构造方法，初始化数据库
     * public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory,int version)
     * 构造方法的参数说明：
     * context：上下文对象
     * name：数据库名称
     * factory：一个可选的游标工厂（通常是 Null）
     * version：当前数据库的版本，值必须是整数并且是递增的状态
     *
     * @param context 上下文对象，content，这么翻译有点别扭，你就直接记住传入的content通常是传入一个this
     */

    public UserOpenHelper(@Nullable Context context) {
        super(context, "canopy.db", null, 1);
    }

    /**
     * 每次打开数据库都会被最先执行
     *
     * @param db SQLite对象
     */
    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        //Returns true if the database is in-memory db.
        if (!db.isReadOnly()) {
            // Enable foreign key constraints 开启外键约束
            db.execSQL("PRAGMA foreign_keys=ON;");
        }
    }

    /**
     * 如果该数据库不存在，在运行程序的时候就会调用onCreate方法进行创建
     *
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        //使用execSQL创建了数据表users
        //数据表中存储了用户的ID，用户的名称，用户的密码
        db.execSQL("create table users (_ID integer primary key autoincrement , _NAME varchar(20) not null ,_PSWD varchar(20) not null);");
    }

    /**
     * 用于更新数据库
     *
     * @param db
     * @param i
     * @param i1
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("drop table if exists users");
        onCreate(db);
    }
}
