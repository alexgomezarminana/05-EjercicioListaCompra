package com.example.a05_ejerciciolistacompra;

import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;

import com.example.a05_ejerciciolistacompra.adapters.ProductosAdapter;
import com.example.a05_ejerciciolistacompra.modelos.Producto;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;



import com.example.a05_ejerciciolistacompra.databinding.ActivityMainBinding;

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

    // - RECYCLER -
    private ProductosAdapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        productosList = new ArrayList<>();

        int columnas;
        //HORIZONTAL -> 2
        //VERTICAL -> 1
        //HACER DESDE LAS CONFIGURACIONES DE LA ACTIVIDAD -> orientation // PORTRAIT -> Vertical / LANDSCAPE -> Horizontal
        //Esto es un if pero en ternario
        columnas = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT ? 1 : 2;


        adapter = new ProductosAdapter(productosList, R.layout.producto_model_card, this);
        layoutManager = new GridLayoutManager(this, columnas
        );
        binding.contentMain.contenedor.setAdapter(adapter);
        binding.contentMain.contenedor.setLayoutManager(layoutManager);


        binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createProducto().show();
            }
        });
    }

    private AlertDialog createProducto(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle(getString(R.string.alert_title_crear));//Para los idiomas
        builder.setCancelable(false);

        View productoAlertView = LayoutInflater.from(this).inflate(R.layout.producto_model_alert, null);
        builder.setView(productoAlertView);

        EditText txtNombre = productoAlertView.findViewById(R.id.txtNombreProductoAlert);
        EditText txtCantidad = productoAlertView.findViewById(R.id.txtCantidadProductoAlert);
        EditText txtPrecio = productoAlertView.findViewById(R.id.txtPrecioProductoAlert);
        TextView lblTotal = productoAlertView.findViewById(R.id.lblTotalProductoAlert);

        TextWatcher textWatcher= new TextWatcher() {

            /**
             * al modificar el texto
             * @param charSequence -> envia el contenido que habia antes debl cambio
             * @param i
             * @param i1
             * @param i2
             */
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * al modificar cuadro de texto
             * @param charSequence _> Envia el texto actual despues de la modificacion
             * @param i
             * @param i1
             * @param i2
             */
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            /**
             * se dispara al terminar la modificacion
             * @param editable -> envia el contenido final del cuadro de texto
             */
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    int cantidad = Integer.parseInt(txtCantidad.getText().toString());
                    float precio = Float.parseFloat(txtPrecio.getText().toString());
                    NumberFormat numberFormat = NumberFormat.getCurrencyInstance();
                    lblTotal.setText(numberFormat.format(cantidad * precio));
                }catch (NumberFormatException ex){}

            }
        };

        txtCantidad.addTextChangedListener(textWatcher);
        txtPrecio.addTextChangedListener(textWatcher);


        builder.setNegativeButton(getString(R.string.btn_alert_cancel), null);
        builder.setPositiveButton(getString(R.string.btn_alert_crear), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!txtNombre.getText().toString().isEmpty() && !txtCantidad.getText().toString().isEmpty() && !txtPrecio.getText().toString().isEmpty()){
                    Producto producto = new Producto(
                                        txtNombre.getText().toString(),
                                        Integer.parseInt(txtCantidad.getText().toString()),
                                        Float.parseFloat(txtPrecio.getText().toString())
                    );
                    productosList.add(0, producto);
                    adapter.notifyItemInserted(0);
                }else {
                    Toast.makeText(MainActivity.this, "FALTAN DATOS", Toast.LENGTH_SHORT).show();
                }
            }
        });


        return builder.create();
    }

    /**
     * Se diasparaANTES de que se elimine la actividad
     * @param outState -> guardo los datos
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("LISTA", productosList);
    }

    /**
     * Se dispara DESPUES de crear la actividad de nuevo
     * @param savedInstanceState -> recupero los datos
     */
    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        ArrayList<Producto> temp = (ArrayList<Producto>) savedInstanceState.getSerializable("LISTA");
        productosList.addAll(temp);
        adapter.notifyItemRangeInserted(0, productosList.size());
    }
}