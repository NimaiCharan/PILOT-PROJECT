from django.http import HttpResponse
from matplotlib.pyplot import text
from .utils import Methods as mt
import pickle
import pandas as pd
import json
import environ

env = environ.Env(interpolate=True)

def index(request):
    return HttpResponse("Hello, world. You're at the polls index.")


def get_data(request):
    data = mt.get_data_from_db(mt.connect_to_db())
    data["time"] = pd.to_datetime(data['time'], format='%I:%M:%S %p').dt.strftime('%H:%M')
    date_time =data['date']+" "+data['time']
    date_data = pd.to_datetime(date_time)
    data["date_time"] = date_data
    data["feels_like"] = data["feels_like"].astype("float64")
    ml_data  = pd.DataFrame({
        'ds':data["date_time"],
        'y':data["feels_like"]
        })
    m = None
    n = None
    file = env.str('ML_MODEL_FILE') 
    
    with open(file , 'rb') as f:
        m = pickle.load(f)
    
    future = m.make_future_dataframe(ml_data, periods=2*24*4, n_historic_predictions=True)
   
    try:
        forecast = m.predict(future)
        forecast = forecast.to_json(orient='records')
        
        #response_data = json.dumps(forecast)
        #print(type(forecast))
        return HttpResponse(forecast.encode('utf-8'), content_type="application/json")
        
        #print(forecast)
    except Exception as e:
        return HttpResponse(e)

def get_ac_status(request):
    data = mt.get_data_from_db(mt.connect_to_db())
    data["time"] = pd.to_datetime(data['time'], format='%I:%M:%S %p').dt.strftime('%H:%M')
    date_time =data['date']+" "+data['time']
    date_data = pd.to_datetime(date_time)
    data["date_time"] = date_data
    data["feels_like"] = data["feels_like"].astype("float64")
    ml_data  = pd.DataFrame({
        'ds':data["date_time"],
        'y':data["feels_like"]
        })
    m = None
    file_ac = env.str('ML_MODEL_FILE_AC') 
    file = env.str('ML_MODEL_FILE') 
    with open(file , 'rb') as f:
        m = pickle.load(f)
    with open(file_ac , 'rb') as f:
        n = pickle.load(f)
    future = m.make_future_dataframe(ml_data, periods=2*24*4, n_historic_predictions=True)
    #print(future.tail())
    try:
        forecast = m.predict(future)
        test_X = forecast['yhat1'].values.reshape(-1,1)
        ac_forecast = n.predict(test_X)
        
        #ac_forecast = ac_forecast.to_json(orient='records')
        #response_data = json.dumps(forecast)
        #print(type(forecast))
        return_data = pd.DataFrame(
            {
                "temp":forecast['yhat1'],
                "ac_status":ac_forecast,
            }
        )
        return_data = return_data.head(48)
        return_data = (return_data.to_json(orient='records'))

        return HttpResponse(return_data, content_type="application/json")
        
        #print(forecast)
    except Exception as e:
        return HttpResponse(e)
   