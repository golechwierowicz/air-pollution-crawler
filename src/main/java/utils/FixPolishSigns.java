package utils;

public class FixPolishSigns {
  private FixPolishSigns() {
  }

  public static String fix(String input) {
    return input.replaceAll("&oacute;", "ó")
        .replaceAll("&quot;", "\"");
  }
}