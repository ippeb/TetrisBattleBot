import java.awt.*;
import java.lang.Math;
import java.awt.image.BufferedImage;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

public class test {
    public static void main(String[] args) throws java.awt.AWTException {
	Robot robot = new Robot();
	robot.delay(2000);
	Runtime rt = Runtime.getRuntime();
	try {
	    rt.exec("./ctrl");
	    robot.delay(150);
	    rt.exec("./ctrl");
	    robot.delay(150);
	    rt.exec("./ctrl");
	    robot.delay(150);
	    rt.exec("./ctrl");
	    robot.delay(150);
	} catch (Throwable trw)
	    {
		trw.printStackTrace();
	    }
    }
}