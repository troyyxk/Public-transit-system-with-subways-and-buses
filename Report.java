import java.io.Serializable;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

/**
 * This the class Report.
 */
public class Report implements Serializable {
  LocalDate dateOfReport;
  String report;
  private int totalTerminals;
  private int totalStops;
  private int totalStations;
  private double totalRevenue;
  private ReportManager reportManager;

  /**
   * This method is the initializer for the method Report.
   *
   * @param reportManager
   * @param date
   * @param totalTerminals
   * @param totalStops
   * @param totalStations
   * @param totalRevenue
   */
  public Report(
      ReportManager reportManager,
      Date date,
      int totalTerminals,
      int totalStops,
      int totalStations,
      double totalRevenue) {
    Calendar cal = Calendar.getInstance();
    cal.setTime(date);
    int year = cal.get(Calendar.YEAR);
    int month = cal.get(Calendar.MONTH) + 1;
    int day = cal.get(Calendar.DAY_OF_MONTH);
    this.dateOfReport = LocalDate.of(year, month, day);
    this.totalTerminals = totalTerminals;
    this.totalStops = totalStops;
    this.totalStations = totalStations;
    this.totalRevenue = totalRevenue;

    this.report = "Report for the date of: " + dateOfReport + System.lineSeparator();
    this.report +=
        "Total revenue: "
            + NumberFormat.getCurrencyInstance().format(this.totalRevenue)
            + System.lineSeparator();
    this.report += "Total terminals reached: " + this.totalTerminals + System.lineSeparator();
    this.report += "Total stops reached: " + this.totalStops + System.lineSeparator();
    this.report += "Total stations reached: " + this.totalStations + System.lineSeparator();

    this.reportManager = reportManager;
    this.reportManager.addReport(this);
  }

  /**
   * This method is used to help print any instance of this method.
   *
   * @return
   */
  @Override
  public String toString() {
    return report;
  }

  /**
   * This method is used to determine whether an object is equals to an instance of this methods.
   * @param obj
   * @return
   */
  @Override
  public boolean equals(Object obj) {
    if (obj instanceof Report) {
      return ((Report) obj).dateOfReport.equals(this.dateOfReport);
    } else {
      return false;
    }
  }
}
