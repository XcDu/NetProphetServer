package me.xcdu.bo;

import java.util.ArrayList;

import com.google.gson.Gson;

import me.xcdu.bo.ChartBuilder.DelayLineChart;
import me.xcdu.bo.ChartBuilder.DelayPieChart;
import me.xcdu.bo.ChartBuilder.DelayStackedBarChart;
import me.xcdu.bo.ChartBuilder.ErroRateChart;
import me.xcdu.bo.ChartBuilder.RedirectionChart;
import me.xcdu.po.HttpRequestInfo;

public class UrlListCharts {

  private DelayLineChart delayLineChart;
  private DelayStackedBarChart delayStackedBarChart;
  private DelayPieChart delayPieChart;
  private ErroRateChart errorRateChart;
  private RedirectionChart redirectionChart;

  public UrlListCharts() {
    ChartBuilder chartBuilder = new ChartBuilder();
    delayLineChart = chartBuilder.createDelayLineChart();
    delayStackedBarChart = chartBuilder.createDelayStackedBarChart();
    delayPieChart = chartBuilder.createDelayPieChart();
    errorRateChart = chartBuilder.createErrorRateChart();
    redirectionChart = chartBuilder.createRedirectionChart();
  }

  public UrlListCharts(UrlListCharts charts) {
    delayLineChart = charts.delayLineChart;
    delayStackedBarChart = charts.delayStackedBarChart;
    delayPieChart = charts.delayPieChart;
    errorRateChart = charts.errorRateChart;
    redirectionChart = charts.redirectionChart;
  }

  public void setAllCharts(ArrayList<HttpRequestInfo> arrayWithoutRedirection,
      ArrayList<HttpRequestInfo> redirectionArray) {
    setDelayLineChart(arrayWithoutRedirection);
    setDelayStackedBarChart(arrayWithoutRedirection);
    setDelayPieChart(arrayWithoutRedirection);
    setErrorRateChart(arrayWithoutRedirection);
    setRedirectionChart(redirectionArray);
  }

  public void setDelayLineChart(ArrayList<HttpRequestInfo> infoArray) {
    if (!delayLineChart.data.isEmpty()) {
      delayLineChart.data.clear();
    }
    if (infoArray.isEmpty()) {
      System.out.println("Warning: now Element");
    }
    ArrayList<HttpRequestInfo> infos = new ArrayList<HttpRequestInfo>();
    for (int i = 0; i < infoArray.size(); ++i) {
      if (!infoArray.get(i).isFailedRequest()) {
        infos.add(infoArray.get(i));
      }
    }
    // TODO(xcdu): modify if size() < 3
    for (int i = 0; i < infos.size(); ++i) {
      delayLineChart.data.put(infos.get(i).getStartTime(),
          infos.get(i).getOverallDelay());
    }
  }

  public void setDelayStackedBarChart(ArrayList<HttpRequestInfo> infoArray) {
    delayStackedBarChart.categories.clear();
    delayStackedBarChart.seriesAddElementWithName("DNS Delay");
    delayStackedBarChart.seriesAddElementWithName("Connection Delay");
    delayStackedBarChart.seriesAddElementWithName("Handshake Delay");
    delayStackedBarChart.seriesAddElementWithName("TLS Delay");
    delayStackedBarChart.seriesAddElementWithName("Require Write Delay");
    delayStackedBarChart.seriesAddElementWithName("Server Delay");
    delayStackedBarChart.seriesAddElementWithName("TTFB Delay");
    delayStackedBarChart.seriesAddElementWithName("Response TransmissionDelay");
    ArrayList<HttpRequestInfo> infos = new ArrayList<HttpRequestInfo>();
    for (int i = 0; i < infoArray.size(); ++i) {
      if (!infoArray.get(i).isFailedRequest()) {
        infos.add(infoArray.get(i));
      }
    }
    for (int i = 0; i < infos.size(); ++i) {
      HttpRequestInfo info = infos.get(i);
      delayStackedBarChart.categoriesAddElement(info.getReqID());

      delayStackedBarChart.seriesAddData(0, info.getDnsDelay());
      delayStackedBarChart.seriesAddData(1, info.getConnDelay());
      delayStackedBarChart.seriesAddData(2, info.getHandshakeDelay());
      delayStackedBarChart.seriesAddData(3, info.getTlsDelay());
      delayStackedBarChart.seriesAddData(4, info.getReqWriteDelay());
      delayStackedBarChart.seriesAddData(5, info.getServerDelay());
      delayStackedBarChart.seriesAddData(6, info.getTTFBDelay());
      delayStackedBarChart.seriesAddData(7, info.getRespTransDelay());
    }
  }

  public void setDelayPieChart(ArrayList<HttpRequestInfo> infoArray) {
    if (infoArray.isEmpty()) {
      System.out.println("Error no element");
    }
    delayPieChart.series.clear();
    delayPieChart.seriesAddElement();
    double dns, conn, handshake, tls, reqWrite, server, ttfb, respTrans;
    dns = conn = handshake = tls = reqWrite = server = ttfb = respTrans = 0;
    for (int i = 0; i < infoArray.size(); ++i) {
      HttpRequestInfo info = infoArray.get(i);
      dns += info.getDnsDelay();
      conn += info.getConnDelay();
      handshake += info.getHandshakeDelay();
      tls += info.getTlsDelay();
      reqWrite += info.getReqWriteDelay();
      server += info.getServerDelay();
      ttfb += info.getTTFBDelay();
      respTrans += info.getRespTransDelay();
    }
    double sum =
        dns + conn + handshake + tls + reqWrite + server + ttfb + respTrans;
    delayPieChart.series.get(0).name = "Percentage of Delays";
    delayPieChart.series.get(0).colorByPoint = true;
    delayPieChart.setSeriesData(0, "DNS Delay", dns / sum);
    delayPieChart.setSeriesData(0, "Connection Delay", conn / sum);
    delayPieChart.setSeriesData(0, "Handshake Delay", handshake / sum);
    delayPieChart.setSeriesData(0, "TLS Delay", tls / sum);
    delayPieChart.setSeriesData(0, "Require Write Delay", reqWrite / sum);
    delayPieChart.setSeriesData(0, "Server Delay", server / sum);
    delayPieChart.setSeriesData(0, "TTFB Delay", ttfb / sum);
    delayPieChart.setSeriesData(0, "Response TransmissionDelay",
        respTrans / sum);
  }

  public void setErrorRateChart(ArrayList<HttpRequestInfo> infoArray) {
    if (infoArray.isEmpty()) {
      System.out.println("Error Rate Chart Error, no element");
    }
    double error_cnt = 0;
    for (int i = 0; i < infoArray.size(); ++i) {
      if (infoArray.get(i).isFailedRequest()) {
        error_cnt++;
      }
    }
    if (!infoArray.isEmpty()) {
      errorRateChart.errorRate = error_cnt / infoArray.size();
    }
  }

  public void setRedirectionChart(ArrayList<HttpRequestInfo> redirectionArray) {
    for (int i = 0; i < redirectionArray.size(); ++i) {
      redirectionChart.redirectionList.add(redirectionArray.get(i).getUrl());
    }
  }

  public String getJson() {
    Gson gson = new Gson();
    return gson.toJson(this);
  }
}
