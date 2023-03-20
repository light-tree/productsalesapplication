package com.example.product_sales_application.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sales_application.R;
import com.example.product_sales_application.activities.HistoryDetailActivity;
import com.example.product_sales_application.activities.OrderHistoryActivity;
import com.example.product_sales_application.activities.ProductListActivity;
import com.example.product_sales_application.models.Order;
import com.google.gson.Gson;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.ViewHolder> {
    private List<Order> orderList;
    private Context context;
    private DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy hh:mm");
    private int limit = 6;

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView customerName;
        public TextView customerPhone;
        public TextView orderDate;
        public TextView requireDate;

        public Button viewMoreButton;
        private View view;

        public ViewHolder(View viewOrderHistory) {
            super(viewOrderHistory);
            view = viewOrderHistory;
            customerPhone = viewOrderHistory.findViewById(R.id.customer_phone);
            customerName = viewOrderHistory.findViewById(R.id.customer_name);
            orderDate = viewOrderHistory.findViewById(R.id.order_date);
            requireDate = viewOrderHistory.findViewById(R.id.require_date);
            viewMoreButton = viewOrderHistory.findViewById(R.id.view_more_button);
        }
    }


    public OrderHistoryAdapter(List<Order> order) {
        this.orderList = order;
    }


    @NonNull
    @Override
    public OrderHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;

        if (viewType == R.layout.order_history_layout) {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_history_layout, parent, false);
        } else {
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_more_button, parent, false);
        }

        if (context == null)
            context = parent.getContext();

        OrderHistoryAdapter.ViewHolder viewHolder = new OrderHistoryAdapter.ViewHolder(itemView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderHistoryAdapter.ViewHolder holder, int position) {
        if (position == limit) {
            holder.viewMoreButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    limit += 6;
                    notifyDataSetChanged();
                }
            });
            return;
        }

        Order order = this.orderList.get(position);
        holder.customerName.setText(order.getCustomerFullName());
        holder.customerPhone.setText(order.getCustomerPhone());
        holder.orderDate.setText(dateFormat.format(order.getOrderedDate()));
        holder.requireDate.setText(dateFormat.format(order.getRequiredDate()));

        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, HistoryDetailActivity.class);
                intent.putExtra("order", new Gson().toJson(order));
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(orderList == null)
            return 0;

        if(limit < orderList.size())
            return limit + 1;

        return orderList.size();
    }

    public void setOrderList(List<Order> orderList) {
        this.orderList = orderList;
        limit = 6;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemViewType(int position) {
        if(limit < orderList.size() && position == limit)
            return R.layout.view_more_button;

        return R.layout.order_history_layout;
    }
}
