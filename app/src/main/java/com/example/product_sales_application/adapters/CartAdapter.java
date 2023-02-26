package com.example.product_sales_application.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sales_application.models.Cart;
import com.example.product_sales_application.models.Product;
import com.example.product_sales_application.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

public static class ViewHolder extends RecyclerView.ViewHolder {

    public TextView productName;
    public TextView Id;
    public TextView quantity;
    public TextView price;
    public TextView total;
    public Button deleteButton;
    public  ImageView imgProduct;
    public Button increaseButton;
    public  Button decreaseButton;



    public ViewHolder (View viewProduct) {
        super(viewProduct);
        Id = (TextView) viewProduct.findViewById(R.id.tv_Id);
        productName = (TextView) viewProduct.findViewById(R.id.tv_product_name);
        price = (TextView) viewProduct.findViewById(R.id.tv_product_price);
        quantity = (TextView) viewProduct.findViewById(R.id.tv_product_quantity);
        total = (TextView) viewProduct.findViewById(R.id.tv_product_total_money);
        imgProduct = (ImageView) viewProduct.findViewById(R.id.imgv_product);
        deleteButton = (Button) viewProduct.findViewById(R.id.btn_Cart_Remove);
        increaseButton = (Button) viewProduct.findViewById(R.id.increase_quantity_button);
        decreaseButton = (Button) viewProduct.findViewById(R.id.decrease_quantity_button);    }
}

    private Cart cart;

    public CartAdapter(Cart cart) {
        this.cart = cart;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.view_cart_detail, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product product = cart.getProducts().get(position);
        int index = cart.getProducts().indexOf(product);

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

        TextView total = holder.total;
        total.setText(String.format( "Thành tiền: " + "%.2f VND", product.getQuantity() * product.getPrice()) );

        Button btnDelete = holder.deleteButton;
        btnDelete.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {
                                          if(cart.removeProduct(product)){
                                              notifyDataSetChanged();
                                          }
                                      }
                                 }
        );
        Button increaseBtn = holder.increaseButton;
        increaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              int check = cart.addProductQuantity(index);
               // product.setQuantity(ssl);
                notifyDataSetChanged();
            }
        });

        Button decreaseBtn = holder.decreaseButton;
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = cart.subtractProductQuantity(index);

                notifyDataSetChanged();
            }
        });




    }


    @Override
    public int getItemCount() {
        return cart.getProducts().size();
    }
}
