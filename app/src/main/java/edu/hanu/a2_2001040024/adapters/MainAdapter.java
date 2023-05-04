package edu.hanu.a2_2001040024.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import edu.hanu.a2_2001040024.R;
import edu.hanu.a2_2001040024.helpers.DatabaseHelper;
import edu.hanu.a2_2001040024.models.Product;
public class MainAdapter extends RecyclerView.Adapter<MainAdapter.ViewHolder> {
	private final List<Product> productList;
	private Toast toast;
	public MainAdapter(List<Product> productList) {
		this.productList = productList;
	}
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		toast = new Toast(context);
		LayoutInflater inflater = LayoutInflater.from(context);
		View productView = inflater.inflate(R.layout.products_rv, parent, false);
		return new ViewHolder(productView);
	}
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		Product product = productList.get(position);
		ImageView image = holder.thumbnail;
		Glide.with(image).load(product.getThumbnail()).placeholder(R.drawable.baseline_image_24).into(image);
		TextView name = holder.name;
		name.setText(product.getName());
		TextView price = holder.price;
		price.setText(formatCurrencyVND(product.getUnitPrice()));
		ImageButton add = holder.add;
		add.setClickable(true);
		add.setOnClickListener(v -> {
			Context context = holder.itemView.getContext();
			DatabaseHelper databaseHelper = new DatabaseHelper(context);
			databaseHelper.addOneToCart(product.getId());
			if (toast != null) {
				toast.cancel();
			}
			toast = Toast.makeText(context, "Added to cart: " + product.getName(), Toast.LENGTH_LONG);
			toast.show();
			databaseHelper.close();
		});
	}
	public String formatCurrencyVND(int amount) {
		Locale vietnam = new Locale("vi", "VN");
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(vietnam);
		return currencyFormatter.format(amount);
	}
	@Override
	public int getItemCount() {
		return productList.size();
	}
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final ImageView thumbnail;
		private final TextView name;
		private final TextView price;
		private final ImageButton add;
		private ViewHolder(View itemView) {
			super(itemView);
			thumbnail = itemView.findViewById(R.id.product_image);
			name = itemView.findViewById(R.id.product_name);
			price = itemView.findViewById(R.id.product_price);
			add = itemView.findViewById(R.id.add_to_cart);
		}
	}
}
