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
import eu.flatworld.worldexplorer.WorldExplorer;
import java.awt.Cursor;
import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;

public class AboutDialog extends javax.swing.JDialog {

    public AboutDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        jLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jImage = new javax.swing.JLabel();
        jName = new javax.swing.JLabel();
        jLink = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("About");
        setResizable(false);

        jImage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/eu/flatworld/worldexplorer/splash.png"))); // NOI18N
        jImage.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        jName.setForeground(new java.awt.Color(102, 102, 102));
        jName.setText(WorldExplorer.NAME + " " + WorldExplorer.VERSION);

        jLink.setFont(new java.awt.Font("Dialog", 0, 10));
        jLink.setForeground(new java.awt.Color(102, 102, 102));
        jLink.setText("www.flatworld.eu");
        jLink.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLinkMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLinkMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLinkMouseExited(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel1.setForeground(new java.awt.Color(102, 102, 102));
        jLabel1.setText("This software has been realeased under the GNU Lesser General Public License verison 2.1");

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel2.setForeground(new java.awt.Color(102, 102, 102));
        jLabel2.setText("Copyright 2007 marcopar@gmail.com");

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 10));
        jLabel3.setForeground(new java.awt.Color(102, 102, 102));
        jLabel3.setText("Many thanks to the developers of the following libraries: Apache Jakarta Commons, GeoTools");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jName)
                    .addComponent(jImage)
                    .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 494, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 226, Short.MAX_VALUE)
                        .addComponent(jLink))
                    .addComponent(jLabel3))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jImage, javax.swing.GroupLayout.PREFERRED_SIZE, 249, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jName)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLink)
                    .addComponent(jLabel2))
                .addContainerGap())
        );

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width-524)/2, (screenSize.height-395)/2, 524, 395);
    }// </editor-fold>//GEN-END:initComponents
    private void jLinkMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLinkMouseExited
// TODO add your handling code here:
    }//GEN-LAST:event_jLinkMouseExited

    private void jLinkMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLinkMouseEntered
    //jLink.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
    }//GEN-LAST:event_jLinkMouseEntered

    private void jLinkMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLinkMouseClicked
        try {
            Desktop.getDesktop().browse(URI.create("http://" + jLink.getText() + "/"));
        } catch (IOException ex) {
            LogX.log(Level.WARNING, "Unable to start browser", ex);
        }
    }//GEN-LAST:event_jLinkMouseClicked

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {

            @Override
            public void run() {
                new AboutDialog(new javax.swing.JFrame(), true).setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jImage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLink;
    private javax.swing.JLabel jName;
    // End of variables declaration//GEN-END:variables
}
