/*
 * Copyright (c) 2012-2016 Arne Schwabe
 * Distributed under the GNU GPL v2 with additional terms. For full terms see the file doc/LICENSE.txt
 */

package de.blinkt.openvpn.activities;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import de.blinkt.openvpn.R;

public class StreamingSettings extends Activity {
    public String ip;
    public String res;
    public String frames;
    public String rotation;
    public String camera_ch;
    public int rotation_to_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_streaming_settings);

        final TextView forres = (TextView)findViewById(R.id.textView2);
        final TextView forframes = (TextView)findViewById(R.id.textView3);
        final TextView forrotation = (TextView)findViewById(R.id.textView4);
        final TextView forcamera = (TextView)findViewById(R.id.textView5);
        final EditText ipadd =  (EditText)findViewById(R.id.ipAddress);
        forres.setText("Choose the resolution");
        forframes.setText("Choose the framerate");
        forrotation.setText("Change this value if camera is rotated");
        forcamera.setText("Choose the camera");
        //установка разрешений для дропдауна
        final Spinner spinner = (Spinner) findViewById(R.id.resolutions);
        String[] resolution_list=new String[]{"176x144","352x288","640x360","640x480","800x600","1280x960"};
        ArrayAdapter<String> AdapterForResolutions= new ArrayAdapter<String>(StreamingSettings.this,android.R.layout.simple_spinner_item, resolution_list);
        AdapterForResolutions.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(AdapterForResolutions);
        //done
        //устсновка фреймрейта
        final Spinner spinner2 = (Spinner) findViewById(R.id.framerates);
        String[] framerate_list=new String[]{"15","20","25","30"};
        ArrayAdapter<String> AdapterForFrames= new ArrayAdapter<String>(StreamingSettings.this,android.R.layout.simple_spinner_item, framerate_list);
        AdapterForFrames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(AdapterForFrames);
        //done
        //установка поворота экрана
        final Spinner spinner3 = (Spinner) findViewById(R.id.rotator);
        String[] rotation_list=new String[]{"90","180","270","0"};
        ArrayAdapter<String> AdapterForRotation= new ArrayAdapter<String>(StreamingSettings.this,android.R.layout.simple_spinner_item, rotation_list);
        AdapterForFrames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(AdapterForRotation);
        //done
        //установка камеры
        final Spinner spinner4 = (Spinner) findViewById(R.id.camera_choice);
        String[] camera_list=new String[]{"Front","Back"};
        ArrayAdapter<String> AdapterForCamera= new ArrayAdapter<String>(StreamingSettings.this,android.R.layout.simple_spinner_item, camera_list);
        AdapterForFrames.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(AdapterForCamera);
        //done
        //
        //Выбор разрешения на спиннере
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                res = spinner.getSelectedItem().toString();
                res = res.replace('x','-');
                final EditText ipadd =  (EditText)findViewById(R.id.ipAddress);
                WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                ip = String.format("rtsp://%s:1234?h264=500000-%s-%s", ip,frames, res);
                ipadd.setText(ip);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //done
        //Выбор фреймрейта на спиннере
        spinner2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                frames = spinner2.getSelectedItem().toString();
                final EditText ipadd =  (EditText)findViewById(R.id.ipAddress);
                WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                ip = String.format("rtsp://%s:1234?h264=500000-%s-%s", ip,frames, res);
                ipadd.setText(ip);
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //Выбор положения камеры на спиннере
        spinner3.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                rotation = spinner3.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        //Выбор камеры на спиннере
        spinner4.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                camera_ch = spinner4.getSelectedItem().toString();
            }
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ipadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("label", ip);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(StreamingSettings.this, "Link is in the clipboard now :)",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getIp (View view)
    {
        final EditText ipadd =  (EditText)findViewById(R.id.ipAddress);
        WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
        ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        ip = String.format("rtsp://%s:1234?h264=500000-%s-%s", ip,frames, res);
        ipadd.setText(ip);
    }

    public void RtspStart (View view){
        Intent intent = new Intent(this, Streaming.class);
        rotation_to_pass = Integer.parseInt(rotation);
        intent.putExtra("Rotation", rotation_to_pass);
        intent.putExtra("Camera", camera_ch);
        startActivity(intent);
    }
}
