package animal.misc;

public interface DebugLogger {
  public static final int INFO = 1;
  public static final int WARN = 2;
  public static final int CONFIG_ERROR = 4;
  public static final int EXCEPTION = 8;
  public static final int ERROR = 16;
  public static final int FATAL_ERROR = 32;

  public void init();

  public DebugLogger getCurrentLogger();
  
  public void errorMessage(String message, int errorLevel);

  public void message(String msg);
  
  public void logMessage(String message, int level);
}
