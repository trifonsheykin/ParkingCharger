package store.smartlocks.parkingcharger;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private final int QR_SCAN = 0;
    private final int PAYMENT = 1;
    private Switch swStation;
    private TextView tvInfo;
    private Button bBuy;
    private ImageButton bAdd;
    private ImageButton bSub;
    private EditText etHours;
    // Write a message to the database
    private FirebaseDatabase database;
    //private DatabaseReference hoursRef;
    //private DatabaseReference stopTimeRef;
    private DatabaseReference node;
    private DatabaseReference nameRef;
    private boolean stationBusy;
    private boolean stationInfoAvailable = false;
    private boolean staionError = false;
    String result;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        tvInfo = findViewById(R.id.id_tv_info);
        bBuy = findViewById(R.id.id_b_buy);
        bAdd = findViewById(R.id.id_b_add);
        bSub = findViewById(R.id.id_b_sub);
        etHours = findViewById(R.id.id_et_hours);
        swStation = findViewById(R.id.id_switch);

        swStation.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                node.setValue(isChecked);
            }
        });

//        bSub.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                etHours.clearFocus();
//                Integer i = Integer.valueOf(etHours.getText().toString());
//                if(!i.equals(0))
//                    i = i - 1;
//                etHours.setText(String.valueOf(i));
//            }
//        });
//
//        bAdd.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                etHours.clearFocus();
//                Integer i = Integer.valueOf(etHours.getText().toString());
//                i = i + 1;
//                etHours.setText(String.valueOf(i));
//            }
//        });



//        bBuy.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Integer hours = Integer.valueOf(etHours.getText().toString());
//                if(!hours.equals(0)){
//                    Intent intent = new Intent(MainActivity.this, Payment.class);
//                    intent.putExtra("station", result);
//                    intent.putExtra("hours", hours);
//                    startActivityForResult(intent, PAYMENT);
//                }else{
//
//                    Toast.makeText(getApplicationContext(), "Укажите количество часов",Toast.LENGTH_SHORT).show();
//
//                }
//
//
//            }
//        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, QrScanner.class);
                startActivityForResult(intent, QR_SCAN);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            if(requestCode == QR_SCAN){
                tvInfo.setText("Запрос данных...");
                database = FirebaseDatabase.getInstance();
                result = data.getStringExtra("result");
                if(result.contains("TestDevice/station0")){
                    tvInfo.setText("Станция найдена: \n" + result);
                    swStation.setEnabled(true);
                    String ref = result + "/node";
                    node = database.getReference(ref);
                    node.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            boolean b = dataSnapshot.getValue(Boolean.class);
                            swStation.setChecked(b);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });




                }else{
                    tvInfo.setText("Ошибка \n" + result);
                    swStation.setEnabled(false);

                }
//                String node = result + "/node";
//
//                stopTimeRef = database.getReference(node);
//
//                String s = data.getStringExtra("station");
//                Integer h = data.getIntExtra("hours", 0);
//                database = FirebaseDatabase.getInstance();
//                String hours = s + "/hours";
//                hoursRef = database.getReference(hours);
//                hoursRef.setValue(h);


//
//                stopTimeRef.addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(DataSnapshot dataSnapshot) {
//                        Integer stop = dataSnapshot.getValue(Integer.class);
//                        if(stop.equals(0)){
//                            tvInfo.setText("Станция " + result + "\n\nСВОБОДНА\n\nУкажите количество часов заряда");
//                            stationBusy = false;
//
//                        }else{
//                            tvInfo.setText("Станция " + result + "\n\nВ РАБОТЕ\n\n");
//                            stationBusy = true;
//
//                        }
//                        stationInfoAvailable = true;
//
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError error) {
//                        staionError = true;
//                    }
//                });


            }else if(requestCode == PAYMENT){
//                String s = data.getStringExtra("station");
//                Integer h = data.getIntExtra("hours", 0);
//                database = FirebaseDatabase.getInstance();
//                String hours = s + "/hours";
//                hoursRef = database.getReference(hours);
//                hoursRef.setValue(h);



            }

        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
