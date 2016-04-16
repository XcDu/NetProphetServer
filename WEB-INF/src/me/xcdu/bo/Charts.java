package me.xcdu.bo;

import com.google.gson.Gson;

public class Charts<T> {
  private T wifi;
  private T mobile;

  public Charts() {}

  public Charts(T wifi, T mobile) {
    this.wifi = wifi;
    this.mobile = mobile;
  }

  public void setWifi(T wifi) {
    this.wifi = wifi;
  }

  public void setMobile(T mobile) {
    this.mobile = mobile;
  }

  public T getWifi() {
    return wifi;
  }

  public T getMobile() {
    return mobile;
  }

  public String toJson() {
    return new Gson().toJson(this);
  }
}
