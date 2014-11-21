import javax.swing.*;

import java.io.FileOutputStream;
import java.io.IOException;

import com.itextpdf.text.BadElementException;
// com.lowagie... old version
// com.itextpdf... recent version
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.Image;

import java.awt.event.*;
import java.awt.*;
import java.io.File;
import java.net.MalformedURLException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class bihurtzailea extends JPanel implements ActionListener {

  private static final long serialVersionUID = 1L;
  private static final String espresio_erregularra = "([^\\s]+(\\.(?i)(jpg|png|gif|bmp))$)";
  JButton go;
  private Pattern pattern;
  private Matcher matcher;
  JFileChooser chooser;
  String choosertitle;

  public bihurtzailea() {
    go = new JButton("Bihurtu");
    go.addActionListener(this);
    add(go);
  }

  public void actionPerformed(ActionEvent e) {
    pattern = Pattern.compile(espresio_erregularra);

    chooser = new JFileChooser();
    chooser.setCurrentDirectory(new java.io.File("."));
    chooser.setDialogTitle(choosertitle);
    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    chooser.setAcceptAllFileFilterUsed(false);
    if (chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
      File aukera = chooser.getSelectedFile();
      String bidea = aukera.getAbsolutePath();
System.out.println(aukera.getName());
      String output = bidea + "/" +aukera.getName()+".pdf";
      File irakurri = new File(output);
      if (irakurri.exists()) {
        int i = baiezMezua("PDF Fitxategia dagoeneko existitzen da. Hau berridatzi nahi al duzu?");
        if (i == 0) {
          irakurri.delete();
        }

      }
      if (!irakurri.exists()) {
        File[] listOfFiles = aukera.listFiles();
        Arrays.sort(listOfFiles);
        Image m;
        com.itextpdf.text.Rectangle tamaina = new Rectangle(1, 1);
        int zabalera;
        int altuera;
        boolean eg = true;
        int k = 0;
        try {
          while (eg) {
            if (irudia_da(listOfFiles[k].getName())) {
              m = Image.getInstance(listOfFiles[k].getAbsolutePath());
              zabalera = Math.round(m.getScaledWidth());
              altuera = Math.round(m.getScaledHeight());
              tamaina = new com.itextpdf.text.Rectangle(zabalera, altuera);
              eg = false;
            } else {
              k++;
            }
          }
        } catch (BadElementException e2) {
          e2.printStackTrace();
        } catch (MalformedURLException e2) {
          e2.printStackTrace();
        } catch (IOException e2) {
          e2.printStackTrace();
        }

        Document document = new Document(tamaina);
        document.setMargins(0, 0, 0, 0);

        PdfWriter writer = new PdfWriter() {};
        try {
          FileOutputStream fos = new FileOutputStream(output);
          writer = PdfWriter.getInstance(document, fos);
          writer.open();
          document.open();
          document.setMargins(0, 0, 0, 0);
          document.add(Image.getInstance(listOfFiles[k].getAbsolutePath()));
          document.setMargins(0, 0, 0, 0);

        } catch (BadElementException e2) {
          e2.printStackTrace();
        } catch (MalformedURLException e2) {
          e2.printStackTrace();
        } catch (DocumentException e2) {
          e2.printStackTrace();
        } catch (IOException e2) {
          e2.printStackTrace();
        }

        document.setMargins(0, 0, 0, 0);

        try {


          for (int i = k+1; i < listOfFiles.length; i++) {
            if (irudia_da(listOfFiles[i].getName())) {
              System.out.println(listOfFiles[i].getAbsolutePath());
              String borratu = (listOfFiles[i].getName());
              System.out.println(borratu);
              m = Image.getInstance(listOfFiles[i].getAbsolutePath());
              document.add(m);
              System.out.println("Zabalera: " + m.getScaledWidth());
              System.out.println("Altuera: " + m.getScaledHeight());
              zabalera = Math.round(m.getScaledWidth());
              altuera = Math.round(m.getScaledHeight());
              tamaina = new com.itextpdf.text.Rectangle(zabalera, altuera);
              m.scaleToFit(tamaina);
              document.setPageSize(tamaina);
              document.setMargins(0, 0, 0, 0);
              document.setMargins(0, 0, 0, 0);

              document.setPageSize(tamaina);
              System.out.println(document.getPageSize());
              System.out.println(document.getPageSize());

            }
          }
          document.close();
          writer.close();
          JOptionPane.showMessageDialog(null, "PDF fitxategia zuzen sortu da");

        } catch (Exception e1) {
          e1.printStackTrace();
        }
      }
    } else {
      System.out.println("No Selection ");
    }
  }

  public Dimension getPreferredSize() {
    return new Dimension(200, 200);
  }

  public static void main(String s[]) {
    JFrame frame = new JFrame("");
    bihurtzailea panel = new bihurtzailea();
    frame.addWindowListener(new WindowAdapter() {
      public void windowClosing(WindowEvent e) {
        System.exit(0);
      }
    });
    frame.getContentPane().add(panel, "Center");
    frame.setSize(panel.getPreferredSize());
    frame.setVisible(true);
  }

  private static int baiezMezua(String mezua) {
    int result =
        JOptionPane.showConfirmDialog((Component) null, mezua, "", JOptionPane.OK_CANCEL_OPTION);
    return result;
  }

  public boolean irudia_da(final String image) {

    matcher = pattern.matcher(image);
    return matcher.matches();

  }
}
