package com.example.product_sales_application.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sales_application.R;
import com.example.product_sales_application.models.ProductTypeDomain;

import java.util.List;

public class ProductTypeAdapter extends RecyclerView.Adapter<ProductTypeAdapter.ViewHolder>{
    private List<ProductTypeDomain> productTypeList;
    private Context context;

    public ProductTypeAdapter(List<ProductTypeDomain> productTypeList) {
        this.productTypeList = productTypeList;
    }

    @NonNull
    @Override
    public ProductTypeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(context == null){
            context = parent.getContext();
        }
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.product_type_layout, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTypeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductTypeDomain productTypeDomain = productTypeList.get(position);

        TextView productTypeNameTextView = holder.productTypeNameTextView;
        productTypeNameTextView.setText(productTypeDomain.getName());

        ImageView ImageView = holder.imageView;
//        Uri uri = Uri.parse(productTypeDomain.getUrlImange());
//        ImageView.setImageURI(uri);

        View view = holder.view;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog(productTypeDomain.getId());
            }
        });

    }

    private void confirmDialog(long position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setTitle("Delete item");
        builder.setMessage("Do you want to remove item " + position + "?");
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        productTypeList.remove(position);
                        notifyDataSetChanged();
                    }
                });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public int getItemCount() {
        return productTypeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView productTypeNameTextView;
        ImageView imageView;

        public ViewHolder(View viewProductType) {
            super(viewProductType);
            view = viewProductType;
            productTypeNameTextView = (TextView) viewProductType.findViewById(R.id.product_type_text);
            imageView = (ImageView) viewProductType.findViewById(R.id.product_type_image);
        }
    }
}
