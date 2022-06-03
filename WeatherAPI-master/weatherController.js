const axios = require("axios");
const { toJSON } = require("flatted");
exports.getWeather = (req, res) => {
  openWeatherResponse = false;
  weatherData = "";

  ACU_URL =
    "http://dataservice.accuweather.com/currentconditions/v1/204108?apikey=";
  ACU_KEY = "MigvGMvRkPXsC3gAprrLLVxkEEmOHo1h";
  API_KEY = "8403f06e4baf53dea51f64ac009892d9";
  API_URL =
    "https://api.openweathermap.org/data/2.5/weather?lat=12.9716&lon=77.5946&appid=";

  axios
    .get(API_URL + API_KEY)
    .then((data) => {
      console.log(new Date().toLocaleTimeString() + `statusCode: ${data.status}`);
     // console.log(data.data.main);

      weatherData = data.data.main;
      axios
      .get(ACU_URL + ACU_KEY)
      .then((data) => {
    
        weatherData.date = new Date().toISOString().slice(0, 10)
        weatherData.time = new Date().toLocaleTimeString()
        weatherData.isRain = data.data[0].HasPrecipitation;
        weatherData.rainType = data.data[0].PrecipitationType;
        weatherData.temp = parseFloat(weatherData.temp-273.15).toFixed(2);
        weatherData.feels_like = parseFloat(weatherData.feels_like-273.15).toFixed(2);
        weatherData.temp_min = parseFloat(weatherData.temp_min-273.15).toFixed(2);
        weatherData.temp_max = parseFloat(weatherData.temp_max-273.15).toFixed(2);
        res.send(weatherData);
       // res.send(toJSON(data.data.data));
      })
      .catch((error) => {
        console.error(error);
        return error;
      });
      openWeatherResponse = true;
    })
    .catch((error) => {
      console.error(error);
      return error;
    });

  if (openWeatherResponse) {
    
  }
};
