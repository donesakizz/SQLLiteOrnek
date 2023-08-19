package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnekle , btnsil , btnguncel , btntablosil;
    EditText txtAdi , txtsoyadi;
    TextView showtxt;
    //Databaseyi oluşturuyoruz
    SQLiteDatabase db ;
    String isim="Mehmet";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnekle = findViewById(R.id.btnekle);
        btnsil = findViewById(R.id.btnsil);
        btnguncel = findViewById(R.id.btnguncelle);
        btntablosil = findViewById(R.id.btntablosil);
        txtAdi = findViewById(R.id.PersonName);
        txtsoyadi = findViewById(R.id.PersonSurname);
        showtxt = findViewById(R.id.showtxt);
        //Önce tablo oluşturmamız lazım try_catch içinde
        try {
            db = this.openOrCreateDatabase("Login",MODE_PRIVATE , null  );
            db.execSQL("CREATE TABLE IF NOT EXISTS person(Id INTEGER PRIMARY KEY , ad VARCHAR ,soyad VARCHAR)");
        }catch (Exception e){
            e.printStackTrace();
        }

        showtxt.setText("");
        //sorgu oluşturulur .Databasedeki verileri cursor yardımıyla getirilir.
        Cursor c= db.rawQuery("SELECT *FROM person " ,null);
        int IdIndex = c.getColumnIndex("Id");
        int adIndex = c.getColumnIndex("ad");
        int soyadIndex = c.getColumnIndex("soyad");

        //Bir while kurarak yukarıdaki verileri sırayla  okuma işlemi gerçekleştirilir.
        while (c.moveToNext() ){
            showtxt.append("Id: " + c.getInt(IdIndex) + " Adı:" + c.getInt(adIndex)+ "Soyadı :" +c.getInt(soyadIndex) +"\n" );
        }

    }

    public void sqlprog(View view) {
        //Butonların ID'lerine göre bir Switch işlemi gerçekleştirilir
        switch (view.getId()){
            case R.id.btnekle:
                //Database ile işlem yaptığımızdan dolayı mutlaka try _catch ile kullanılır.
                try {
                    db.execSQL("INSERT INTO person(ad , soyad) VALUES ('"+txtAdi.getText().toString()+"' , '"+txtsoyadi.getText().toString()+"' )");
                    Toast.makeText(this,"Veri Eklendi" , Toast.LENGTH_SHORT).show();
                    txtAdi.setText("");
                    txtsoyadi.setText("");
                    //Bir method oluşturulur veri yükleme yapıldıktan sonra verileri tekrar güncellemek için
                    getData();

                }catch (Exception e){
                    e.printStackTrace();//Hatanın geçmişini gösterecek
                }
                break;

            case R.id.btnsil:
                try {
                    db.execSQL("DELETE FROM person SET ad='"+isim+"' WHERE ad=('"+txtAdi.getText().toString()+"') OR soyad=('"+txtsoyadi.getText().toString()+"')");
                    Toast.makeText(this , "Veriler silindi" , Toast.LENGTH_SHORT).show();
                    txtAdi.setText("");
                    txtsoyadi.setText("");
                    getData();

                }catch (Exception e){
                    e.printStackTrace();
                }
                break;

                case R.id.btnguncelle:
                    try {
                        db.execSQL("UPDATE person SET ad='"+isim+"'  WHERE ad=('"+txtAdi.getText().toString()+"') " );//Güncellemeyi oluşturacak
                        Toast.makeText(this ,"Veri Güncellendi" , Toast.LENGTH_SHORT).show();
                        txtAdi.setText("");
                        txtsoyadi.setText("");
                        getData();

                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;

                    case R.id.btntablosil:
                        try {
                            db.execSQL("DROP TABLE person ");//Tabloyu siler
                            Toast.makeText(this ,"Tablo Silindi" ,Toast.LENGTH_SHORT).show();
                            txtAdi.setText("");
                            txtsoyadi.setText("");
                            showtxt.setText("");
                            //Buraya kadar tabloyu sildik.Tekrar ekleme yapmak istediğimizde hata verir.
                            //O yüzden tabloyu yeniden açmamız gerekir.
                            db=this.openOrCreateDatabase("Login" ,MODE_PRIVATE,null);
                            db.execSQL("CREATE TABLE IF NOT EXISTS person(Id INTEGER PRIMARY KEY ,ad VARCHAR , soyad VARCHAR) ");

                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        break;



        }

    }

     //getData methodu verileri getirir.showtxt'i önce boşaltıcak arkasından databaseyi okuyacak
    private void getData() {
        showtxt.setText("");
        Cursor c=db.rawQuery("SELECT * FROM person ",null);
        int IdIndex =c.getColumnIndex("Id");
        int adIndex=c.getColumnIndex("ad");
        int soyadIndex=c.getColumnIndex("soyad");
        while(c.moveToNext()){
            showtxt.append("Id: " + c.getInt(IdIndex) + " Adı:" + c.getInt(adIndex)+ "Soyadı :" +c.getInt(soyadIndex) +"\n" );
        }
        c.close();//Databaseyi kapatır
    }


}