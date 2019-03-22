package com.androiddesdecero.rxretrofitudemy;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private Button btRxRetrofit;
    private Button btRxRetrofitAnidado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUpView();

    }

    private void setUpView(){
        btRxRetrofit = findViewById(R.id.btRxRetrofit);
        btRxRetrofit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RxRetrofitActivity.class));
            }
        });

        btRxRetrofitAnidado = findViewById(R.id.btRxRetrofitAnidado);
        btRxRetrofitAnidado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), RxRetrofitAnidadoActivity.class));
            }
        });
    }
}
