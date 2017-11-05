module air.pollution.crawler {
  requires java.base;
  requires config;
  requires java.ws.rs;
  requires slf4j.api;
  requires grizzly.http.server;
  requires jersey.server;
  requires jersey.container.grizzly2.http;
  requires java.logging;
  requires com.google.common;
  requires scala.library;
  requires commons.validator;
  requires jsoup;
  requires java.xml;
  requires htmlcleaner;
  requires jackson.annotations;
  requires joda.time;
  requires jackson.core;
  requires jackson.databind;
  requires jackson.datatype.joda;
}