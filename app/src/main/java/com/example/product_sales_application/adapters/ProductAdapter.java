package com.example.product_sales_application.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sales_application.R;
import com.example.product_sales_application.activities.HomeActivity;
import com.example.product_sales_application.activities.ProductDetailActivity;
import com.example.product_sales_application.activities.ProductListActivity;
import com.example.product_sales_application.api.ProductApi;
import com.example.product_sales_application.models.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    public static DecimalFormat formatter = new DecimalFormat("###,###,###");
    private Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView name;
        public TextView price;
        public View view;
        public Button viewMoreButton;

        public ViewHolder(View viewProduct) {
            super(viewProduct);
            view = viewProduct;
            image = viewProduct.findViewById(R.id.image);
            name = viewProduct.findViewById(R.id.name);
            price = viewProduct.findViewById(R.id.price);
            viewMoreButton = viewProduct.findViewById(R.id.view_more_button);
            //description = viewProduct.findViewById(R.id.)
        }
    }

    private List<Product> products;

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == R.layout.view_product) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_product, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_more_button, parent, false);
        }

        if (context == null)
            context = parent.getContext();

        ViewHolder viewHolder = new ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
        if (position == products.size()) {
            holder.viewMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((ProductListActivity)context).limit += 6;
                    ((ProductListActivity)context).GetProductsByType(
                            ((ProductListActivity)context).textQueryStatic,
                            ((ProductListActivity)context).textTypeStatic
                    );
                }
            });
            return;
        }
        Product product = products.get(position);

        ImageView image = holder.image;
        Picasso.get().load(product.getUrl())
                .into(image);
        TextView name = holder.name;
        name.setText(String.format("%s", product.getName()));

        TextView price = holder.price;
        price.setText(formatter.format(product.getPrice())+" VNĐ");

        View view = holder.view;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductDetailActivity.class);
                intent.putExtra("productId", "" + product.getId());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(context instanceof HomeActivity) return products.size();
        else return products.size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        return (position == products.size()) ? R.layout.view_more_button : R.layout.view_product;
    }
}
