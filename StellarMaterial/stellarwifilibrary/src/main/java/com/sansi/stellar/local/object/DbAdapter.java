package com.sansi.stellar.local.object;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.sansi.stellar.object.Group;
import com.sansi.stellar.object.Light;
import com.sansi.stellar.object.LightStatus;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DbAdapter {
    private final static String TAG = DbAdapter.class.getSimpleName();
    private SQLiteDatabase db;
    private final Context context;
    public DBOpenHelper dbOpenHelper;
    private static final int DB_VERSION = 2;
    private static final String DB_NAME_LIGHT = "light.db";

    // Light info
    private static final String DB_TABLE_LIGHT = "light";
    private static final String KEY_MAC_LIGHT = "mac";
    private static final String KEY_GROUP_LIGHT  = "group_name";
    private static final String KEY_NAME_LIGHT  = "light_name";

    // Group info
    private static final String DB_TABLE_GROUP = "grp";
    private static final String KEY_NAME_GROUP = "name";

    // keep the last light state values
    private static final String DB_TABLE_LIGHT_STATE = "light_state";
    private static final String STATE_ID = "1";
    private static final String LATEST_STATE_BRIGHTNESS = "state_light";
    private static final String LATEST_STATE_RGBW = "state_rgbw";
    private static final String LATEST_STATE_CCT = "state_cct";
    private static final String LATEST_STATE_SCENE = "state_scene";
    private static final String LATEST_STATE_RATE = "state_rate";

    // keep the light remote state
    private static final String DB_TABLE_REMOTE_STATE = "remote_state";
    private static final String KEY_REMOTE_STATE = "remote";

    // following tables have been used in old version (v1.0)
    // and deprecated from new version (v2.0)
    // <!-- Begin
//	//初始状态信息，也就是最后一次亮度是多少
    private static final String DB_TABLE_STATE="state";
    //	private static final String LAST_STATE_BRIGHTLESS="state_light";
//
//	//初始状态信息，也就是最后一次频率是多少
    private static final String DB_TABLE_STATE_RATE="state_rate";
    //	private static final String LAST_STATE_RATE="last_rate";
//
//	//初始状态信息，也就是最后一次RGB是多少
    private static final String DB_TABLE_STATE_RGB="state_rgb";
    //	private static final String LAST_STATE_RGB="last_rgb";
//
//	//初始状态信息，也就是最后一次频率是多少
    private static final String DB_TABLE_STATE_Y="state_y";
//	private static final String LAST_STATE_Y="last_y";

    // End -->

    // 构造函数
    public DbAdapter(Context _context) {
        this.context = _context;
    }

    /**
     * close the database 关闭数据库
     */
    public void close() {
        if (this.db != null) {
            db.close();
            db = null;
        }
    }

    /**
     * 重新创建表
     */
    public void reCreateTable()	{
        dbOpenHelper.onCreate(this.db);
    }

    public void open() {
        dbOpenHelper = new DBOpenHelper(context, DB_NAME_LIGHT,
                null, DB_VERSION);
        try {
            db = dbOpenHelper.getWritableDatabase();
        } catch (SQLiteException ex) {
            db = dbOpenHelper.getReadableDatabase();
        }
        Log.i(TAG, "DB file: " + db.getPath());
    }

    // help类，用于打开数据库、更新数据库
    private static class DBOpenHelper extends SQLiteOpenHelper {
        private static final String DB_CREATE_LIGHT = "create table "
                + DB_TABLE_LIGHT + " (" + KEY_NAME_LIGHT
                + " varchar(32) primary key, "
                +KEY_MAC_LIGHT+" varchar(32) , "+KEY_GROUP_LIGHT+" varchar(32))";

        private static final String DB_CREATE_GROUP = "create table "
                + DB_TABLE_GROUP + " (" + KEY_NAME_GROUP
                + " varchar(32) primary key) " ;

//		private static final String DB_CREATE_STATE="create table "
//				+ DB_TABLE_STATE +"(" +LAST_STATE_BRIGHTLESS
//				+ " varchar(32) primary key) " ;
//
//		private static final String DB_CREATE_RATE="create table "
//				+ DB_TABLE_STATE_RATE +"(" +LAST_STATE_RATE
//				+ " varchar(32) primary key) " ;
//
//		private static final String DB_CREATE_RGB="create table "
//				+ DB_TABLE_STATE_RGB +"(" +LAST_STATE_RGB
//				+ " varchar(32) primary key) " ;
//
//		private static final String DB_CREATE_Y="create table "
//				+ DB_TABLE_STATE_Y +"(" +LAST_STATE_Y
//				+ " varchar(32) primary key) " ;

        private static final String DB_CREATE_LIGHT_STATE = "create table "
                + DB_TABLE_LIGHT_STATE + "("
                + "id varchar(32) primary key, "
                + LATEST_STATE_BRIGHTNESS + " varchar(32), "
                + LATEST_STATE_RGBW + " varchar(32), "
                + LATEST_STATE_CCT + " varchar(32), "
                + LATEST_STATE_SCENE + " varchar(32), "
                + LATEST_STATE_RATE + " varchar(32))";

        private static final String DB_CREATE_REMOTE_STATE = "create table "
                + DB_TABLE_REMOTE_STATE + "("
                + KEY_MAC_LIGHT + " varchar(32) primary key, "
                + KEY_REMOTE_STATE + " integer)";

        public DBOpenHelper(Context context, String name,
                            CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DB_CREATE_LIGHT);
            db.execSQL(DB_CREATE_GROUP);
            db.execSQL(DB_CREATE_LIGHT_STATE);
            db.execSQL(DB_CREATE_REMOTE_STATE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            int last_v = oldVersion;

            // V1 --> V2
            if (last_v == 1) {
                migrateV1ToV2(db);
                last_v++;
            }

            // V2 --> V3, etc.
        }

        private void migrateV1ToV2(SQLiteDatabase db) {
            // deprecated table in V2
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_STATE);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_STATE_RATE);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_STATE_RGB);
            db.execSQL("DROP TABLE IF EXISTS " + DB_TABLE_STATE_Y);

            // new tables in V2
            db.execSQL(DB_CREATE_LIGHT_STATE);
            db.execSQL(DB_CREATE_REMOTE_STATE);
            // properties changed in V2
            db.execSQL("UPDATE " + DB_TABLE_LIGHT + " SET " + KEY_GROUP_LIGHT + "='' WHERE " + KEY_GROUP_LIGHT + "='Ungrouped' ");
            db.execSQL("DELETE FROM " + DB_TABLE_GROUP + " WHERE " + KEY_NAME_GROUP + "='Ungrouped'");
        }
    }

    // 灯表插入灯
    public long addOneLight(Light light) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAME_LIGHT, light.getName());
        newValues.put(KEY_MAC_LIGHT, light.getMac());
        newValues.put(KEY_GROUP_LIGHT, light.getGroup());
        return db.insert(DB_TABLE_LIGHT, null, newValues);
    }

    /**
     * save the latest light status to DB
     * @param statusObj
     */
    public void updateState(LightStatus statusObj) {
        ContentValues newValues = new ContentValues();
        newValues.put("id", STATE_ID);
        newValues.put(LATEST_STATE_BRIGHTNESS, statusObj.getBrightness());
        newValues.put(LATEST_STATE_RGBW, statusObj.getRGBW());
        newValues.put(LATEST_STATE_CCT, statusObj.getCCT());
        newValues.put(LATEST_STATE_SCENE, statusObj.getScene());
        newValues.put(LATEST_STATE_RATE, statusObj.getRate());
        db.delete(DB_TABLE_LIGHT_STATE, null, null);
        db.insert(DB_TABLE_LIGHT_STATE, null, newValues);
    }

    /**
     *
     * @return LightStatus object which maintain the latest status
     */
    public LightStatus getState() {
        LightStatus statusObj = new LightStatus();
        Cursor cursor = db.query(DB_TABLE_LIGHT_STATE,
                new String[] { "id",
                        LATEST_STATE_BRIGHTNESS,
                        LATEST_STATE_RGBW,
                        LATEST_STATE_CCT,
                        LATEST_STATE_SCENE,
                        LATEST_STATE_RATE },
                null, null, null, null, null);
        if(cursor.getCount()==0)
            return statusObj;

        cursor.moveToFirst();
        statusObj.setBrightness(Integer.parseInt(cursor.getString(1)));
        statusObj.setRGBW(Integer.parseInt(cursor.getString(2)));
        statusObj.setCCT(Integer.parseInt(cursor.getString(3)));
        statusObj.setScene(Integer.parseInt(cursor.getString(4)));
        statusObj.setRate(Integer.parseInt(cursor.getString(5)));
        return statusObj;
    }

