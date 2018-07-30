package com.example.cwal1.mqtt;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText_topic;
    Button button_subscribe, button_clear;
    private MqttClient client;
    String topic = "hello/+";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Widgets
        textView            = findViewById(R.id.textView);
        editText_topic      = findViewById(R.id.editText_topic);
        button_subscribe    = findViewById(R.id.button_subscribe);
        button_clear        = findViewById(R.id.button_clear);
        editText_topic.setText(topic);

        String clientId = MqttClient.generateClientId();
        try {
            client = new MqttClient("tcp://49.236.136.179:1883", clientId, new MemoryPersistence());
            client.connect();
            // hello message
            client.publish("hello/world", new MqttMessage(new String("Hello MQTT !").getBytes()));
            client.subscribe(topic, new IMqttMessageListener() {
                @Override
                public void messageArrived(final String topic, final MqttMessage message) throws Exception {
                    Log.d(topic, message.toString());
                    runOnUiThread(new Runnable()
                    {
                        public void run()
                        {
                            textView.append((new SimpleDateFormat("YYYY-MM-dd hh:mm:ss")).format(new Date())+"\n          Topic : " + topic + "\tMessage : " + message.toString() + "\n");
                        }
                    });

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "error!", Toast.LENGTH_LONG).show();
        }

        // 구독 버튼 클릭시 기존 토픽 구독 해제 후 입력받은 토픽 구독
        button_subscribe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    client.unsubscribe(topic);
                    topic = editText_topic.getText().toString();
                    client.subscribe(topic, new IMqttMessageListener() {
                        @Override
                        public void messageArrived(final String topic, final MqttMessage message) throws Exception {
                            Log.d(topic, message.toString());
                            runOnUiThread(new Runnable()
                            {
                                public void run()
                                {
                                    textView.append((new SimpleDateFormat("YYYY-MM-dd hh:mm:ss")).format(new Date())+"\n          Topic : " + topic + "\tMessage : " + message.toString() + "\n");
                                }
                            });

                        }
                    });
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }
        });

        // 삭제 버튼 클릭시 내용 제거
        button_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("");
            }
        });

    }

}
