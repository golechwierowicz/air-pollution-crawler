package modules.rest.model.gios;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.base.MoreObjects;
import com.google.common.base.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

@Entity
@Table(name="parameter")
public class Parameter {
  @Column(name="p_id")
  private int idParam;
  @Column(name="p_name")
  private String paramName;
  @Column(name="p_formula")
  private String paramFormula;
  @Column(name="p_code")
  private String paramCode;
  @JsonIgnore
  @ManyToMany(mappedBy = "sensor")
  private Set<Sensor> sensors;

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

  public Set<Sensor> getSensors() {
    return sensors;
  }

  public void setSensors(Set<Sensor> sensors) {
    this.sensors = sensors;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Parameter parameter = (Parameter) o;
    return idParam == parameter.idParam &&
        Objects.equal(paramName, parameter.paramName) &&
        Objects.equal(paramFormula, parameter.paramFormula) &&
        Objects.equal(paramCode, parameter.paramCode) &&
        Objects.equal(sensors, parameter.sensors);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(idParam, paramName, paramFormula, paramCode, sensors);
  }

  @Override
  public String toString() {
    return MoreObjects.toStringHelper(this)
        .add("idParam", idParam)
        .add("paramName", paramName)
        .add("paramFormula", paramFormula)
        .add("paramCode", paramCode)
        .add("sensors", sensors)
        .toString();
  }
}
