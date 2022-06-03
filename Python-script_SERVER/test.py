import pickle
import CreateDataFrame as cmlm
import pandas as pd
from neuralprophet import NeuralProphet



data = cmlm.get_data_from_db(cmlm.connect_to_db())
data["time"] = pd.to_datetime(data['time'], format='%I:%M:%S %p').dt.strftime('%H:%M')

date_time =data['date']+" "+data['time']
date_data = pd.to_datetime(date_time)
data["date_time"] = date_data
data["feels_like"] = data["feels_like"].astype("float64")
ml_data  = pd.DataFrame({
    'ds':data["date_time"],
    'y':data["feels_like"]
    })
print(type(ml_data["y"][0]))
m = None
file = "D:\\ECLIPSE\\iqnext-PILOT-PROJECT\\Python-script_SERVER\\neuralprophet_model.pkl"
with open(file , 'rb') as f:
    m = pickle.load(f)
future = m.make_future_dataframe(ml_data, periods=30//5*24*4, n_historic_predictions=True)

try:
    forecast = m.predict(future)
    print(forecast)
except Exception as e:
    print(e)