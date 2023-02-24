package com.example.product_sales_application.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sales_application.models.Order;
import com.example.product_sales_application.models.Product;
import com.example.product_sales_application.R;

public class OrderDetailAdapter  extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.view_order_detail, parent, false);

        ViewHolder viewHolder  = new OrderDetailAdapter.ViewHolder(contactView);
        return viewHolder;




    }



    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView productName;
        public TextView Id;
        public TextView quantity;
        public TextView price;
        public TextView subTotal;
        public ImageView imgProduct;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Id = (TextView) itemView.findViewById(R.id.tv_Id);
            productName = (TextView) itemView.findViewById(R.id.tv_product_name);
            price = (TextView) itemView.findViewById(R.id.tv_product_price);
            quantity = (TextView) itemView.findViewById(R.id.tv_product_quantity);
            subTotal = (TextView) itemView.findViewById(R.id.tv_product_total_money);

            imgProduct = (ImageView) itemView.findViewById(R.id.imgv_product);

        }
    }

    private Order order;

    public OrderDetailAdapter(Order order){
        this.order = order;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = order.getCart().getProducts().get(position);
        int index =  order.getCart().getProducts().indexOf(product);

        TextView No = holder.Id;
        No.setText(String.format("Mã Sản Phẩm: %s", product.getId()));


        TextView productName = holder.productName;
        productName.setText(String.format("%s", product.getName()));

        TextView price = holder.price;
        price.setText(String.format("Giá: %.2f", product.getPrice()));

        TextView quantity = holder.quantity;
        quantity.setText(String.format(" %d ", product.getQuantity()));

        ImageView imageView = holder.imgProduct;
        imageView.setImageResource(product.getImageResource());

        TextView subTotal = holder.subTotal;
        subTotal.setText(String.format( "Thành tiền: " + "%.2f VND", product.getQuantity() * product.getPrice()) );




    }

    @Override
    public int getItemCount() {
        return order.getCart().getProducts().size();
    }


}
