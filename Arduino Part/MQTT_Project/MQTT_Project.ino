#include <ESP8266WiFi.h>
#include <PubSubClient.h>
#include <dht11.h>

// Update these with values suitable for your network.

const char* ssid = "Simonphone";
const char* password = "424zylsimon";
const char* mqtt_server = "8zt9cfa.mqtt.iot.gz.baidubce.com";
const char* IOT_USERNAME  = "8zt9cfa/thing";
const char* IOT_KEY       = "c7J29H7Ur2o12WO9";

//DHT11
int pinDHT11 = D4;
dht11 DHT11;

//LED指示灯
int ledPin = D6;
boolean ledState = false;

WiFiClient espClient;
PubSubClient client(espClient);
long lastMsg = 0;
char msg[50];
int value = 0;

void setup_wifi()
{
  delay(10);
  // We start by connecting to a WiFi network
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);

  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED)
  {
    delay(500);
    Serial.print(".");
  }

  randomSeed(micros());

  Serial.println("");
  Serial.println("WiFi connected");
  Serial.println("IP address: ");
  Serial.println(WiFi.localIP());
}
void callback(char* topic, byte* payload, unsigned int length)
{
  Serial.print("Message arrived server[");
  Serial.print(topic);
  Serial.print("] ");
  for (int i = 0; i < length; i++)
  {
    Serial.print((char)payload[i]);
  }
  Serial.println();
  if (((char)payload[0] == '0') && ((char)payload[1] == '1'))
  {
    if (ledState == false) {
      Serial.println("ledState:false,turn on led");
      digitalWrite(ledPin, HIGH);
      ledState = true;
    }
    else {
      Serial.println("ledState:true,turn off led");
      digitalWrite(ledPin, LOW);
      ledState = false;
    }
  }
}

void reconnect()
{
  // Loop until we're reconnected
  while (!client.connected())
  {
    Serial.print("Attempting MQTT connection...");
    // client ID
    String clientId = "ESP8266Clien111";
    // Attempt to connect
    if (client.connect(clientId.c_str(), IOT_USERNAME, IOT_KEY))
    {
      Serial.print(F("MQTT Connected. Client id = "));
      Serial.println(clientId.c_str());
      client.subscribe("topic01");
    }
    else
    {
      Serial.print("failed, rc=");
      Serial.print(client.state());
      Serial.println(" try again in 5 seconds");
      delay(5000);
    }
  }
}

void setup()
{
  pinMode(ledPin, OUTPUT);
  Serial.begin(115200);
  setup_wifi();
  client.setServer(mqtt_server, 1883);
  client.setCallback(callback);
}

void loop()
{
  if (!client.connected())
  {
    reconnect();
  }
  client.loop();
  long now = millis();
  //两秒发送一次
  if (now - lastMsg > 2000)
  {
    lastMsg = now;
    char temp[20];
    char hum[20];
    int chk = DHT11.read(pinDHT11);
    int Humidity = DHT11.humidity;
    int Temperature = DHT11.temperature ;
    snprintf (temp, 20, "T#%ld", Temperature);
    snprintf (hum, 20, "H#%ld", Humidity);
    //发送温度值
    Serial.print("ESP8266Clien111 Send Temperature To Server:");
    Serial.println(temp);
    client.publish("topic01", temp);
    delay(500);
    //发送湿度值
    Serial.print("ESP8266Clien111 Send Humidity To Server:");
    Serial.println(hum);
    client.publish("topic01", hum);
    delay(500);
    client.publish("topic01", "{device:ESP8266Clien111,'status':'on'}");
  }
}
