package com.example.cwal1.mqtt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    private MqttClient client;
    String topic = "hello/world";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        String clientId = MqttClient.generateClientId();
        try {
            client = new MqttClient("tcp://49.236.136.179:1883", clientId, new MemoryPersistence());
            client.connect();
            client.publish(topic, new MqttMessage(new String("Hello MQTT !").getBytes()));
            client.subscribe(topic, new IMqttMessageListener() {
                @Override
                public void messageArrived(final String topic, final MqttMessage message) throws Exception {
                    Log.d(topic, message.toString());
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            textView.setText("Topic : " + topic + ", Message : " + message.toString());
                        }
                    });

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "error!", Toast.LENGTH_LONG).show();
        }


    }

}
