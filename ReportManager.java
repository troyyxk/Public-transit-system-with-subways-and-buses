import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * This is the class ReportManager.
 */
public class ReportManager implements Serializable {
  StatisticsTracker statisticsTracker;
  ArrayList<Report> collectionOfReports;

  /**
   * This method is the initializer for the class ReportManager.
   *
   * @param statisticsTracker
   */
  public ReportManager(StatisticsTracker statisticsTracker) {
    this.statisticsTracker = statisticsTracker;
    collectionOfReports = new ArrayList<>();
  }

  /**
   * This method is used to add report.
   *
   * @param report
   */
  public void addReport(Report report) {
    if (collectionOfReports.contains(report)) {
      for (int i = 0; i < collectionOfReports.size(); i++) {
        if (collectionOfReports.get(i).equals(report)) {
          collectionOfReports.set(i, report);
        }
      }
    } else {
      collectionOfReports.add(report);
    }
  }

  /**
   * This method is used to get report on date.
   *
   * @param date
   * @return
   */
  public Report getReportOnDate(LocalDate date) {
    for (Report collectionOfReport : collectionOfReports) {
      if (collectionOfReport.dateOfReport.equals(date)) {
        return collectionOfReport;
      }
    }
    return null;
  }
}
