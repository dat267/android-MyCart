package edu.hanu.a2_2001040024;
import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import edu.hanu.a2_2001040024.adapters.MainAdapter;
import edu.hanu.a2_2001040024.helpers.CommonExecutor;
import edu.hanu.a2_2001040024.helpers.CommonHandler;
import edu.hanu.a2_2001040024.helpers.DatabaseHelper;
import edu.hanu.a2_2001040024.models.Product;
public class MainActivity extends AppCompatActivity {
	private RecyclerView rvProducts;
	private MainAdapter adapter;
	private List<Product> products;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);
		rvProducts = findViewById(R.id.rvCart);
		CommonExecutor.getInstance().execute(() -> {
			products = apiToProducts();
			CommonHandler.getInstance().post(() -> {
				adapter = new MainAdapter(products);
				rvProducts.setAdapter(adapter);
				rvProducts.setLayoutManager(new GridLayoutManager(this, 2));
				searchViewHandler();
			});
		});
	}
	private List<Product> apiToProducts() {
		try {
			DatabaseHelper databaseHelper = new DatabaseHelper(this);
			URL url = new URL("https://hanu-congnv.github.io/mpr-cart-api/products.json");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("GET");
			connection.connect();
			StringBuilder sb = new StringBuilder();
			InputStream inputStream = connection.getInputStream();
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				sb.append(line);
			}
			connection.disconnect();
			String jsonString = String.valueOf(sb);
			List<Product> products = new ArrayList<>();
			JSONArray jsonArray = new JSONArray(jsonString);
			for (int i = 0; i < jsonArray.length(); i++) {
				JSONObject jsonObject = jsonArray.getJSONObject(i);
				Product product = new Product(jsonObject.getInt("id"), jsonObject.getString("thumbnail"), jsonObject.getString("name"), jsonObject.getString("category"), jsonObject.getInt("unitPrice"));
				databaseHelper.addProduct(product);
				products.add(product);
			}
			databaseHelper.close();
			return products;
		} catch (IOException e) {
			Log.e(TAG, "apiToList: IOException", e);
			return null;
		} catch (JSONException e) {
			Log.e(TAG, "apiToList: JSONException", e);
			return null;
		}
	}
	private void searchViewHandler() {
		SearchView searchView = findViewById(R.id.searchView);
		searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
			@Override
			public boolean onQueryTextSubmit(String query) {
				List<Product> results = search(query);
				RecyclerView recyclerView = findViewById(R.id.rvCart);
				adapter = new MainAdapter(results);
				recyclerView.setAdapter(adapter);
				return false;
			}
			@Override
			public boolean onQueryTextChange(String newText) {
				List<Product> results = search(newText);
				RecyclerView recyclerView = findViewById(R.id.rvCart);
				adapter = new MainAdapter(results);
				recyclerView.setAdapter(adapter);
				return false;
			}
		});
	}
	private List<Product> search(String query) {
		List<Product> results = new ArrayList<>();
		for (Product product : products) {
			String name = product.getName();
			if (name.toLowerCase().contains(query.toLowerCase())) {
				results.add(product);
			}
		}
		return results;
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.go_to_cart, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.to_cart) {
			Intent intent = new Intent(this, CartActivity.class);
//            intent.putExtra("products", (Serializable) products);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}