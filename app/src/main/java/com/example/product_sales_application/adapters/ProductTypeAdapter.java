package com.example.product_sales_application.adapters;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sales_application.R;
import com.example.product_sales_application.activities.ProductListActivity;
import com.example.product_sales_application.models.ProductTypeDomain;
import com.squareup.picasso.Picasso;

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

        View contactView = inflater.inflate(R.layout.view_product_type, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductTypeAdapter.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        ProductTypeDomain productTypeDomain = productTypeList.get(position);

        TextView productTypeNameTextView = holder.productTypeNameTextView;
        productTypeNameTextView.setText(productTypeDomain.getName());

        ImageView ImageView = holder.imageView;
        Picasso.get().load(productTypeDomain.getUrlImange())
                .into(ImageView);
//        Uri uri = Uri.parse(productTypeDomain.getUrlImange());
//        ImageView.setImageURI(uri);

        View view = holder.view;
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ProductListActivity.class);
                intent.putExtra("type", productTypeDomain.getName());
                context.startActivity(intent);

                ((Activity)context).finish();
            }
        });

    }

//    private void confirmDialog(long position) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        builder.setCancelable(true);
//        builder.setTitle("Delete item");
//        builder.setMessage("Do you want to remove item " + position + "?");
//        builder.setPositiveButton("OK",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        productTypeList.remove(position);
//                        notifyDataSetChanged();
//                    }
//                });
//        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//            }
//        });
//
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }

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
