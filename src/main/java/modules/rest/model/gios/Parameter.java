package modules.rest.model.gios;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Parameter {
  @JsonIgnore
  int idParam;
  private String paramName;
  private String paramFormula;
  private String paramCode;

  public Parameter() {
  }

  public String getParamName() {
    return paramName;
  }

  public void setParamName(String paramName) {
    this.paramName = paramName;
  }

  public String getParamFormula() {
    return paramFormula;
  }

  public void setParamFormula(String paramFormula) {
    this.paramFormula = paramFormula;
  }

  public String getParamCode() {
    return paramCode;
  }

  public void setParamCode(String paramCode) {
    this.paramCode = paramCode;
  }

  public int getIdParam() {
    return idParam;
  }

  public void setIdParam(int idParam) {
    this.idParam = idParam;
  }
}
