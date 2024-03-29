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
import com.example.product_sales_application.models.OrderDetail;
import com.example.product_sales_application.R;
import com.example.product_sales_application.models.Product;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.ViewHolder> {
    public static DecimalFormat formatter = new DecimalFormat("###,###,###");
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.view_product_order, parent, false);

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

    private List<OrderDetail> orderDetailList;

    public OrderDetailAdapter(List<OrderDetail> orderDetailList){
        this.orderDetailList = orderDetailList;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        Product product = orderDetailList.get(position).getProduct();

        TextView No = holder.Id;
        No.setText(String.format("Mã Sản Phẩm: %s", product.getId()));

        TextView productName = holder.productName;
        productName.setText(String.format("%s", product.getName()));

        TextView price = holder.price;
        price.setText("Đơn giá: " + formatter.format(product.getPrice())+" VNĐ");

        TextView quantity = holder.quantity;
        quantity.setText(String.format("Số lượng: %d ", product.getQuantity()));

        ImageView imageView = holder.imgProduct;
        Picasso.get().load(product.getUrl())
                .into(imageView);

        TextView subTotal = holder.subTotal;
        subTotal.setText("Tổng: " + formatter.format(product.getQuantity() * product.getPrice())+" VNĐ");

    }

    @Override
    public int getItemCount() {
        return orderDetailList != null ? orderDetailList.size() : 0;
    }

    public void setOrderDetailList(List<OrderDetail> list){
        this.orderDetailList = list;
        notifyDataSetChanged();
    }
}
