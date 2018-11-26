package pt.ua.concurrent;

import static java.lang.System.*;

/**
 * System output related static utilities (supporting colors).
 *
 * <P>This class follows DbC(tm) methodology
 * (<a href="http://en.wikipedia.org/wiki/Design_by_contract">Wikipedia</a>).
 * Where possible, contracts are implement with native's {@code Java} assert.
 *
 * @author Miguel Oliveira e Silva (mos@ua.pt)
 * @version 0.5, October 2013
 */
public class Console
{
   public static final String BOLD="1";
   public static final String FAINT="2";
   public static final String UNDERLINE="4";
   public static final String CROSSED_OUT="9";
   public static final String BLACK="30";
   public static final String RED="31";
   public static final String GREEN="32";
   public static final String YELLOW="33";
   public static final String BLUE="34";
   public static final String MAGENTA="35";
   public static final String CYAN="36";
   public static final String WHITE="37";
   public static final String BACKGROUND_BLACK="40";
   public static final String BACKGROUND_RED="41";
   public static final String BACKGROUND_GREEN="42";
   public static final String BACKGROUND_YELLOW="43";
   public static final String BACKGROUND_BLUE="44";
   public static final String BACKGROUND_MAGENTA="45";
   public static final String BACKGROUND_CYAN="46";
   public static final String BACKGROUND_WHITE="47";
   public static final String BRIGHT_BLACK="90";
   public static final String BRIGHT_RED="91";
   public static final String BRIGHT_GREEN="92";
   public static final String BRIGHT_YELLOW="93";
   public static final String BRIGHT_BLUE="94";
   public static final String BRIGHT_MAGENTA="95";
   public static final String BRIGHT_CYAN="96";
   public static final String BRIGHT_WHITE="97";
   public static final String BACKGROUND_BRIGHT_BLACK="100";
   public static final String BACKGROUND_BRIGHT_RED="101";
   public static final String BACKGROUND_BRIGHT_GREEN="102";
   public static final String BACKGROUND_BRIGHT_YELLOW="103";
   public static final String BACKGROUND_BRIGHT_BLUE="104";
   public static final String BACKGROUND_BRIGHT_MAGENTA="105";
   public static final String BACKGROUND_BRIGHT_CYAN="106";
   public static final String BACKGROUND_BRIGHT_WHITE="107";


