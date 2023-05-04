package edu.hanu.a2_2001040024;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.hanu.a2_2001040024.adapters.CartAdapter;
import edu.hanu.a2_2001040024.helpers.DatabaseHelper;
import edu.hanu.a2_2001040024.models.Product;
public class CartActivity extends AppCompatActivity {
	private Toast toast;
	private CartAdapter adapter;
	private List<Product> products;
	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.cart_activity);
		ImageView cartLogo = findViewById(R.id.cartLogo);
		TextView cartText = findViewById(R.id.myCartText);
		if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
			cartLogo.setVisibility(View.GONE);
			cartText.setVisibility(View.GONE);
		} else {
			cartLogo.setVisibility(View.VISIBLE);
			cartText.setVisibility(View.VISIBLE);
		}
		RecyclerView rvCart = findViewById(R.id.rvCart);
		TextView totalPrice = findViewById(R.id.totalPrice);
		DatabaseHelper databaseHelper = new DatabaseHelper(this);
		products = databaseHelper.getCart();
		databaseHelper.close();
		adapter = new CartAdapter(products, totalPrice);
		rvCart.setAdapter(adapter);
		rvCart.setLayoutManager(new LinearLayoutManager(this));
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.go_to_cart, menu);
		return true;
	}
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.to_cart) {
			onBackPressed();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	public void checkout(View view) {
		if (products.isEmpty()) {
			if (toast != null) {
				toast.cancel();
			}
			toast = Toast.makeText(this, "Your cart is empty!", Toast.LENGTH_SHORT);
			toast.show();
		} else {
			DatabaseHelper databaseHelper = new DatabaseHelper(this);
			databaseHelper.clearCart();
			databaseHelper.close();
			adapter.notifyItemRangeRemoved(0, adapter.getItemCount());
			products.clear();
			adapter.updateTotalPrice();
			if (toast != null) {
				toast.cancel();
			}
			toast = Toast.makeText(this, "Checkout completed! Your cart is cleared!", Toast.LENGTH_SHORT);
			toast.show();
		}
	}
}