//	//数据更新------亮度
//	public void update(int brightless) {
//		ContentValues newValues = new ContentValues();
//		newValues.put(LAST_STATE_BRIGHTLESS, brightless);
//		db.delete(DB_TABLE_STATE, null, null);
//		db.insert(DB_TABLE_STATE, null, newValues);
//	}

//	public int getLightState() {
//		Cursor cursor = db.query(DB_TABLE_STATE, new String[] { LAST_STATE_BRIGHTLESS }, null, null, null, null, null);
//		if(cursor.getCount()==0)
//			return 50;
//		cursor.moveToFirst();
//		return Integer.parseInt(cursor.getString(0));
//	}
//
//	//数据更新------频率
//	public void updateRate(int rate) {
//		ContentValues newValues = new ContentValues();
//		newValues.put(LAST_STATE_RATE, rate);
//		db.delete(DB_TABLE_STATE_RATE, null, null);
//		db.insert(DB_TABLE_STATE_RATE, null, newValues);
//	}
//
//	public int getLightStateRate() {
//		Cursor cursor = db.query(DB_TABLE_STATE_RATE, new String[] { LAST_STATE_RATE }, null, null, null, null, null);
//		if(cursor.getCount()==0) {
//		    return 50;
//		}
//		cursor.moveToFirst();
//		return Integer.parseInt(cursor.getString(0));
//	}
//
//	//数据更新------RGB
//	public void updateRGB(String rgb) {
//		ContentValues newValues = new ContentValues();
//		newValues.put(LAST_STATE_RGB, rgb);
//		db.delete(DB_TABLE_STATE_RGB, null, null);
//		db.insert(DB_TABLE_STATE_RGB, null, newValues);
//	}
//
//	public String getLightStateRGB() {
//		Cursor cursor = db.query(DB_TABLE_STATE_RGB, new String[] { LAST_STATE_RGB }, null, null, null, null, null);
//		if(cursor.getCount()==0) {
//		    return "0,0,0,0";
//		}
//		cursor.moveToFirst();
//		return cursor.getString(0);
//	}
//
//	//数据更新------暖色
//	public void updateY(String y) {
//		ContentValues newValues = new ContentValues();
//		newValues.put(LAST_STATE_Y, y);
//		db.delete(DB_TABLE_STATE_Y, null, null);
//		db.insert(DB_TABLE_STATE_Y, null, newValues);
//	}
//
//	public String getLightStateY() {
//		Cursor cursor = db.query(DB_TABLE_STATE_Y, new String[] { LAST_STATE_Y }, null, null, null, null, null);
//		if(cursor.getCount()==0) {
//		    return "0,0,0,0";
//		}
//		cursor.moveToFirst();
//		return cursor.getString(0);
//	}

    /**
     *
     * @param mac
     * @return
     */
    public long deleteOneLight(String mac) {
        db.execSQL("delete from "+DB_TABLE_LIGHT+" where "+KEY_MAC_LIGHT+"= '"+mac+"'");
        return 0;
    }

    /**
     *
     * @param mac
     * @param light
     * @return
     */
    public long updateLight(String mac, Light light) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAME_LIGHT, light.getName());
        newValues.put(KEY_MAC_LIGHT, light.getMac());
        newValues.put(KEY_GROUP_LIGHT, light.getGroup());
        db.update(DB_TABLE_LIGHT,newValues , KEY_MAC_LIGHT+"=?",new String[]{mac});
        return 1;
    }

    /**
     *
     * @return
     */
    public long deleteAllLight() {
        return db.delete(DB_TABLE_LIGHT, null, null);
    }

    /**
     * delete light table
     */
    public void deleteTableLight() {
        db.execSQL("drop table "+DB_TABLE_LIGHT);
    }

    /**
     * delete all tables
     */
    public void deleteAll() {
        db.execSQL("drop table "+DB_TABLE_LIGHT);
        db.execSQL("drop table "+DB_TABLE_GROUP);
        db.execSQL("drop table "+DB_TABLE_LIGHT_STATE);
        db.execSQL("drop table "+DB_TABLE_REMOTE_STATE);
    }

    /**
     *
     * @return
     */
    public Light[] getAllLight() {
        Cursor cursor = db.query(DB_TABLE_LIGHT, new String[] { KEY_NAME_LIGHT,KEY_MAC_LIGHT,KEY_GROUP_LIGHT }, null, null, null, null, null);
        return convertToLight(cursor);
    }

    /**
     *
     * @param mac
     * @return
     */
    public Light getLight(String mac) {
        Cursor cursor = db.query(DB_TABLE_LIGHT,
                new String[] {KEY_NAME_LIGHT,KEY_MAC_LIGHT,KEY_GROUP_LIGHT},
                KEY_MAC_LIGHT+"=?",
                new String[] { mac }, null, null, null);

        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return null;
        }

        Light light = new Light();
        light.setName(cursor.getString(0));
        light.setMac(cursor.getString(1));
        light.setGroup(cursor.getString(2));
        return light;
    }

    /**
     * convert table data to light object
     * @param cursor
     * @return
     */
    private Light[] convertToLight(Cursor cursor) {
        int resultCounts = cursor.getCount();
        Light[] lights = new Light[resultCounts];
        if (resultCounts > 0 && cursor.moveToFirst()) {
            for (int i = 0; i < resultCounts; i++) {
                lights[i] = new Light();
                lights[i].setMac(cursor.getString(1));
                lights[i].setGroup(cursor.getString(2));
                lights[i].setName(cursor.getString(0));
                cursor.moveToNext();
            }
        }
        return lights;
    }

    /**
     * Convert table data to group object
     * @param cursor
     * @return
     */
    private Group[] convertToGroup(Cursor cursor) {
        int resultCounts = cursor.getCount();
        if(resultCounts == 0 || !cursor.moveToFirst()) {
            return null;
        }

        Group[] groups = new Group[resultCounts];
        for(int i = 0 ; i <resultCounts;i++) {
            groups[i] = new Group();
            groups[i].setName(cursor.getString(0));
            groups[i].setDesc(cursor.getString(0));
            cursor.moveToNext();
        }
        return groups;
    }

    /**
     * Add new group
     * @param g
     * @return
     */
    public long addOneGroup(Group g) {
        ContentValues newValues = new ContentValues();
        newValues.put(KEY_NAME_GROUP, g.getName());
        return db.insert(DB_TABLE_GROUP, null, newValues);
    }

    /**
     *
     * @param name
     * @return
     */
    public long deleteOneGroup(String name) {
        db.execSQL("delete from "+DB_TABLE_GROUP+" where "+KEY_NAME_GROUP+"= '"+name+"'");
        return 1;
    }

    /**
     * Remove all groups from DB
     * @return
     */
    public long deleteAllGroup() {
        return db.delete(DB_TABLE_GROUP, null, null);
    }

    /**
     * Query all groups data from DB
     * @return
     */
    public Group[] getAllGroup() {
        Cursor cursor = db.query(DB_TABLE_GROUP, new String[]{KEY_NAME_GROUP},null,null,null,null,null);
        if(cursor!=null) {
            return  convertToGroup(cursor);
        }
        return null;
    }

    /**
     *
     * @param name
     * @param g
     * @return
     */
    public long updateGroup(String name, Group g) {
        this.deleteOneGroup(name);
        this.addOneGroup(g);
        return 1;
    }

    /**
     *
     * @param mac
     * @return
     */
    public int getRemoteState(String mac) {
        Cursor cursor = db.query(DB_TABLE_REMOTE_STATE,
                new String[] {KEY_REMOTE_STATE},
                KEY_MAC_LIGHT+"=?",
                new String[] { mac }, null, null, null);

        int resultCounts = cursor.getCount();
        if (resultCounts == 0 || !cursor.moveToFirst()) {
            return 0;
        }
        return cursor.getInt(0);
    }

    /**
     *
     * @param mac
     * @param state
     */
    public void setRemoteState(String mac, int state) {
        String sql = "INSERT OR REPLACE INTO "
                + DB_TABLE_REMOTE_STATE
                + " (" +  KEY_MAC_LIGHT + "," + KEY_REMOTE_STATE + ") VALUES('"
                + mac + "'," + state + ");";
        db.execSQL(sql);
    }

    /**
     *
     */
    public Map<String, Integer> getAllRemoteStates() {
        Map<String, Integer> states = new ConcurrentHashMap<String, Integer>();
        Cursor cursor = db.query(DB_TABLE_REMOTE_STATE, new String[]{KEY_MAC_LIGHT, KEY_REMOTE_STATE},null,null,null,null,null);
        int resultCounts = cursor.getCount();
        if(resultCounts > 0 && cursor.moveToFirst()) {
            for(int i = 0 ; i <resultCounts;i++) {
                states.put(cursor.getString(0), cursor.getInt(1));
                cursor.moveToNext();
            }
        }
        return states;
    }
}
