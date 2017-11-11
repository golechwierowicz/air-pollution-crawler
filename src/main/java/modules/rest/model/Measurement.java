package modules.rest.model;

import modules.rest.model.gios.Value;

public class Measurement {
  public int id; // sensor id
  public String measurementName;
  public Value[] values;
  public String[] attributes;
}
