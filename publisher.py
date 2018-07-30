import paho.mqtt.client as mqtt
import sys
import time

broker="45.119.147.28"

def on_public(mosq, userdata, mid):
 mosq.disconnect()


client = mqtt.Client("publisher")
client.connect(broker, 1883)
client.publish("hello/world","asdasdsd")
client.loop(5)
#client.loop_forever()