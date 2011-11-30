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

package eu.flatworld.worldexplorer;

import eu.flatworld.commons.log.LogX;
import eu.flatworld.worldexplorer.gui.MainFrame;
import java.io.File;
import java.util.Set;
import java.util.logging.Level;

public class Main {

    public static void main(String args[]) {
        try {
            File f = new File(WorldExplorer.WORK_DIR);
            if (!f.exists()) {
                if (!f.mkdirs()) {
                    throw new Exception("Error creating directory: " + f.getAbsolutePath());
                }
            }
            LogX.configureLog(WorldExplorer.LOG_FILE, true, true, Level.ALL);
        } catch (Throwable ex) {
            LogX.log(Level.SEVERE, "FATAL ERROR", ex, true);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    Set<Object> props = System.getProperties().keySet();
                    for (Object object : props) {
                        String sp = (String) object;
                        LogX.log(Level.CONFIG, sp + "=" + System.getProperty(sp));
                    }
                    for (String elem : System.getenv().keySet()) {
                        LogX.log(Level.CONFIG, elem + "=" + System.getenv().get(elem));
                    }
                    new MainFrame().setVisible(true);
                } catch (Throwable ex) {
                    LogX.log(Level.SEVERE, "FATAL ERROR", ex, true);
                }
            }
        });
    }
}
