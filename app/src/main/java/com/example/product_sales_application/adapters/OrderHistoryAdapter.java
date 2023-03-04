package com.example.product_sales_application.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sales_application.R;
import com.example.product_sales_application.activities.HistoryDetailActivity;
import com.example.product_sales_application.models.Order;

import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private List<Order> order;
    private Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView customerName;
        public TextView customerPhone;
        public TextView orderDate;
        public TextView orderSaler;
        private View view;

        public ViewHolder (View viewOrderHistory) {
            super(viewOrderHistory);
            view = viewOrderHistory;
            customerPhone = viewOrderHistory.findViewById(R.id.customer_phone);
            customerName = viewOrderHistory.findViewById(R.id.customer_name);
            orderDate = viewOrderHistory.findViewById(R.id.order_date);
            orderSaler = viewOrderHistory.findViewById(R.id.order_saler);
        }
    }


    public OrderHistoryAdapter(List<Order> order) {
        this.order = order;
    }


    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.order_history_layout, parent, false);

        OrderHistoryAdapter.ViewHolder viewHolder = new OrderHistoryAdapter.ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        Order order = this.order.get(position);
        holder.orderSaler.setText(order.getOrderSaler());
        holder.customerName.setText(order.getCustomerName());
        holder.customerPhone.setText(order.getCustomerName());
        holder.orderDate.setText(order.getOrderDate());

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, HistoryDetailActivity.class));
            }
        });
    }

    @Override
    public int getItemCount() {
        return order.size();
    }
}
