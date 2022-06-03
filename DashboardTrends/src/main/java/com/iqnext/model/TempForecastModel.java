package com.iqnext.model;

public class TempForecastModel {
	
	private long ds;
	private float y;
	private float yhat1;
    private float residual1; 
    private float trend;
    private float season_daily;
	public long getDs() {
		return ds;
	}
	public void setDs(long ds) {
		this.ds = ds;
	}
	
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	public float getYhat1() {
		return yhat1;
	}
	public void setYhat1(float yhat1) {
		this.yhat1 = yhat1;
	}
	public float getResidual1() {
		return residual1;
	}
	public void setResidual1(float residual1) {
		this.residual1 = residual1;
	}
	public float getTrend() {
		return trend;
	}
	public void setTrend(float trend) {
		this.trend = trend;
	}
	public float getSeason_daily() {
		return season_daily;
	}
	public void setSeason_daily(float season_daily) {
		this.season_daily = season_daily;
	}

}
