package com.example.canopytemperature;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;


/**
 * TemperatureActivity的主要作用是实现温度的获取显示以及控制节点对应的电机，这部分代码还没有写完
 */
public class TemperatureActivity extends AppCompatActivity {

    /**
     * 客户端的ID
     */
    private String clientId = "client111";
    /**
     * 服务端IP地址
     */
    private String serverURI = "tcp://8zt9cfa.mqtt.iot.gz.baidubce.com:1883";
    /**
     * 用户名，在云端进行配置
     */
    private String userName = "8zt9cfa/thing";
    /**
     * 用户密码，在云端进行配置
     */
    private String passWord = "c7J29H7Ur2o12WO9";

    /**
     * 客户端的一个引用
     */
    private MqttClient client_1;
    //配置  保存控制客户端连接到服务器的方式的选项集。
    private MqttConnectOptions options;

    MqttConnectThread mqttConnectThread = new MqttConnectThread();

    /**
     * 相应的控件
     */
    Button buttonLevelOne;
    TextView nodeTemperature;
    Button buttonConnect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        initControls();
        initializationMQTT();
        mqttConnectThread.start();//执行连接服务器任务
    }

    /**
     * 初始化ToolBar
     */
    public void setToolBar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar_control);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);//设计隐藏标题
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//设置显示返回键
        //返回按键的监控事件
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(TemperatureActivity.this, MainActivity.class);
                startActivity(intent);
                try {
                    client_1.disconnect();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                TemperatureActivity.this.finish();
            }
        });
    }


    /**
     * 初始化控件
     */
    public void initControls() {
        nodeTemperature = findViewById(R.id.node_temperature);
        buttonConnect = findViewById(R.id.connet_button);
        StatusBarUtils.setColor(this, getResources().getColor(R.color.colorPrimary));
        setToolBar();
    }

    /**
     * 初始化MQTT的配置
     */
    private void initializationMQTT() {
        try {
            // MqttClient(String serverURI, String clientId, MqttClientPersistence persistence)
            // 第一个参数是主机的URI地址，第二个参数是是客户端的唯一标识符(不能和其它的客户端重名)，第三个参数是指数据保存在内存
            client_1 = new MqttClient(serverURI, clientId, new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }
        options = new MqttConnectOptions();//MQTT的连接设置
        options.setCleanSession(true);//设置是否清空session,这里如果设置为false表示服务器会保留客户端的连接记录，这里设置为true表示每次连接到服务器都以新的身份连接
        options.setUserName(userName);//设置连接的用户名(自己的服务器没有设置用户名)
        options.setPassword(passWord.toCharArray());//设置连接的密码(自己的服务器没有设置密码)
        options.setConnectionTimeout(10);// 设置连接超时时间 单位为秒
        options.setKeepAliveInterval(5);// 设置会话心跳时间 单位为秒 服务器会每隔1.5*20秒的时间向客户端发送个消息判断客户端是否在线，但这个方法并没有重连的机制
        client_1.setCallback(new MqttCallback() {
            @Override//连接丢失后，会执行这里
            public void connectionLost(Throwable throwable) {

            }

            /**
             * This method is called when a message arrives from the server
             * 当来自服务器的消息到达以后，这个函数就会被调用
             * @param topic
             * @param mqttMessage
             * @throws Exception
             */
            @Override
            public void messageArrived(final String topic, MqttMessage mqttMessage) throws Exception {
                final String getMessage = mqttMessage.toString();//消息
                runOnUiThread(new Runnable() {
                    public void run() {
                        String flag = getMessage.substring(0, 2);
                        if (flag.equals("T#")) {
                            String strTemperature = getMessage.substring(2, getMessage.length());
                            nodeTemperature.setText(strTemperature + " ℃ ");
                            Toast.makeText(TemperatureActivity.this,  topic +":" + getMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

            @Override//订阅主题后会执行到这里
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    public void controlButton(View view) {
        switch (view.getId()) {
            case R.id.connet_button:
                try {
                    client_1.subscribe("topic01", 0);//设置(订阅)接收的主题,主题的级别是0
                    Log.e("Succeed", "Succeed to subscribe the topic");
                    Toast.makeText(TemperatureActivity.this, "Succeed to subscribe", Toast.LENGTH_SHORT).show();
                } catch (MqttException e) {
                    Log.e("Failed", "Failed to subscribe the topic");
                    Toast.makeText(TemperatureActivity.this, "Failed to subscribe", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;
            case R.id.fan_level1:
                MqttMessage mqttMessage = new MqttMessage("01".getBytes());
                try {
                    client_1.publish("topic01", mqttMessage);
                    Log.e("Send", "Send to topic01 " + mqttMessage);
                    Toast.makeText(TemperatureActivity.this, "send to topic01：" + mqttMessage, Toast.LENGTH_SHORT).show();
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }

    /**
     * 连接服务器
     */
    class MqttConnectThread extends Thread {
        public void run() {
            try {
                client_1.connect(options);//连接服务器,连接不上会阻塞在这
                Log.e("is Looping", "wating for connecting server");
                runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Connecting Successfully", Toast.LENGTH_SHORT).show();
                        Log.e("Run", "succeed to connect server");
                    }
                });
            } catch (MqttSecurityException e) {
                //安全问题连接失败
                Log.e("Security issues lead to failure ", "" + e);
            } catch (MqttException e) {
                //连接失败原因
                Log.e("The reason lead to failure ", "" + e);
            }
        }
    }
}

