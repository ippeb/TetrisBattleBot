// Simple program used for analyzing the implemented
// stategy (without any Web interaction)

package TetrisBattleBot;

import java.util.*;
import java.io.*;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public class ExecSample { 
    public static void main(String[] args) {
	try {
	    Runtime rt = Runtime.getRuntime();
	    Process proc = rt.exec(" ./TetrisBattleBot/MouseTools/MouseTools -leftClick");
	    rt.exec("osascript TetrisBattleBot/AppleScript/right.scpt");
	  
	    InputStream stderr = proc.getErrorStream();
	    InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            System.out.println("<ERROR>");
	    while ( (line = br.readLine()) != null)
                System.out.println(line);
            System.out.println("</ERROR>");
	    /* // INPUT
	       InputStream stdin = proc.getInputStream();
	       isr = new InputStreamReader(stdin);
	       br = new BufferedReader(isr);
	       line = null;
	       System.out.println("<INPUT>");
	       while ( (line = br.readLine()) != null)
	       System.out.println(line);
	       System.out.println("</INPUT>");
	    */

            int exitVal = proc.waitFor();
            System.out.println("Process exitValue: " + exitVal);
    } catch (Throwable t)
	    {
		t.printStackTrace();
	    }
    }
}