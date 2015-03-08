package com.teampk.lollol.util;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import com.teampk.lollol.R;
import com.teampk.lollol.dto.ChampionDTO;

public class DataBaseHandler extends SQLiteOpenHelper{
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "lollolDB";
	private static final String TABLE_NAME = "champ_name";
	
	private static final String KEY_ID = "id";
	private static final String KEY_NAME = "name";
	private static final String KEY_IMGSRC = "imgSrc";

    public Context context;

	public DataBaseHandler(Context context) {
		
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		
		String CREATE_LOLLOL_TABLE = "CREATE TABLE " + TABLE_NAME + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
				+ KEY_NAME + " TEXT," + KEY_IMGSRC + " TEXT" + ")";

		db.execSQL(CREATE_LOLLOL_TABLE);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		
	}
	
	public void addChamp(ChampionDTO champion){
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_ID, champion.getId());
		values.put(KEY_NAME, champion.getName());
		values.put(KEY_IMGSRC, champion.getImgSrc());
		
		db.insert(TABLE_NAME, null, values);
	}
	
	public void deleteChamp(int id){
		SQLiteDatabase db = this.getWritableDatabase();
		
		db.delete(TABLE_NAME, KEY_ID + " = ?", new String[]{Integer.toString(id)});
		db.close();
	}

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("Delete FROM " + TABLE_NAME);
    }
	
	public ChampionDTO getChamp(int id){
		SQLiteDatabase db = getReadableDatabase();
        ChampionDTO champ;

		Cursor cursor = db.query(
				TABLE_NAME, 
				new String[]{KEY_ID, KEY_NAME, KEY_IMGSRC}, 
				KEY_ID +" =?", 
				new String[]{Integer.toString(id)}, 
				null, null, null, null);

		if(cursor != null){
            if(cursor.getCount() != 0){
                cursor.moveToFirst();
                champ = new ChampionDTO(cursor.getInt(0), cursor.getString(1), cursor.getString(2));
            } else {
                champ = new ChampionDTO(id, null, null);
            }
        } else {
            champ = new ChampionDTO(id, null, null);
        }

		db.close();
		
		return champ;
	}
	
	public List<ChampionDTO> getChamps(String [] ids){
		List<ChampionDTO> champsResult = new ArrayList<ChampionDTO>();
		SQLiteDatabase db = this.getWritableDatabase();
		
		String query = "SELECT * FROM " + TABLE_NAME + "WHERE" + KEY_ID + " IN(?)";
		Cursor cursor = db.rawQuery(query, ids);
		
		if(cursor.moveToFirst()){
			do{
				ChampionDTO champ = new ChampionDTO();
				champ.setId(cursor.getInt(0));
				champ.setName(cursor.getString(1));
				champ.setImgSrc(cursor.getString(2));
				
				champsResult.add(champ);
			} while (cursor.moveToNext());
		}
		
		db.close();
		
		return champsResult;
	}
	
	public int updateChamp(ChampionDTO champ){
		
		SQLiteDatabase db = this.getWritableDatabase();
		
		ContentValues values = new ContentValues();
		values.put(KEY_ID, champ.getId());
		values.put(KEY_NAME, champ.getName());
		values.put(KEY_IMGSRC, champ.getImgSrc());

		int result = db.update(TABLE_NAME, values, KEY_ID + " =?", new String[]{Integer.toString(champ.getId())});
		
		db.close();
		
		return result;
	}
	
	public int getChampsCount(){
		
		String countQuery = "SELECT * FROM " + TABLE_NAME;
		SQLiteDatabase db = this.getReadableDatabase();
		Cursor cursor = db.rawQuery(countQuery, null);
		int count = cursor.getCount();
        cursor.close();
		return count;
	}

    public void initializeDB(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.beginTransaction();
        try{
            if(getChampsCount() < 123) {
                this.deleteAll();
                this.addChamp(new ChampionDTO(1, "Annie", null));
                this.addChamp(new ChampionDTO(2, "Olaf", null));
                this.addChamp(new ChampionDTO(3, "Galio", null));
                this.addChamp(new ChampionDTO(4, "TwistedFate", null));
                this.addChamp(new ChampionDTO(5, "XinZhao", null));
                this.addChamp(new ChampionDTO(6, "Urgot", null));
                this.addChamp(new ChampionDTO(7, "Leblanc", null));
                this.addChamp(new ChampionDTO(8, "Vladimir", null));
                this.addChamp(new ChampionDTO(9, "FiddleSticks", null));
                this.addChamp(new ChampionDTO(10, "Kayle", null));
                this.addChamp(new ChampionDTO(11, "MasterYi", null));
                this.addChamp(new ChampionDTO(12, "Alistar", null));
                this.addChamp(new ChampionDTO(13, "Ryze", null));
                this.addChamp(new ChampionDTO(14, "Sion", null));
                this.addChamp(new ChampionDTO(15, "Sivir", null));
                this.addChamp(new ChampionDTO(16, "Soraka", null));
                this.addChamp(new ChampionDTO(17, "Teemo", null));
                this.addChamp(new ChampionDTO(18, "Tristana", null));
                this.addChamp(new ChampionDTO(19, "Warwick", null));
                this.addChamp(new ChampionDTO(20, "Nunu", null));
                this.addChamp(new ChampionDTO(21, "MissFortune", null));
                this.addChamp(new ChampionDTO(22, "Ashe", null));
                this.addChamp(new ChampionDTO(23, "Tryndamere", null));
                this.addChamp(new ChampionDTO(24, "Jax", null));
                this.addChamp(new ChampionDTO(25, "Morgana", null));
                this.addChamp(new ChampionDTO(26, "Zilean", null));
                this.addChamp(new ChampionDTO(27, "Singed", null));
                this.addChamp(new ChampionDTO(28, "Evelynn", null));
                this.addChamp(new ChampionDTO(29, "Twitch", null));
                this.addChamp(new ChampionDTO(30, "Karthus", null));
                this.addChamp(new ChampionDTO(31, "Chogath", null));
                this.addChamp(new ChampionDTO(32, "Amumu", null));
                this.addChamp(new ChampionDTO(33, "Rammus", null));
                this.addChamp(new ChampionDTO(34, "Anivia", null));
                this.addChamp(new ChampionDTO(35, "Shaco", null));
                this.addChamp(new ChampionDTO(36, "DrMundo", null));
                this.addChamp(new ChampionDTO(37, "Sona", null));
                this.addChamp(new ChampionDTO(38, "Kassadin", null));
                this.addChamp(new ChampionDTO(39, "Irelia", null));
                this.addChamp(new ChampionDTO(40, "Janna", null));
                this.addChamp(new ChampionDTO(41, "Gangplank", null));
                this.addChamp(new ChampionDTO(42, "Corki", null));
                this.addChamp(new ChampionDTO(43, "Karma", null));
                this.addChamp(new ChampionDTO(44, "Taric", null));
                this.addChamp(new ChampionDTO(45, "Veigar", null));
                this.addChamp(new ChampionDTO(48, "Trundle", null));
                this.addChamp(new ChampionDTO(50, "Swain", null));
                this.addChamp(new ChampionDTO(51, "Caitlyn", null));
                this.addChamp(new ChampionDTO(53, "Blitzcrank", null));
                this.addChamp(new ChampionDTO(54, "Malphite", null));
                this.addChamp(new ChampionDTO(55, "Katarina", null));
                this.addChamp(new ChampionDTO(56, "Nocturne", null));
                this.addChamp(new ChampionDTO(57, "Maokai", null));
                this.addChamp(new ChampionDTO(58, "Renekton", null));
                this.addChamp(new ChampionDTO(59, "JarvanIV", null));
                this.addChamp(new ChampionDTO(60, "Elise", null));
                this.addChamp(new ChampionDTO(61, "Orianna", null));
                this.addChamp(new ChampionDTO(62, "MonkeyKing", null));
                this.addChamp(new ChampionDTO(63, "Brand", null));
                this.addChamp(new ChampionDTO(64, "LeeSin", null));
                this.addChamp(new ChampionDTO(67, "Vayne", null));
                this.addChamp(new ChampionDTO(68, "Rumble", null));
                this.addChamp(new ChampionDTO(69, "Cassiopeia", null));
                this.addChamp(new ChampionDTO(72, "Skarner", null));
                this.addChamp(new ChampionDTO(74, "Heimerdinger", null));
                this.addChamp(new ChampionDTO(75, "Nasus", null));
                this.addChamp(new ChampionDTO(76, "Nidalee", null));
                this.addChamp(new ChampionDTO(77, "Udyr", null));
                this.addChamp(new ChampionDTO(78, "Poppy", null));
                this.addChamp(new ChampionDTO(79, "Gragas", null));
                this.addChamp(new ChampionDTO(80, "Pantheon", null));
                this.addChamp(new ChampionDTO(81, "Ezreal", null));
                this.addChamp(new ChampionDTO(82, "Mordekaiser", null));
                this.addChamp(new ChampionDTO(83, "Yorick", null));
                this.addChamp(new ChampionDTO(84, "Akali", null));
                this.addChamp(new ChampionDTO(85, "Kennen", null));
                this.addChamp(new ChampionDTO(86, "Garen", null));
                this.addChamp(new ChampionDTO(89, "Leona", null));
                this.addChamp(new ChampionDTO(90, "Malzahar", null));
                this.addChamp(new ChampionDTO(91, "Talon", null));
                this.addChamp(new ChampionDTO(92, "Riven", null));
                this.addChamp(new ChampionDTO(96, "KogMaw", null));
                this.addChamp(new ChampionDTO(98, "Shen", null));
                this.addChamp(new ChampionDTO(99, "Lux", null));
                this.addChamp(new ChampionDTO(101, "Xerath", null));
                this.addChamp(new ChampionDTO(102, "Shyvana", null));
                this.addChamp(new ChampionDTO(103, "Ahri", null));
                this.addChamp(new ChampionDTO(104, "Graves", null));
                this.addChamp(new ChampionDTO(105, "Fizz", null));
                this.addChamp(new ChampionDTO(106, "Volibear", null));
                this.addChamp(new ChampionDTO(107, "Rengar", null));
                this.addChamp(new ChampionDTO(110, "Varus", null));
                this.addChamp(new ChampionDTO(111, "Nautilus", null));
                this.addChamp(new ChampionDTO(112, "Viktor", null));
                this.addChamp(new ChampionDTO(113, "Sejuani", null));
                this.addChamp(new ChampionDTO(114, "Fiora", null));
                this.addChamp(new ChampionDTO(115, "Ziggs", null));
                this.addChamp(new ChampionDTO(117, "Lulu", null));
                this.addChamp(new ChampionDTO(119, "Draven", null));
                this.addChamp(new ChampionDTO(120, "Hecarim", null));
                this.addChamp(new ChampionDTO(121, "Khazix", null));
                this.addChamp(new ChampionDTO(122, "Darius", null));
                this.addChamp(new ChampionDTO(126, "Jayce", null));
                this.addChamp(new ChampionDTO(127, "Lissandra", null));
                this.addChamp(new ChampionDTO(131, "Diana", null));
                this.addChamp(new ChampionDTO(133, "Quinn", null));
                this.addChamp(new ChampionDTO(134, "Syndra", null));
                this.addChamp(new ChampionDTO(143, "Zyra", null));
                this.addChamp(new ChampionDTO(150, "Gnar", null));
                this.addChamp(new ChampionDTO(154, "Zac", null));
                this.addChamp(new ChampionDTO(157, "Yasuo", null));
                this.addChamp(new ChampionDTO(161, "Velkoz", null));
                this.addChamp(new ChampionDTO(201, "Braum", null));
                this.addChamp(new ChampionDTO(222, "Jinx", null));
                this.addChamp(new ChampionDTO(236, "Lucian", null));
                this.addChamp(new ChampionDTO(238, "Zed", null));
                this.addChamp(new ChampionDTO(254, "Vi", null));
                this.addChamp(new ChampionDTO(266, "Aatrox", null));
                this.addChamp(new ChampionDTO(267, "Nami", null));
                this.addChamp(new ChampionDTO(268, "Azir", null));
                this.addChamp(new ChampionDTO(412, "Thresh", null));
                this.addChamp(new ChampionDTO(421, "RekSai	", null));
                this.addChamp(new ChampionDTO(429, "Kalista", null));
            }
            db.setTransactionSuccessful();
        } catch (Exception e){
            Resources resources = context.getResources();
            String failed = resources.getString(R.string.init_db_failed);
            Toast.makeText(context, failed, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

}
