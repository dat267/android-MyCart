package edu.hanu.a2_2001040024.adapters;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import edu.hanu.a2_2001040024.R;
import edu.hanu.a2_2001040024.helpers.DatabaseHelper;
import edu.hanu.a2_2001040024.models.Product;
public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
	private final List<Product> products;
	private final TextView totalPrice;
	public CartAdapter(List<Product> products, TextView totalPrice) {
		this.products = products;
		totalPrice.setText(formatCurrencyVND(0));
		this.totalPrice = totalPrice;
	}
	public String formatCurrencyVND(int amount) {
		Locale vietnam = new Locale("vi", "VN");
		NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(vietnam);
		return currencyFormatter.format(amount);
	}
	@NonNull
	@Override
	public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
		Context context = parent.getContext();
		LayoutInflater inflater = LayoutInflater.from(context);
		View cartView = inflater.inflate(R.layout.cart_rv, parent, false);
		return new ViewHolder(cartView);
	}
	@Override
	public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
		updateTotalPrice();
		Product product = products.get(position);
		ImageView thumbnail = holder.thumbnail;
		Glide.with(thumbnail).load(product.getThumbnail()).placeholder(R.drawable.baseline_image_24).into(thumbnail);
		TextView name = holder.name;
		name.setText(product.getName());
		TextView unitPrice = holder.unitPrice;
		unitPrice.setText(formatCurrencyVND(product.getUnitPrice()));
		TextView sumPrice = holder.sumPrice;
		sumPrice.setText(formatCurrencyVND(product.getUnitPrice() * product.getNumberInCart()));
		TextView quantity = holder.quantity;
		quantity.setText(String.valueOf(product.getNumberInCart()));
		ImageButton increase = holder.increase;
		ImageButton decrease = holder.decrease;
		increase.setOnClickListener(v -> {
			DatabaseHelper databaseHelper = new DatabaseHelper(holder.itemView.getContext());
			product.setNumberInCart(product.getNumberInCart() + 1);
			databaseHelper.addOneToCart(product.getId());
			notifyItemChanged(position);
			updateTotalPrice();
			databaseHelper.close();
		});
		decrease.setOnClickListener(v -> {
			DatabaseHelper databaseHelper = new DatabaseHelper(holder.itemView.getContext());
			product.setNumberInCart(product.getNumberInCart() - 1);
			databaseHelper.removeOneFromCart(product.getId());
			notifyItemChanged(position);
			if (product.getNumberInCart() == 0) {
				products.remove(position);
				notifyItemRemoved(position);
				notifyItemRangeChanged(position, products.size());
				updateTotalPrice();
			}
			databaseHelper.close();
		});
	}
	public void updateTotalPrice() {
		int total = 0;
		for (Product p : products) {
			total = total + (p.getUnitPrice() * p.getNumberInCart());
		}
		totalPrice.setText(formatCurrencyVND(total));
	}
	@Override
	public int getItemCount() {
		return products.size();
	}
	public static class ViewHolder extends RecyclerView.ViewHolder {
		private final ImageView thumbnail;
		private final TextView name;
		private final TextView unitPrice;
		private final TextView sumPrice;
		private final ImageButton increase;
		private final ImageButton decrease;
		private final TextView quantity;
		private ViewHolder(@NonNull View itemView) {
			super(itemView);
			thumbnail = itemView.findViewById(R.id.thumbnail);
			name = itemView.findViewById(R.id.name);
			unitPrice = itemView.findViewById(R.id.unitPrice);
			sumPrice = itemView.findViewById(R.id.sumPrice);
			increase = itemView.findViewById(R.id.increase);
			decrease = itemView.findViewById(R.id.decrease);
			quantity = itemView.findViewById(R.id.quantity);
		}
	}
}
