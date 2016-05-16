package me.xcdu.bo;

import java.util.ArrayList;

public class UrlIndex {
  public String domain;
  public ArrayList<String> subUrlList;

  public UrlIndex() {
    subUrlList = new ArrayList<String>();
  }

  public void setDomain(String domain) {
    this.domain = domain;
  }

  public void addSubList(String subUrl) {
    subUrlList.add(subUrl);
  }

  @Override
  public String toString() {
    return String.format("DOMAIN:%s\n" + "SUB_LIST:%s\n", domain,
        subUrlList.toString());
  }
}
