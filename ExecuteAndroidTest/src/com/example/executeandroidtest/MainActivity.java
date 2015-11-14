package com.example.executeandroidtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Spinner spinner_RunMode=(Spinner)findViewById(R.id.spinner_RunMode);
        //createFromResouce������ArrayAdapter<CharSequence>����������������
        //��һ����conetxt��Ҳ����application�Ļ�������������Ϊthis��Ҳ����ͨ��getContext()��ȡ.
        //�ڶ��������Ǵ�data source�е�array ID��Ҳ����������strings�����õ�ID�ţ�
        //������������spinnerδչ����UI��ʽ
        ArrayAdapter<CharSequence> adapter_0 = ArrayAdapter.createFromResource( this, R.array.RunMode_array, android.R.layout.simple_spinner_item);
        adapter_0.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_RunMode.setAdapter(adapter_0);
        
        Spinner spinner_RunTime=(Spinner)findViewById(R.id.spinner_RunTime);
        ArrayAdapter<CharSequence> adapter_1 = ArrayAdapter.createFromResource( this, R.array.RunTime_array, android.R.layout.simple_spinner_item);
        adapter_1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_RunTime.setAdapter(adapter_1);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