   public static final String[][] colors = {

      {BLACK}, {RED}, {GREEN}, {YELLOW}, {BLUE}, {MAGENTA}, {CYAN},
      {BOLD,BLACK}, {BOLD,RED}, {BOLD,GREEN}, {BOLD,YELLOW}, {BOLD,BLUE}, {BOLD,MAGENTA}, {BOLD,CYAN},
      {FAINT,BLACK}, {FAINT,RED}, {FAINT,GREEN}, {FAINT,YELLOW}, {FAINT,BLUE}, {FAINT,MAGENTA}, {FAINT,CYAN},
      {BACKGROUND_BLACK, WHITE}, {BACKGROUND_RED}, {BACKGROUND_GREEN}, {BACKGROUND_YELLOW}, {BACKGROUND_BLUE}, {BACKGROUND_MAGENTA}, {BACKGROUND_CYAN},
      {BRIGHT_BLACK}, {BRIGHT_RED}, {BRIGHT_GREEN}, {BRIGHT_YELLOW}, {BRIGHT_BLUE}, {BRIGHT_MAGENTA}, {BRIGHT_CYAN},
      {BACKGROUND_BRIGHT_BLACK, WHITE}, {BACKGROUND_BRIGHT_RED}, {BACKGROUND_BRIGHT_GREEN}, {BACKGROUND_BRIGHT_YELLOW}, {BACKGROUND_BRIGHT_BLUE}, {BACKGROUND_BRIGHT_MAGENTA}, {BACKGROUND_BRIGHT_CYAN},

      {UNDERLINE,BLACK}, {UNDERLINE,RED}, {UNDERLINE,GREEN}, {UNDERLINE,YELLOW}, {UNDERLINE,BLUE}, {UNDERLINE,MAGENTA}, {UNDERLINE,CYAN},
      {UNDERLINE,BOLD,BLACK}, {UNDERLINE,BOLD,RED}, {UNDERLINE,BOLD,GREEN}, {UNDERLINE,BOLD,YELLOW}, {UNDERLINE,BOLD,BLUE}, {UNDERLINE,BOLD,MAGENTA}, {UNDERLINE,BOLD,CYAN},
      {UNDERLINE,FAINT,BLACK}, {UNDERLINE,FAINT,RED}, {UNDERLINE,FAINT,GREEN}, {UNDERLINE,FAINT,YELLOW}, {UNDERLINE,FAINT,BLUE}, {UNDERLINE,FAINT,MAGENTA}, {UNDERLINE,FAINT,CYAN},
      {UNDERLINE,BACKGROUND_BLACK, WHITE}, {UNDERLINE,BACKGROUND_RED}, {UNDERLINE,BACKGROUND_GREEN}, {UNDERLINE,BACKGROUND_YELLOW}, {UNDERLINE,BACKGROUND_BLUE}, {UNDERLINE,BACKGROUND_MAGENTA}, {UNDERLINE,BACKGROUND_CYAN},
      {UNDERLINE,BRIGHT_BLACK}, {UNDERLINE,BRIGHT_RED}, {UNDERLINE,BRIGHT_GREEN}, {UNDERLINE,BRIGHT_YELLOW}, {UNDERLINE,BRIGHT_BLUE}, {UNDERLINE,BRIGHT_MAGENTA}, {UNDERLINE,BRIGHT_CYAN},
      {UNDERLINE,BACKGROUND_BRIGHT_BLACK, WHITE}, {UNDERLINE,BACKGROUND_BRIGHT_RED}, {UNDERLINE,BACKGROUND_BRIGHT_GREEN}, {UNDERLINE,BACKGROUND_BRIGHT_YELLOW}, {UNDERLINE,BACKGROUND_BRIGHT_BLUE}, {UNDERLINE,BACKGROUND_BRIGHT_MAGENTA}, {UNDERLINE,BACKGROUND_BRIGHT_CYAN},

      {CROSSED_OUT,BLACK}, {CROSSED_OUT,RED}, {CROSSED_OUT,GREEN}, {CROSSED_OUT,YELLOW}, {CROSSED_OUT,BLUE}, {CROSSED_OUT,MAGENTA}, {CROSSED_OUT,CYAN},
      {CROSSED_OUT,BOLD,BLACK}, {CROSSED_OUT,BOLD,RED}, {CROSSED_OUT,BOLD,GREEN}, {CROSSED_OUT,BOLD,YELLOW}, {CROSSED_OUT,BOLD,BLUE}, {CROSSED_OUT,BOLD,MAGENTA}, {CROSSED_OUT,BOLD,CYAN},
      {CROSSED_OUT,FAINT,BLACK}, {CROSSED_OUT,FAINT,RED}, {CROSSED_OUT,FAINT,GREEN}, {CROSSED_OUT,FAINT,YELLOW}, {CROSSED_OUT,FAINT,BLUE}, {CROSSED_OUT,FAINT,MAGENTA}, {CROSSED_OUT,FAINT,CYAN},
      {CROSSED_OUT,BACKGROUND_BLACK, WHITE}, {CROSSED_OUT,BACKGROUND_RED}, {CROSSED_OUT,BACKGROUND_GREEN}, {CROSSED_OUT,BACKGROUND_YELLOW}, {CROSSED_OUT,BACKGROUND_BLUE}, {CROSSED_OUT,BACKGROUND_MAGENTA}, {CROSSED_OUT,BACKGROUND_CYAN},
      {CROSSED_OUT,BRIGHT_BLACK}, {CROSSED_OUT,BRIGHT_RED}, {CROSSED_OUT,BRIGHT_GREEN}, {CROSSED_OUT,BRIGHT_YELLOW}, {CROSSED_OUT,BRIGHT_BLUE}, {CROSSED_OUT,BRIGHT_MAGENTA}, {CROSSED_OUT,BRIGHT_CYAN},
      {CROSSED_OUT,BACKGROUND_BRIGHT_BLACK, WHITE}, {CROSSED_OUT,BACKGROUND_BRIGHT_RED}, {CROSSED_OUT,BACKGROUND_BRIGHT_GREEN}, {CROSSED_OUT,BACKGROUND_BRIGHT_YELLOW}, {CROSSED_OUT,BACKGROUND_BRIGHT_BLUE}, {CROSSED_OUT,BACKGROUND_BRIGHT_MAGENTA}, {CROSSED_OUT,BACKGROUND_BRIGHT_CYAN},

      {UNDERLINE,CROSSED_OUT,BLACK}, {UNDERLINE,CROSSED_OUT,RED}, {UNDERLINE,CROSSED_OUT,GREEN}, {UNDERLINE,CROSSED_OUT,YELLOW}, {UNDERLINE,CROSSED_OUT,BLUE}, {UNDERLINE,CROSSED_OUT,MAGENTA}, {UNDERLINE,CROSSED_OUT,CYAN},
      {UNDERLINE,CROSSED_OUT,BOLD,BLACK}, {UNDERLINE,CROSSED_OUT,BOLD,RED}, {UNDERLINE,CROSSED_OUT,BOLD,GREEN}, {UNDERLINE,CROSSED_OUT,BOLD,YELLOW}, {UNDERLINE,CROSSED_OUT,BOLD,BLUE}, {UNDERLINE,CROSSED_OUT,BOLD,MAGENTA}, {UNDERLINE,CROSSED_OUT,BOLD,CYAN},
      {UNDERLINE,CROSSED_OUT,FAINT,BLACK}, {UNDERLINE,CROSSED_OUT,FAINT,RED}, {UNDERLINE,CROSSED_OUT,FAINT,GREEN}, {UNDERLINE,CROSSED_OUT,FAINT,YELLOW}, {UNDERLINE,CROSSED_OUT,FAINT,BLUE}, {UNDERLINE,CROSSED_OUT,FAINT,MAGENTA}, {UNDERLINE,CROSSED_OUT,FAINT,CYAN},
      {UNDERLINE,CROSSED_OUT,BACKGROUND_BLACK, WHITE}, {UNDERLINE,CROSSED_OUT,BACKGROUND_RED}, {UNDERLINE,CROSSED_OUT,BACKGROUND_GREEN}, {UNDERLINE,CROSSED_OUT,BACKGROUND_YELLOW}, {UNDERLINE,CROSSED_OUT,BACKGROUND_BLUE}, {UNDERLINE,CROSSED_OUT,BACKGROUND_MAGENTA}, {UNDERLINE,CROSSED_OUT,BACKGROUND_CYAN},
      {UNDERLINE,CROSSED_OUT,BRIGHT_BLACK}, {UNDERLINE,CROSSED_OUT,BRIGHT_RED}, {UNDERLINE,CROSSED_OUT,BRIGHT_GREEN}, {UNDERLINE,CROSSED_OUT,BRIGHT_YELLOW}, {UNDERLINE,CROSSED_OUT,BRIGHT_BLUE}, {UNDERLINE,CROSSED_OUT,BRIGHT_MAGENTA}, {UNDERLINE,CROSSED_OUT,BRIGHT_CYAN},
      {UNDERLINE,CROSSED_OUT,BACKGROUND_BRIGHT_BLACK, WHITE}, {UNDERLINE,CROSSED_OUT,BACKGROUND_BRIGHT_RED}, {UNDERLINE,CROSSED_OUT,BACKGROUND_BRIGHT_GREEN}, {UNDERLINE,CROSSED_OUT,BACKGROUND_BRIGHT_YELLOW}, {UNDERLINE,CROSSED_OUT,BACKGROUND_BRIGHT_BLUE}, {UNDERLINE,CROSSED_OUT,BACKGROUND_BRIGHT_MAGENTA}, {UNDERLINE,CROSSED_OUT,BACKGROUND_BRIGHT_CYAN},

   };

   public static void setColor(String color)
   {
      out.print("\u001B["+color+"m");
   }

   public static void resetColor()
   {
      out.print("\u001B[0m");
   }

   public static void print(String text)
   {
      out.print(text);
   }

   public static void println(String text)
   {
      print(text+"\n");
   }

   public static void print(String color, String text)
   {
      out.print("\u001B["+color+"m"+text+"\u001B[0m");
   }

   public static void println(String color, String text)
   {
      out.println("\u001B["+color+"m"+text+"\u001B[0m");
   }

   public static void print(String[] colors, String text)
   {
      assert colors != null;

      for(int i = 0; i < colors.length; i++)
         out.print("\u001B["+colors[i]+"m");
      out.print(text+"\u001B[0m");
   }

   public static void println(String[] colors, String text)
   {
      assert colors != null;

      for(int i = 0; i < colors.length; i++)
         out.print("\u001B["+colors[i]+"m");
      out.println(text+"\u001B[0m");
   }
}
