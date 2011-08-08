package translator;

public class Debug
{
  public static void printMessage(boolean message)
  {
    printMessage(String.valueOf(message));
  }

  public static void printMessage(char message)
  {
    printMessage(String.valueOf(message));
  }

  public static void printMessage(double message)
  {
    printMessage(String.valueOf(message));
  }

  public static void printMessage(float message)
  {
    printMessage(String.valueOf(message));
  }

  public static void printMessage(int message)
  {
    printMessage(String.valueOf(message));
  }

  public static void printMessage(long message)
  {
    printMessage(String.valueOf(message));
  }

  public static void printMessage(Object message)
  {
    printMessage(String.valueOf(message));
  }

  public static void printMessage(int[] message)
  {
    if (message != null && message.length > 0)
      for (int i=0; i<message.length; i++)
        printMessage(String.valueOf(message[i]) +",");
//    printMessage(String.valueOf(message[message.length-1]));
  }

  public static void printMessage(String message)
  {
    System.out.print(message);
  }

  public static void printlnMessage(boolean message)
  {
    printlnMessage(String.valueOf(message));
  }

  public static void printlnMessage(char message)
  {
    printlnMessage(String.valueOf(message));
  }

  public static void printlnMessage(double message)
  {
    printlnMessage(String.valueOf(message));
  }

  public static void printlnMessage(float message)
  {
    printlnMessage(String.valueOf(message));
  }

  public static void printlnMessage(int message)
  {
    printlnMessage(String.valueOf(message));
  }

  public static void printlnMessage(long message)
  {
    printlnMessage(String.valueOf(message));
  }

  public static void printlnMessage(Object message)
  {
    printlnMessage(String.valueOf(message));
  }

  public static void printlnMessage(int[] message)
  {
    if (message != null && message.length > 0)
      for (int i=0; i<message.length; i++)
        printlnMessage(String.valueOf(message[i]) +",");
//    printlnMessage(String.valueOf(message[message.length-1]));
  }

  public static void printlnMessage(String message)
  {
    System.out.println(message);
  }
}
