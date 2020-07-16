package store.smartlocks.parkingcharger;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class Payment extends AppCompatActivity {

    private TextView tvInfo;
    private Button bPay;
    private String s;
    private Integer h;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        tvInfo = findViewById(R.id.id_tv_pay_info);
        bPay = findViewById(R.id.id_b_payment);

        Intent intent = getIntent();
        s = intent.getStringExtra("station");//intent.putExtra("rowId", id);
        if(s == null){
            s = "Не определена";
            bPay.setEnabled(false);
        }
        h = intent.getIntExtra("hours", 0);

        tvInfo.setText("Станция:\n" + s + "\n\nКоличество часов: " + h);

        bPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("station", s);
                intent.putExtra("hours", h);
                setResult(RESULT_OK, intent);
                finish();



            }
        });





    }


}
