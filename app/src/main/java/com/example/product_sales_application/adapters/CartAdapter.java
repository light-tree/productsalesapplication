package com.example.product_sales_application.adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.product_sales_application.api.ProductApi;
import com.example.product_sales_application.manager.CartManager;
import com.example.product_sales_application.manager.CartManagerSingleton;
import com.example.product_sales_application.models.Cart;
import com.example.product_sales_application.models.Product;
import com.example.product_sales_application.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    public static DecimalFormat formatter = new DecimalFormat("###,###,###");
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
        decreaseButton = (Button) viewProduct.findViewById(R.id.decrease_quantity_button);

    }
}

    private Cart cart;
    public  Context context;
    public CartManager cartManager ;
    private ProgressDialog dialog;


    public CartAdapter(Cart cart) {
        this.cart = cart;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        context = parent.getContext();
        cartManager =  CartManagerSingleton.getInstance(context);

        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.view_cart_detail, parent, false);

        ViewHolder viewHolder = new ViewHolder(contactView);

        dialog = new ProgressDialog(context);
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(true);
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
        price.setText("Đơn giá: " + formatter.format(product.getPrice())+" VNĐ");

        TextView quantity = holder.quantity;
        quantity.setText(String.format(" %d ", product.getQuantity()));

        ImageView imageView = holder.imgProduct;
        Picasso.get().load(product.getUrl())
                .into(imageView);

        TextView total = holder.total;
        total.setText("Tổng: " + formatter.format(product.getQuantity() * product.getPrice())+" VNĐ");

        Button btnDelete = holder.deleteButton;
        btnDelete.setOnClickListener(new View.OnClickListener() {
                                      @Override
                                      public void onClick(View v) {

                                          List<Product> productList = cartManager.getCart();
                                          if(cart.removeProduct(product)){
                                              notifyDataSetChanged();
                                              productList = cart.getProducts();
                                              cartManager.saveCart(productList);
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
                updateQuantity(product, context);



            }
        });

        Button decreaseBtn = holder.decreaseButton;
        decreaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int check = cart.subtractProductQuantity(index);
                List<Product> productList = cartManager.getCart();
                productList = cart.getProducts();
                cartManager.saveCart(productList);

                notifyDataSetChanged();
            }
        });




    }


    @Override
    public int getItemCount() {
        return cart.getProducts().size();
    }

    private  void updateQuantity(Product product, Context context){
        dialog.setMessage("Kiểm tra số lượng");
        dialog.show();
        int productId = product.getId();

        ProductApi.productApi.getProductById(productId).enqueue(
                new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {
                        Product currentProduct = response.body();
                        if(currentProduct.getQuantity()< product.getQuantity()){
                            showErrorMessage("Số lượng còn lại không đủ", context);
                            dialog.hide();
                        } else {
                            List<Product> productList = cartManager.getCart();
                            productList = cart.getProducts();
                            cartManager.saveCart(productList);
                            dialog.hide();
                            notifyDataSetChanged();

                        }
                    }
                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {

                    }
                }
        );

    }

    public void showErrorMessage(String message,Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Error");
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }




}
