package com.example.product_sales_application.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sales_application.R;
import com.example.product_sales_application.models.Product;

import java.io.File;
import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {

    private Context context;
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView image;
        public TextView name;
        public TextView price;

        public ViewHolder (View viewProduct) {
            super(viewProduct);
            image = viewProduct.findViewById(R.id.image);
            name = viewProduct.findViewById(R.id.name);
            price = viewProduct.findViewById(R.id.price);
        }
    }

    private List<Product> products;

    public ProductAdapter(List<Product> products) {
        this.products = products;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(context == null) context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.view_product, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = products.get(position);

        ImageView image = holder.image;
        image.setImageResource(R.drawable.sample_product);
//        File file = new  File("sample_product.jpg");
//        if(file.exists()){
//            image.setImageURI(Uri.fromFile(file));
////            image.setImageURI(Uri.parse("android.resource://com.segf4ult.test/drawable/sample_product.jpg"));
//        }
//        else {
//            image.setImageResource(R.drawable.ic_launcher_foreground);
//        }

        TextView name = holder.name;
        name.setText(String.format("%s", product.getName()));

        TextView price = holder.price;
        price.setText(String.format("Giá: %.2f", product.getPrice()));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }
}
