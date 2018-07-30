package net.jahanco.jahandaru.Helpers;

import android.content.ContentValues;
import android.content.Context;

import net.jahanco.jahandaru.Models.ItemEntity;
import net.jahanco.jahandaru.Models.Sick;
import net.jahanco.jahandaru.Models.SpecialSick;
import net.sqlcipher.Cursor;
import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteOpenHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mr R00t on 4/22/2017.
 */
//id 	name 	en_name
// scientific_name 	family 	nature
// specifications 	ingredients 	properties
// 	contraindications 	organs 	habitat 	construction
public class DbHelper
        extends SQLiteOpenHelper {
    private String plantsTable = "planets";
    private String sicknessTable = "sickness";
    private String specialSicknessTable = "special";
    private final String SQL_CREATE_PLANT = "CREATE TABLE IF NOT EXISTS " + plantsTable + "( " +
            "id INTEGER PRIMARY KEY UNIQUE ," +
            "name VARCHAR(256)," +
            "en_name VARCHAR(256)," +
            "scientific_name VARCHAR(256)," +
            "family VARCHAR(256)," +
            "nature TEXT," +
            "specifications TEXT," +
            "ingredients TEXT," +
            "properties TEXT," +
            "contraindications TEXT," +
            "organs TEXT," +
            "habitat TEXT," +
            "construction TEXT," +
            "backimage VARCHAR(256)," +
            "image VARCHAR(256)," +
            "price VARCHAR(10)," +
            "tags TEXT ," +
            "favorite INTEGER," +
            "type INTEGER)";
    private final String SQL_CREATE_SICKNESS = "CREATE TABLE IF NOT EXISTS " + sicknessTable + "( " +
            "id INTEGER PRIMARY KEY UNIQUE ," +
            "name VARCHAR(256)," +
            "tags TEXT);";
    private final String SQL_CREATE_SPECIAL_SICKNESS = "CREATE TABLE IF NOT EXISTS " + specialSicknessTable + "( " +
            "id INTEGER PRIMARY KEY UNIQUE ," +
            "name VARCHAR(256)," +
            "body TEXT);";

    private SQLiteDatabase db = getWritableDatabase(EHelper.get_pass());


    public DbHelper(Context paramContext) {
        super(paramContext, "jd.db", null, 1);
    }

    private ItemEntity parseItemEntity(Cursor paramCursor) {
        ArrayList<ItemEntity> ItemEntitys = parseItemEntitys(paramCursor, true);
        if (ItemEntitys.isEmpty()) {
            return null;
        }
        return ItemEntitys.get(0);
    }

    //id 	name 	en_name
// scientific_name 	family 	nature
// specifications 	ingredients 	properties
// 	contraindications 	organs 	habitat 	construction
    private ArrayList<ItemEntity> parseItemEntitys(Cursor paramCursor, boolean single) {
        ArrayList<ItemEntity> itemEntities = new ArrayList<>();
        if (paramCursor != null) {
            while (paramCursor.moveToNext()) {
                ItemEntity itemEntity = new ItemEntity();
                itemEntity.setId(paramCursor.getInt(0));
                itemEntity.setName(paramCursor.getString(1));
                itemEntity.setEn_name(paramCursor.getString(2));
                itemEntity.setScientific_name(paramCursor.getString(3));
                itemEntity.setFamily(paramCursor.getString(4));
                itemEntity.setNature(paramCursor.getString(5));
                itemEntity.setSpecifications(paramCursor.getString(6));
                itemEntity.setIngredients(paramCursor.getString(7));
                itemEntity.setProperties(paramCursor.getString(8));
                itemEntity.setContraindications(paramCursor.getString(9));
                itemEntity.setOrgans(paramCursor.getString(10));
                itemEntity.setHabitat(paramCursor.getString(11));
                itemEntity.setConstruction(paramCursor.getString(12));
                itemEntity.setBackImage(paramCursor.getString(13));
                itemEntity.setImage(paramCursor.getString(14));
                itemEntity.setPrice(paramCursor.getString(15));
                itemEntity.setTags(paramCursor.getString(16));
                if (paramCursor.getInt(17) == 0) {
                    itemEntity.setFavorite(false);
                } else {
                    itemEntity.setFavorite(true);

                }
                itemEntity.setType((paramCursor.getInt(18)));

                itemEntities.add(itemEntity);
                if (single) {
                    break;
                }
            }
            paramCursor.close();
        }
        return itemEntities;
    }


    private ContentValues parseToContent(ItemEntity paramItemEntity) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", paramItemEntity.getName());
        contentValues.put("en_name", paramItemEntity.getEn_name());
        contentValues.put("scientific_name", paramItemEntity.getScientific_name());
        contentValues.put("family", paramItemEntity.getFamily());
        contentValues.put("nature", paramItemEntity.getNature());
        contentValues.put("specifications", paramItemEntity.getSpecifications());
        contentValues.put("ingredients", paramItemEntity.getIngredients());
        contentValues.put("properties", paramItemEntity.getProperties());
        contentValues.put("contraindications", paramItemEntity.getContraindications());
        contentValues.put("organs", paramItemEntity.getOrgans());
        contentValues.put("habitat", paramItemEntity.getHabitat());
        contentValues.put("construction", paramItemEntity.getConstruction());
        contentValues.put("backimage", paramItemEntity.getBackImage());
        contentValues.put("image", paramItemEntity.getImage());
        contentValues.put("price", paramItemEntity.getPrice());
        contentValues.put("tags", paramItemEntity.getTags());
        if (paramItemEntity.isFavorite())
            contentValues.put("favorite", 1);
        else {
            contentValues.put("favorite", 0);
        }
        contentValues.put("type", paramItemEntity.getType());

        return contentValues;
    }

    public boolean batch_insert_Item(ArrayList<ItemEntity> paramArrayList) {
        db.beginTransaction();
        try {
            for (ItemEntity item : paramArrayList) {
                this.insert(item);
            }
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    public Cursor customQuery(String table, String select, String where) {
        String sql = "SELECT " + select + " FROM " + table;
        if (where != null) {
            sql += " WHERE " + where;
        }
        return this.db.rawQuery(sql, null);
    }

    public int delete(String table, HashMap<String, String> where) {
        String keys = "";
        boolean flag = false;
        ArrayList<String> values = new ArrayList<>();
        String[] vals;
        if (where == null) {
            vals = null;
            keys = null;
        } else {

            for (Map.Entry<String, String> entry : where.entrySet()) {
                if (flag) {
                    keys += " and ";
                }
                keys += entry.getKey() + "=?";
                values.add(entry.getValue());
                flag = true;
            }
            vals = new String[values.size()];
            for (int i = 0; i < values.size(); i++) {
                vals[i] = values.get(i);
            }
        }
        return db.delete(table, keys, vals);
    }

    protected void finalize()
            throws Throwable {
        this.db.close();
        super.finalize();
    }

    public Cursor getQuery(String table, HashMap<String, String> where) { //get data from database in cursor
        String query = "SELECT * FROM " + table;
        String keys = "";
        boolean flag = false;
        ArrayList<String> values = new ArrayList<>();
        String[] vals;
        if (where == null) {
            vals = null;
        } else {
            query += " WHERE ";
            for (Map.Entry<String, String> entry : where.entrySet()) {
                if (flag) {
                    keys += " and ";
                }
                keys += entry.getKey() + "=?";
                values.add(entry.getValue());
                flag = true;
            }
            query += keys;
            vals = new String[values.size()];
            for (int i = 0; i < values.size(); i++) {
                vals[i] = values.get(i);
            }
        }

        return db.rawQuery(query, vals);
    }

    public ArrayList<ItemEntity> getItems() {
        Cursor cursor = getQuery(plantsTable, null);
        ArrayList<ItemEntity> ItemEntitys = parseItemEntitys(cursor, false);
        cursor.close();
        if (ItemEntitys.isEmpty()) {
            return null;
        } else {
            return ItemEntitys;
        }

    }

    public ItemEntity getItem(int id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id + "");
        Cursor cursor = getQuery(plantsTable, map);
        return parseItemEntity(cursor);

    }

    public Long insert(ItemEntity ItemEntity) {
        int res = this.update(ItemEntity);
        if (res == 0) {
            return db.insert(plantsTable, null, parseToContent(ItemEntity));
        } else {
            return (long) res;
        }
    }

    public void onCreate(SQLiteDatabase paramSQLiteDatabase) {
        paramSQLiteDatabase.execSQL(SQL_CREATE_PLANT);
        paramSQLiteDatabase.execSQL(SQL_CREATE_SICKNESS);
        paramSQLiteDatabase.execSQL(SQL_CREATE_SPECIAL_SICKNESS);
    }

    public void onUpgrade(SQLiteDatabase paramSQLiteDatabase, int paramInt1, int paramInt2) {
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + plantsTable);
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + sicknessTable);
        paramSQLiteDatabase.execSQL("DROP TABLE IF EXISTS " + specialSicknessTable);
        onCreate(paramSQLiteDatabase);
    }

    public int update(ItemEntity ItemEntity) {
        ContentValues contentValues = parseToContent(ItemEntity);
        String keys = "id = ?";
        String[] vals = {ItemEntity.getId() + ""};
        return db.update(plantsTable, contentValues, keys, vals);
    }

    public ArrayList<ItemEntity> jsonArrayToItem(String json) throws JSONException {
        ArrayList<ItemEntity> itemEntities = new ArrayList<>();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonItem = array.getJSONObject(i);
            ItemEntity item = new ItemEntity();
            item.setId(jsonItem.getInt("id"));
            item.setName(jsonItem.getString("name"));
            item.setEn_name(jsonItem.getString("en_name"));
            item.setScientific_name(jsonItem.getString("scientific_name"));
            item.setFamily(jsonItem.getString("family"));
            item.setNature(jsonItem.getString("nature"));
            item.setSpecifications(jsonItem.getString("specifications"));
            item.setIngredients(jsonItem.getString("ingredients"));
            item.setProperties(jsonItem.getString("properties"));
            item.setContraindications(jsonItem.getString("contraindications"));
            item.setOrgans(jsonItem.getString("organs"));
            item.setHabitat(jsonItem.getString("habitat"));
            item.setConstruction(jsonItem.getString("construction"));
//            item.setBackImage(jsonItem.getString("back_image"));
//            item.setImage(jsonItem.getString(""));
//            item.setPrice(jsonItem.getString(""));
//            item.setTags(jsonItem.getString(""));
//            if (jsonItem.getInt("") == 0) {
//                item.setFavorite(false);
//            } else {
//                item.setFavorite(true);
//
//            }
//            item.setType((jsonItem.getInt("")));
            itemEntities.add(item);
        }
        return itemEntities;
    }

    public boolean batch_update_Item(ArrayList<ItemEntity> paramArrayList) {
        db.beginTransaction();
        try {
            for (ItemEntity item : paramArrayList) {
                this.update(item);
            }
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }

    public ArrayList<ItemEntity> getItemsWithSickness(int sick_id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", sick_id + "");
        Cursor cursor = getQuery(sicknessTable, map);
        Sick sicknessItem = parseSickEntity(cursor, true).get(0);
        String[] tags=sicknessItem.getTags().split(":");
        ArrayList<ItemEntity> itemEntities=new ArrayList<>();

        for (String tag:tags) {
            ItemEntity itemEntity=getItem(Integer.parseInt(tag));
            itemEntities.add(itemEntity);
        }
        return itemEntities;
    }



    private ArrayList<Sick> parseSickEntity(Cursor paramCursor, boolean single) {
        ArrayList<Sick> sicks = new ArrayList<>();
        if (paramCursor != null) {
            while (paramCursor.moveToNext()) {
                Sick sick = new Sick();
                sick.setId(paramCursor.getInt(0));
                sick.setTitle(paramCursor.getString(1));
                sick.setTags(paramCursor.getString(2));

                sicks.add(sick);
                if (single) {
                    break;
                }
            }
            paramCursor.close();
        }
        return sicks;
    }
    private ArrayList<SpecialSick> parseSpecialSickEntity(Cursor paramCursor, boolean single) {
        ArrayList<SpecialSick> sicks = new ArrayList<>();
        if (paramCursor != null) {
            while (paramCursor.moveToNext()) {
                SpecialSick sick = new SpecialSick();
                sick.setId(paramCursor.getInt(0));
                sick.setName(paramCursor.getString(1));
                sick.setBody(paramCursor.getString(2));

                sicks.add(sick);
                if (single) {
                    break;
                }
            }
            paramCursor.close();
        }
        return sicks;
    }
    public Long insert(Sick sick) {
        return insert(sick,sicknessTable);
    }
    public Long insert(Sick sick,String table) {
        int res = this.update(sick);
        if (res == 0) {
            return db.insert(table, null, parseToContent(sick));
        } else {
            return (long) res;
        }
    }
    public Long insert(SpecialSick sick) {
        int res = this.update(sick);
        if (res == 0) {
            return db.insert(specialSicknessTable, null, parseToContent(sick));
        } else {
            return (long) res;
        }
    }
    public int update(Sick sick) {
        return update(sick,sicknessTable);
    }
    public int update(Sick sick,String table) {
        ContentValues contentValues = parseToContent(sick);
        String keys = "id = ?";
        String[] vals = {sick.getId() + ""};
        return db.update(table, contentValues, keys, vals);
    }
    public int update(SpecialSick sick) {
        ContentValues contentValues = parseToContent(sick);
        String keys = "id = ?";
        String[] vals = {sick.getId() + ""};
        return db.update(specialSicknessTable, contentValues, keys, vals);
    }
    private ContentValues parseToContent(Sick sick) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", sick.getTitle());
        contentValues.put("tags", sick.getTags());
        return contentValues;
    }
    private ContentValues parseToContent(SpecialSick sick) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", sick.getName());
        contentValues.put("body", sick.getBody());
        return contentValues;
    }
    public ArrayList<Sick> jsonArrayToSicknessItem(String json) throws JSONException {
        ArrayList<Sick> sicks = new ArrayList<>();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonItem = array.getJSONObject(i);
            Sick item = new Sick();
            item.setId(jsonItem.getInt("id"));
            item.setTitle(jsonItem.getString("name"));
            item.setTags(jsonItem.getString("tags"));
            sicks.add(item);
        }
        return sicks;
    }
    public ArrayList<SpecialSick> jsonArrayToSpecialSicknessItem(String json) throws JSONException {
        ArrayList<SpecialSick> sicks = new ArrayList<>();
        JSONArray array = new JSONArray(json);
        for (int i = 0; i < array.length(); i++) {
            JSONObject jsonItem = array.getJSONObject(i);
            SpecialSick item = new SpecialSick();
            item.setId(jsonItem.getInt("id"));
            item.setName(jsonItem.getString("name"));
            item.setBody(jsonItem.getString("body"));
            sicks.add(item);
        }
        return sicks;
    }
    public boolean batch_insert_sickness_item(ArrayList<Sick> sicks) {
        db.beginTransaction();
        try {
            for (Sick item : sicks) {
                this.insert(item);
            }
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }
    public boolean batch_insert_special_sickness_item(ArrayList<SpecialSick> sicks) {
        db.beginTransaction();
        try {
            for (SpecialSick item : sicks) {
                this.insert(item);
            }
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }
    }


    public ArrayList<Sick> getSicknessItems() {
        Cursor cursor = getQuery(sicknessTable, null);
        ArrayList<Sick> sicks = parseSickEntity(cursor, false);
        cursor.close();
        if (sicks.isEmpty()) {
            return null;
        } else {
            return sicks;
        }
    }

    public SpecialSick getSpecialItem(int id) {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", id + "");

        Cursor cursor = getQuery(specialSicknessTable, map);
        ArrayList<SpecialSick> sicks = parseSpecialSickEntity(cursor, false);
        cursor.close();
        if (sicks.isEmpty()) {
            return null;
        } else {
            return sicks.get(0);
        }
    }

    public ArrayList<SpecialSick> getSpecialItems() {
        Cursor cursor = getQuery(specialSicknessTable, null);
        ArrayList<SpecialSick> sicks = parseSpecialSickEntity(cursor, false);
        cursor.close();
        if (sicks.isEmpty()) {
            return null;
        } else {
            return sicks;
        }
    }

    public boolean batch_update_special_sickness_items(ArrayList<SpecialSick> specialSicks) {
        db.beginTransaction();
        try {
            for (SpecialSick item : specialSicks) {
                this.update(item);
            }
            db.setTransactionSuccessful();
            return true;
        } finally {
            db.endTransaction();
        }

    }
}
