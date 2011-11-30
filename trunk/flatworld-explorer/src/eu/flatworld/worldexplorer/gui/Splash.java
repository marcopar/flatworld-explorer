/*
   Copyright 2011 marcopar@gmail.com

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
*/

package eu.flatworld.worldexplorer.gui;

import eu.flatworld.commons.log.LogX;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.SplashScreen;
import java.util.logging.Level;

public class Splash {

    public static void setMessage(String message) {
        LogX.log(Level.INFO, message);
        SplashScreen ss = SplashScreen.getSplashScreen();
        if (ss != null) {
            Graphics2D g2 = ss.createGraphics();
            Dimension d = ss.getSize();
            //g2.setFont(g2.getFont().deriveFont(Font.BOLD, 9));
            g2.setFont(g2.getFont().deriveFont(9f));
            g2.setComposite(AlphaComposite.Clear);
            g2.fillRect(0, 0, (int) d.getWidth(), (int) d.getHeight());
            g2.setPaintMode();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.DARK_GRAY);
            //int n = ((int)d.getWidth() - g2.getFontMetrics().stringWidth(message)) / 2;
            g2.drawString(message, 293, 242);
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
            ss.update();
        }
    }

    public static void main(String args[]) {
        Splash.setMessage("TEST");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        Splash.setMessage("XXXXXX");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        System.exit(0);
    }
}
