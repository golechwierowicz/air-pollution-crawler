package modules.rest.model.gios;

import com.google.common.base.MoreObjects;

import java.io.Serializable;

public class Commune implements Serializable {
  private String communeName;
  private String districtName;
  private String provinceName;
  public Commune() {
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("communeName", communeName)
        .add("districtName", districtName)
        .add("provinceName", provinceName)
        .toString();
  }

  public String getCommuneName() {
    return communeName;
  }

  public void setCommuneName(String communeName) {
    this.communeName = communeName;
  }

  public String getDistrictName() {
    return districtName;
  }

  public void setDistrictName(String districtName) {
    this.districtName = districtName;
  }

  public String getProvinceName() {
    return provinceName;
  }

  public void setProvinceName(String provinceName) {
    this.provinceName = provinceName;
  }
}
