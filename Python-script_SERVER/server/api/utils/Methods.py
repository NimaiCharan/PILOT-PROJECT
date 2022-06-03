#DesignedBy: Nimai Charan

#Importing the libraries
from email import header
import pandas as pd
import matplotlib.pyplot as plt
import mysql.connector
from mysql.connector import Error

#Connecting to the MYSQL Database
def connect_to_db():
    try:
        connection = mysql.connector.connect(
            host='43.204.47.39',
            #host='localhost',
            database='iqnext_v2_nimai',
            user='ncharan',
            password='Charan@123')
        if(connection.is_connected()):
            return connection
    except Error as e:
        print(e)
        return e

def get_data_from_db(connection):
    try:
        cursor = connection.cursor()
        query = "SELECT date, time, feels_like FROM iqnext_v2_nimai.iq_weather_time_series"
        cursor.execute(query)
        data = cursor.fetchall()
        headers= ["date", "time", "feels_like"]
        df_data = pd.DataFrame(data, columns=headers)
        return df_data
    except Error as e:
        print(e)
        return e




