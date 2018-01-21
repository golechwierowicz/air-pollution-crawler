package modules.rest.model.gios;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.joda.time.DateTime;

public class AirQualityIndex {
  public int id;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime stCalcDate;
  public IndexLevel stIndexLevel;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime stSourceDataDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime so2CalcDate;
  public IndexLevel so2IndexLevel;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime so2SourceDataDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime no2CalcDate;
  public IndexLevel no2IndexLevel;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime no2SourceDataDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime coCalcDate;
  public IndexLevel coIndexLevel;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime coSourceDataDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime pm10CalcDate;
  public IndexLevel pm10IndexLevel;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime pm10SourceDataDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime pm25CalcDate;
  public IndexLevel pm25IndexLevel;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime pm25SourceDataDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime o3CalcDate;
  public IndexLevel o3IndexLevel;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime o3SourceDataDate;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime c6h6CalcDate;
  public IndexLevel c6h6IndexLevel;
  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", shape = JsonFormat.Shape.STRING)
  public DateTime c6h6SourceDataDate;
  public Boolean stIndexStatus;
  public String stIndexCrParam;

  public AirQualityIndex() {
  }
}
