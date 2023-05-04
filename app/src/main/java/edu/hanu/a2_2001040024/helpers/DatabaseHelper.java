package edu.hanu.a2_2001040024.helpers;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import edu.hanu.a2_2001040024.models.Product;
public class DatabaseHelper extends SQLiteOpenHelper {
	private static final String DATABASE_NAME = "MyCart.db";
	private static final int DATABASE_VERSION = 2;
	private static final String CREATE_PRODUCTS_TABLE = "CREATE TABLE products ( " + "id INT PRIMARY KEY, " + "thumbnail VARCHAR(255), " + "category VARCHAR(255), " + "name VARCHAR(255), " + "unitPrice INT, " + "numberInCart INT )";
	private static final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS products";
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}
	public void addProduct(Product product) {
		SQLiteDatabase db = getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("id", product.getId());
		values.put("thumbnail", product.getThumbnail());
		values.put("name", product.getName());
		values.put("category", product.getCategory());
		values.put("unitPrice", product.getUnitPrice());
		values.put("numberInCart", 0);
		Cursor cursor = db.rawQuery("SELECT * FROM products WHERE id=?", new String[]{String.valueOf(product.getId())});
		if (cursor.getCount() == 0) {
			db.insert("products", null, values);
		}
		cursor.close();
		db.close();
	}
	public void clearCart() {
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL("UPDATE products SET numberInCart = 0;");
		db.close();
	}
	public void addOneToCart(int productId) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "UPDATE products SET numberInCart = numberInCart + 1 WHERE id = " + productId;
		db.execSQL(sql);
		db.close();
	}
	public void removeOneFromCart(int productId) {
		SQLiteDatabase db = getWritableDatabase();
		String sql = "UPDATE products SET numberInCart = numberInCart - 1 WHERE id = " + productId;
		db.execSQL(sql);
		db.close();
	}
	public List<Product> getCart() {
		SQLiteDatabase db = getReadableDatabase();
		List<Product> cart = new ArrayList<>();
		Cursor cursor = db.rawQuery("SELECT * FROM products WHERE numberInCart > 0", null);
		if (cursor.moveToFirst()) {
			do {
				int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
				String thumbnail = cursor.getString(cursor.getColumnIndexOrThrow("thumbnail"));
				String category = cursor.getString(cursor.getColumnIndexOrThrow("category"));
				String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
				int unitPrice = cursor.getInt(cursor.getColumnIndexOrThrow("unitPrice"));
				int numberInCart = cursor.getInt(cursor.getColumnIndexOrThrow("numberInCart"));
				Product product = new Product(id, thumbnail, name, category, unitPrice);
				product.setNumberInCart(numberInCart);
				cart.add(product);
			} while (cursor.moveToNext());
		}
		cursor.close();
		db.close();
		return cart;
	}
	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_PRODUCTS_TABLE);
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(SQL_DELETE_ENTRIES);
		onCreate(db);
	}
	public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onUpgrade(db, oldVersion, newVersion);
	}
}

