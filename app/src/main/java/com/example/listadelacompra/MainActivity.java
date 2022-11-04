package com.example.listadelacompra;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import com.example.listadelacompra.adapters.ProductosAdapter;
import com.example.listadelacompra.modelos.Producto;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;

import com.example.listadelacompra.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<Producto> productosList;

    //RECYCLER
    private ProductosAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        productosList = new ArrayList<>();

        adapter = new ProductosAdapter(productosList,R.layout.producto_model_card,this);
        layoutManager = new GridLayoutManager(this,1);
        binding.contentMain.contenedor.setAdapter(adapter);
        binding.contentMain.contenedor.setLayoutManager(layoutManager);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProducto().show();
            }
        });
    }

    private AlertDialog createProducto() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Agregar Producto");
        builder.setCancelable(false);

        View productoAlertView = LayoutInflater.from(this).inflate(R.layout.producto_model_alert, null);
        builder.setView(productoAlertView);

        EditText txtNombre = productoAlertView.findViewById(R.id.txtNombreProductoAlert);
        EditText txtCantidad = productoAlertView.findViewById(R.id.txtCantidadProductoAleret);
        EditText txtPrecio = productoAlertView.findViewById(R.id.txtPrecioProductoAlert);
        TextView lblTotal = productoAlertView.findViewById(R.id.lblTotalProductoAlert);

        TextWatcher textWatcher = new TextWatcher() {

            /**
             * Al modifiar un cuadro de texto
             * @param charSequence -> enviar el contenido que habia antes del cambio
             * @param i
             * @param i1
             * @param i2
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * Al modificar un cuadro de texto
             * @param charSequence -> Envia el texto actual despues de la  modificacion
             * @param i
             * @param i1
             * @param i2
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * se dispara el terminar la modificacion
             * @param editable -> Envia el contenido final del cuadro de texto
             */
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int cantidad = Integer.parseInt(txtCantidad.getText().toString());
                    float precio = Float.parseFloat(txtPrecio.getText().toString());
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance(); //IMPORTANTE MONEDA AUTOMATICA
                    lblTotal.setText(numberFormat.format(cantidad * precio));
                } catch (NumberFormatException ex) {
                }
            }
        };

        txtCantidad.addTextChangedListener(textWatcher);
        txtPrecio.addTextChangedListener(textWatcher);

        builder.setNegativeButton("CANCELAR", null);
        builder.setPositiveButton("AGREGAR", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!txtNombre.getText().toString().isEmpty() && !txtCantidad.getText().toString().isEmpty() && !txtPrecio.getText().toString().isEmpty()) {
                    Producto producto = new Producto(txtNombre.getText().toString(), Integer.parseInt(txtCantidad.getText().toString()), Float.parseFloat(txtPrecio.getText().toString()));
                    productosList.add(0,producto);
                    adapter.notifyItemInserted(0);
                } else {
                    Toast.makeText(MainActivity.this, "FALTAN DATOS", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return builder.create();
    }


}