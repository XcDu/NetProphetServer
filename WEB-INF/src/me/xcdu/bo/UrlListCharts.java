package me.xcdu.bo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.TimeZone;

import com.google.gson.Gson;

import me.xcdu.bo.ChartBuilder.DelayLineChart;
import me.xcdu.bo.ChartBuilder.DelayPieChart;
import me.xcdu.bo.ChartBuilder.DelayStackedBarChart;
import me.xcdu.bo.ChartBuilder.ErrorRateChart;
import me.xcdu.bo.ChartBuilder.RedirectionChart;
import me.xcdu.po.HttpRequestInfo;

public class UrlListCharts {

  private DelayLineChart delayLineChart;
  private DelayStackedBarChart delayStackedBarChart;
  private DelayPieChart delayPieChart;
  private ErrorRateChart errorRateChart;
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
    if (infoArray.isEmpty())
      return;
    final int kMAX_NUM = 10;
    delayStackedBarChart.categories.clear();
    delayStackedBarChart.seriesAddElementWithName("DNS Delay");
    delayStackedBarChart.seriesAddElementWithName("Handshake Delay");
    delayStackedBarChart.seriesAddElementWithName("TLS Delay");
    delayStackedBarChart.seriesAddElementWithName("Upload Delay");
    delayStackedBarChart.seriesAddElementWithName("TTFB Delay");
    delayStackedBarChart
        .seriesAddElementWithName("Response Transmission Delay");
    ArrayList<HttpRequestInfo> infos = new ArrayList<HttpRequestInfo>();
    for (int i = 0; i < infoArray.size(); ++i) {
      if (!infoArray.get(i).isFailedRequest()) {
        infos.add(infoArray.get(i));
      }
    }
    // -1 means use cache
    // TLS with -2 means that it is not greater than 0
    for (int i = 0; i < infos.size() && i < kMAX_NUM; ++i) {
      HttpRequestInfo info = infos.get(i);
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS z");
      sdf.setTimeZone(TimeZone.getTimeZone("US/Pacific"));
      delayStackedBarChart
          .categoriesAddElement(sdf.format(new Date(info.getStartTime())));
      System.out.println(sdf.format(new Date(info.getStartTime())));
      if (info.isUseDNSCache() == false) {
        delayStackedBarChart.seriesAddData(0, info.getDnsDelay());
      } else {
        delayStackedBarChart.seriesAddData(0, -1L);
      }
      if (info.isUseConnCache() == false) {
        delayStackedBarChart.seriesAddData(1, info.getHandshakeDelay());
        if (info.getTlsDelay() > 0) {
          delayStackedBarChart.seriesAddData(2, info.getTlsDelay());
        } else {
          delayStackedBarChart.seriesAddData(2, -2L);
        }
      } else {
        delayStackedBarChart.seriesAddData(1, -1L);
        delayStackedBarChart.seriesAddData(2, -1L);
      }
      delayStackedBarChart.seriesAddData(3, info.getReqWriteDelay());
      delayStackedBarChart.seriesAddData(4, info.getTTFBDelay());
      delayStackedBarChart.seriesAddData(5, info.getRespTransDelay());
    }
  }

  public void setDelayPieChart(ArrayList<HttpRequestInfo> infoArray) {
    if (infoArray.isEmpty()) {
      System.out.println("Error no element");
      return;
    }
    delayPieChart.series.clear();
    delayPieChart.seriesAddElement();
    // d means "delay", c means "count"
    double dDNS = 0, cDNS = 0;
    double dHandshake = 0, cHandshake = 0;
    double dTLS = 0, cTLS = 0;
    double dReqWrite = 0, cReqWrite = 0;
    // double dServer = 0, cServer = 0;// not shown
    double dTTFB = 0, cTTFB = 0;
    double dRespTrans = 0, cRespTrans = 0;
    for (int i = 0; i < infoArray.size(); ++i) {
      HttpRequestInfo info = infoArray.get(i);
      if (info.isUseDNSCache() == false) {
        dDNS += info.getDnsDelay();
        cDNS++;
      }
      if (info.isUseConnCache() == false) {
        dHandshake += info.getHandshakeDelay();
        cHandshake++;
        dTLS += info.getTlsDelay();
        cTLS++;
      }
      dReqWrite += info.getReqWriteDelay();
      cReqWrite++;
      // dServer += info.getServerDelay();
      // cServer++;
      dTTFB += info.getTTFBDelay();
      cTTFB++;
      dRespTrans += info.getRespTransDelay();
      cRespTrans++;
    }
    double avgDNS = (cDNS == 0 ? 0 : dDNS / cDNS);
    double avgHandshake = (cHandshake == 0 ? 0 : dHandshake / cHandshake);
    double avgTLS = (cTLS == 0 ? 0 : dTLS / cTLS);
    double avgReqWrite = dReqWrite / cReqWrite;
    double avgTTFB = dTTFB / cTTFB;
    double avgRespTrans = dRespTrans / cRespTrans;
    delayPieChart.series.get(0).name = "Percentage of Delays";
    delayPieChart.series.get(0).colorByPoint = true;
    delayPieChart.setSeriesData(0, "DNS Delay", avgDNS);
    delayPieChart.setSeriesData(0, "Handshake Delay", avgHandshake);
    delayPieChart.setSeriesData(0, "TLS Delay", avgTLS);
    delayPieChart.setSeriesData(0, "Require Write Delay", avgReqWrite);
    delayPieChart.setSeriesData(0, "TTFB Delay", avgTTFB);
    delayPieChart.setSeriesData(0, "Response Transmission Delay", avgRespTrans);
    // double sum =
    // avgDNS + avgHandshake + avgTLS + avgReqWrite + avgTTFB + avgRespTrans;
    // delayPieChart.series.get(0).name = "Percentage of Delays";
    // delayPieChart.series.get(0).colorByPoint = true;
    // delayPieChart.setSeriesData(0, "DNS Delay", avgDNS / sum);
    // delayPieChart.setSeriesData(0, "Handshake Delay", avgHandshake / sum);
    // delayPieChart.setSeriesData(0, "TLS Delay", avgTLS / sum);
    // delayPieChart.setSeriesData(0, "Require Write Delay", avgReqWrite / sum);
    // delayPieChart.setSeriesData(0, "TTFB Delay", avgTTFB / sum);
    // delayPieChart.setSeriesData(0, "Response Transmission Delay",
    // avgRespTrans / sum);
  }

  public void setErrorRateChart(ArrayList<HttpRequestInfo> infoArray) {
    if (infoArray.isEmpty()) {
      System.out.println("Error Rate Chart Error, no element");
      return;
    }
    Double error_cnt = 0.0;
    for (int i = 0; i < infoArray.size(); ++i) {
      if (infoArray.get(i).isFailedRequest()) {
        error_cnt++;
      }
    }
    if (!infoArray.isEmpty()) {
      errorRateChart.errorRate = error_cnt / infoArray.size();
    } else {
      System.out.println("Error Rate Chart Error.");
      errorRateChart.errorRate = error_cnt;
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
