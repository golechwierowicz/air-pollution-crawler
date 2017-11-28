package modules.common.streams;

import com.google.common.base.Charsets;
import modules.common.dao.HibernateSessionFactory;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.StatelessSession;
import org.hibernate.query.NativeQuery;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.OutputStream;

public class ExportMeasurementsStreamWriter implements StreamingOutput {
  private final HibernateSessionFactory hibernateSessionFactory;

  public ExportMeasurementsStreamWriter(HibernateSessionFactory hibernateSessionFactory) {
    this.hibernateSessionFactory = hibernateSessionFactory;
  }

  @Override
  public void write(OutputStream outputStream) throws IOException, WebApplicationException {
    final String sql = "select m_id, m_timestamp, m_value, sensor_id, s_name, locpoint_id," +
        " lp_latitude, lp_longitude, lp_station_name, city_id," +
        " c_name from measurement msr join sensor se on se.s_id = msr.sensor_id" +
        " join location_point as lp on lp.lp_id =" +
        " se.locpoint_id join city as ci on ci.c_id = lp.city_id";
    try(StatelessSession session = hibernateSessionFactory.getInstance().openStatelessSession()) {
      final NativeQuery query = session.createNativeQuery(sql).setFetchSize(1000).setReadOnly(true);
      final ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
      while (results.next()) {
        final Object[] objects = results.get();
        final String formatted = String.join(",",
            safeString(objects[0]),
            safeString(objects[1]),
            safeString(objects[2]),
            safeString(objects[3]),
            safeString(objects[4]),
            safeString(objects[5]),
            safeString(objects[6]),
            safeString(objects[7]),
            safeString(objects[8]),
            safeString(objects[9]),
            safeString(objects[10])) + "\n";
        outputStream.write(formatted.getBytes(Charsets.UTF_8));
      }
    } finally {
      outputStream.close();
    }
  }

  private String safeString(Object object) {
    return object != null ? object.toString() : "null";
  }
}
