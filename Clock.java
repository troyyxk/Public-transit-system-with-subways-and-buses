import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/** This is the Clock class and it is responsible for control the time in this program. */
class Clock {
  private Timer timer = new Timer();
  private long time;
  private long rate = 1000;
  private TimerTask updateTime;
  private ArrayList<ClockObserver> listeners = new ArrayList<>();

  /** Start the already established timing system. */
  public Clock() {
    time = System.currentTimeMillis();
    setUpdateTimeTask();
    timer.schedule(updateTime, 0, 1000);
  }

  public void addObserver(ClockObserver clockObserver) {
    listeners.add(clockObserver);
  }

  /** Set up the timing system for this program. */
  private void setUpdateTimeTask() {
    updateTime =
        new TimerTask() {
          public void run() {
            time += 1000;
            for (ClockObserver listener : listeners) {
              listener.updateEverySec();
            }
          }
        };
  }

  /** Slow down the Timing System. */
  void slowDown() {
    if (rate >= 8000) {
      System.out.println("Cannot slow down anymore!");
    } else {
      updateTime.cancel();
      setUpdateTimeTask();
      rate = rate * 2;
      timer.schedule(updateTime, 0, rate);
    }
  }

  void defaultSpeed() {
    this.rate = 1000;
  }

  /** Speed up the Timing System. */
  void speedUp() {
    if (rate <= 50) {
      System.out.println("Cannot speed up anymore!");
    } else {
      updateTime.cancel();
      setUpdateTimeTask();
      rate = rate / 2;
      timer.schedule(updateTime, 0, rate);
    }
  }

  void progressDays(int toIncrease) {
    this.time += toIncrease * 86400000;
  }

  void progressHours(int toIncrease) {
    this.time += toIncrease * 3600000;
  }

  void progressMinutes(int toIncrease) {
    this.time += toIncrease * 60000;
  }

  /**
   * Get the time in the program. Add
   *
   * @return time
   */
  Date getDate() {
    return new Date(time);
  }
}
