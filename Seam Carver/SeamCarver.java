/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package seamcarver;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

/**
 *
 * @author Khewbs
 */
public class SeamCarver {

    /**
     * @param args the command line arguments
     */
    
    private Picture p;
    private int width;
    private int height;
    private double[][] e;
    
    public SeamCarver(Picture p){
        if(p==null){
            throw new NullPointerException("Picture was null");
        }
        this.p = p;
        this.width = p.width()-1;
        this.height = p.height()-1;
        e = new double[p.height()][p.width()];
        for(int i = 0; i<p.height(); i++){
            for(int j = 0; j<p.width(); j++){
                e[i][j] = energy(j, i);
            }
        }
    }
    public int width(){
        return width;
    }
    public int height(){
        return height;
    }
    public Picture picture(){
        return p;
    }
    public double energy(int x, int y){
        double total = 0;
        double Rx, Gx, Bx, Ry, Gy, By;
        double x_e, y_e;
        if(x<0 || x>width){
            throw new IndexOutOfBoundsException("Invalid column value = "+x);
        }
        if(y<0 || y>height){
            throw new IndexOutOfBoundsException("Invalid row value = "+y);
        }
        if(x==0){
            Rx = Math.pow((p.get(x+1, y).getRed()-p.get(width(), y).getRed()), 2);
            Gx = Math.pow((p.get(x+1, y).getGreen()-p.get(width(), y).getGreen()), 2);
            Bx = Math.pow((p.get(x+1, y).getBlue()-p.get(width(), y).getBlue()), 2);
            x_e = Rx + Gx + Bx;
        }
        else if(x==width){
            Rx = Math.pow((p.get(0, y).getRed()-p.get(x-1, y).getRed()), 2);
            Gx = Math.pow((p.get(0, y).getGreen()-p.get(x-1, y).getGreen()), 2);
            Bx = Math.pow((p.get(0, y).getBlue()-p.get(x-1, y).getBlue()), 2);
            x_e = Rx + Gx + Bx;
        }
        else{
            Rx = Math.pow((p.get(x+1, y).getRed()-p.get(x-1, y).getRed()), 2);
            Gx = Math.pow((p.get(x+1, y).getGreen()-p.get(x-1, y).getGreen()), 2);
            Bx = Math.pow((p.get(x+1, y).getBlue()-p.get(x-1, y).getBlue()), 2);
            x_e = Rx + Gx + Bx;
        }
        if(y==0){
            Ry = Math.pow((p.get(x, y+1).getRed()-p.get(x, height()).getRed()), 2);
            Gy = Math.pow((p.get(x, y+1).getGreen()-p.get(x, height()).getGreen()), 2);
            By = Math.pow((p.get(x, y+1).getBlue()-p.get(x, height()).getBlue()), 2);
            y_e = Ry + Gy + By;
        }
        else if(y==height()){
            Ry = Math.pow((p.get(x, y-1).getRed()-p.get(x, 0).getRed()), 2);
            Gy = Math.pow((p.get(x, y-1).getGreen()-p.get(x, 0).getGreen()), 2);
            By = Math.pow((p.get(x, y-1).getBlue()-p.get(x, 0).getBlue()), 2);
            y_e = Ry + Gy + By;
        }
        else{
            Ry = Math.pow((p.get(x, y-1).getRed()-p.get(x, y+1).getRed()), 2);
            Gy = Math.pow((p.get(x, y-1).getGreen()-p.get(x, y+1).getGreen()), 2);
            By = Math.pow((p.get(x, y-1).getBlue()-p.get(x, y+1).getBlue()), 2);
            y_e = Ry + Gy + By;
        }
        total = Math.sqrt(x_e+y_e);
        return total;
    }
    public int[] findVerticalSeam(){
        double[][] sum = new double[e.length][e[0].length];
        for(int i = sum.length-1; i>=0; i--){
            for(int j = 0; j<e[i].length; j++){
                if(i==sum.length-1){
                    sum[i][j] = e[i][j];
                }
                else if(j==0){
                    if(sum[i+1][j]<sum[i+1][j+1]){
                        sum[i][j] = e[i][j] + sum[i+1][j];
                    }
                    else{
                        sum[i][j] = e[i][j] + sum[i+1][j+1];
                    }
                }
                else if(j==e[0].length-1){
                    if(sum[i+1][j]<sum[i+1][j-1]){
                        sum[i][j] = e[i][j] + sum[i+1][j];
                    }
                    else{
                        sum[i][j] = e[i][j] + sum[i+1][j-1];
                    }
                }
                else{
                    if(sum[i+1][j-1]<sum[i+1][j] && sum[i+1][j-1]<sum[i+1][j+1]){
                        sum[i][j] = e[i][j] + sum[i+1][j-1];
                    }
                    else if(sum[i+1][j]<sum[i+1][j-1] && sum[i+1][j]<sum[i+1][j+1]){
                        sum[i][j] = e[i][j] + sum[i+1][j];
                    }
                    else{
                        sum[i][j] = e[i][j] + sum[i+1][j+1];
                    }
                }
            }
        }
        int j = 0;
        for(int i = 0; i<sum[0].length; i++){
            if(sum[0][i]<sum[0][j]){
                j = i;
            }
        }
        int[] seam = new int[sum.length];
        int count = 0;
        seam[count++] = j; boolean lflag = false; boolean rflag = false;
        for(int i = 0; i<sum.length-1; i++){
            if(j==0){
                lflag = true;
                if(sum[i+1][j]<sum[i+1][j+1]){
                    seam[count++] = j;
                }
                else{
                    j = j+1;
                    seam[count++] = j;
                }
            }
            else if(j==sum[0].length-1){
                rflag = true;
                if(sum[i+1][j]<sum[i+1][j-1]){
                    seam[count++] = j;
                }
                else{
                    j = j-1;
                    seam[count++] = j;
                }
            }
            else{
                if(j-1==0 && rflag){
                    if(sum[i+1][j]<sum[i+1][j+1]){
                        seam[count++] = j;
                    }
                    else{
                        j = j+1;
                        seam[count++] = j;
                    }
                }
                else if(j+1==sum[0].length-1 && lflag){
                    if(sum[i+1][j]<sum[i+1][j-1]){
                        seam[count++] = j;
                    }
                    else{
                        j = j-1;
                        seam[count++] = j;
                    }
                }
                else{
                    if(sum[i+1][j-1]<sum[i+1][j] && sum[i+1][j-1]<sum[i+1][j+1]){
                        j = j-1;
                        seam[count++] = j;
                    }
                    else if(sum[i+1][j]<sum[i+1][j-1] && sum[i+1][j]<sum[i+1][j+1]){
                        seam[count++] = j;
                    }
                    else{
                        j = j+1;
                        seam[count++] = j;
                    }
                }
            }
        }
        return seam;
    }
    public int[] findHorizontalSeam(){
        double[][] sum = new double[e.length][e[0].length];
        for(int j = sum[0].length-1; j>=0; j--){
            for(int i = 0; i<sum.length; i++){
                if(j==sum[0].length-1){
                    sum[i][j] = e[i][j];
                }
                else if(i==0){
                    if(sum[i][j+1]<sum[i+1][j+1]){
                        sum[i][j] = e[i][j] + sum[i][j+1];
                    }
                    else{
                        sum[i][j] = e[i][j] + sum[i+1][j+1];
                    }
                }
                else if(i==e.length-1){
                    if(sum[i][j+1]<sum[i-1][j+1]){
                        sum[i][j] = e[i][j] + sum[i][j+1];
                    }
                    else{
                        sum[i][j] = e[i][j] + sum[i-1][j+1];
                    }
                }
                else{
                    if(sum[i-1][j+1]<sum[i][j+1] && sum[i-1][j+1]<sum[i+1][j+1]){
                        sum[i][j] = e[i][j] + sum[i-1][j+1];
                    }
                    else if(sum[i][j+1]<sum[i-1][j+1] && sum[i][j+1]<sum[i+1][j+1]){
                        sum[i][j] = e[i][j] + sum[i][j+1];
                    }
                    else{
                        sum[i][j] = e[i][j] + sum[i+1][j+1];
                    }
                }
            }
        }
        int i = 0;
        for(int j = 0; j<sum.length; j++){
            if(sum[j][0]<sum[i][0]){
                i = j;
            }
        }
        int[] seam = new int[sum[0].length];
        int count = 0;
        seam[count++] = i; boolean uflag = false; boolean dflag = false;
        for(int j = 0; j<sum[0].length-1; j++){
            if(i==0){
                uflag = true;
                if(sum[i][j+1]<sum[i+1][j+1]){
                    seam[count++] = i;
                }
                else{
                    i = i+1;
                    seam[count++] = i;
                }
            }
            else if(i==sum.length-1){
                dflag = true;
                if(sum[i][j+1]<sum[i-1][j+1]){
                    seam[count++] = i;
                }
                else{
                    i = i-1;
                    seam[count++] = i;
                }
            }
            else{
                if(i-1==0 && dflag){
                    if(sum[i][j+1]<sum[i+1][j+1]){
                        seam[count++] = i;
                    }
                    else{
                        i = i+1;
                        seam[count++] = i;
                    }
                }
                else if(i+1==sum.length-1 && uflag){
                    if(sum[i][j+1]<sum[i-1][j+1]){
                        seam[count++] = i;
                    }
                    else{
                        i = i-1;
                        seam[count++] = i;
                    }
                }
                else{
                    if(sum[i-1][j+1]<sum[i][j+1] && sum[i-1][j+1]<sum[i+1][j+1]){
                        i = i-1;
                        seam[count++] = i;
                    }
                    else if(sum[i][j+1]<sum[i-1][j+1] && sum[i][j+1]<sum[i+1][j+1]){
                        seam[count++] = i;
                    }
                    else{
                        i = i+1;
                        seam[count++] = i;
                    }
                }
            }
        }
        return seam;
    }
    public void removeVerticalSeam(int[] seam){
        if(seam==null){
            throw new NullPointerException("Seam was null");
        }
        if(p.width()==1 || seam.length!=p.height()){
            throw new IllegalArgumentException();
        }
        Picture np = new Picture(p.width()-1, p.height());
        for(int i = 0; i<=height(); i++){
            int new_j = 0;
            for(int j = 0; j<=width(); j++){
                if(seam[i]!=j){    
                    np.set(new_j++, i, p.get(j, i));
                }    
            }
        }
        this.p = np;
        this.height = p.height()-1;
        this.width = p.width()-1;
        e = new double[p.height()][p.width()];
        for(int i = 0; i<p.height(); i++){
            for(int j = 0; j<p.width(); j++){
                e[i][j] = energy(j, i);
            }
        }
    }
    public void removeHorizontalSeam(int[] seam){
        if(seam==null){
            throw new NullPointerException("Seam was null");
        }
        if(p.height()==1 || seam.length!=p.width()){
            throw new IllegalArgumentException();
        }
        Picture np = new Picture(p.width(), p.height()-1);
        for(int i = 0; i<=width(); i++){
            int new_j = 0;
            for(int j = 0; j<=height(); j++){
                if(seam[i]!=j){
                    np.set(i, new_j++, p.get(i, j));
                }
            }
        }
        this.p = np;
        this.height = p.height()-1;
        this.width = p.width()-1;
        e = new double[p.height()][p.width()];
        for(int i = 0; i<p.height(); i++){
            for(int j = 0; j<p.width(); j++){
                e[i][j] = energy(j, i);
            }
        }
    }
    
    public static void main(String[] args) {
        Picture picture = new Picture("HJoceanSmall.png");
        SeamCarver sc = new SeamCarver(picture);
        sc.picture().show();
        for(int i = 0; i<250; i++){
            sc.removeHorizontalSeam(sc.findHorizontalSeam());
            sc.removeVerticalSeam(sc.findVerticalSeam());
            sc.picture().show();
        }
    }
    
}

/******************************************************************************
 *  Compilation:  javac Picture.java
 *  Execution:    java Picture imagename
 *  Dependencies: none
 *
 *  Data type for manipulating individual pixels of an image. The original
 *  image can be read from a file in jpg, gif, or png format, or the
 *  user can create a blank image of a given size. Includes methods for
 *  displaying the image in a window on the screen or saving to a file.
 *
 *  % java Picture mandrill.jpg
 *
 *  Remarks
 *  -------
 *   - pixel (x, y) is column x and row y, where (0, 0) is upper left
 *
 *   - see also GrayPicture.java for a grayscale version
 *
 ******************************************************************************/


/**
 *  This class provides methods for manipulating individual pixels of
 *  an image. The original image can be read from a <tt>.jpg</tt>, <tt>.gif</tt>,
 *  or <tt>.png</tt> file or the user can create a blank image of a given size.
 *  This class includes methods for displaying the image in a window on
 *  the screen or saving it to a file.
 *  <p>
 *  Pixel (<em>x</em>, <em>y</em>) is column <em>x</em> and row <em>y</em>.
 *  By default, the origin (0, 0) is upper left, which is a common convention
 *  in image processing.
 *  The method <tt>setOriginLowerLeft()</tt> change the origin to the lower left.
 *  <p>
 *  For additional documentation, see
 *  <a href="http://introcs.cs.princeton.edu/31datatype">Section 3.1</a> of
 *  <i>Introduction to Programming in Java: An Interdisciplinary Approach</i>
 *  by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */
final class Picture implements ActionListener {
    private BufferedImage image;               // the rasterized image
    private JFrame frame;                      // on-screen view
    private String filename;                   // name of file
    private boolean isOriginUpperLeft = true;  // location of origin
    private final int width, height;           // width and height

   /**
     * Initializes a blank <tt>width</tt>-by-<tt>height</tt> picture, with <tt>width</tt> columns
     * and <tt>height</tt> rows, where each pixel is black.
     *
     * @param width the width of the picture
     * @param height the height of the picture
     */
    public Picture(int width, int height) {
        if (width  < 0) throw new IllegalArgumentException("width must be nonnegative");
        if (height < 0) throw new IllegalArgumentException("height must be nonnegative");
        this.width  = width;
        this.height = height;
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        // set to TYPE_INT_ARGB to support transparency
        filename = width + "-by-" + height;
    }

   /**
     * Initializes a new picture that is a deep copy of the argument picture.
     *
     * @param picture the picture to copy
     */
    public Picture(Picture picture) {
        width  = picture.width();
        height = picture.height();
        image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        filename = picture.filename;
        for (int col = 0; col < width(); col++)
            for (int row = 0; row < height(); row++)
                image.setRGB(col, row, picture.get(col, row).getRGB());
    }

   /**
     * Initializes a picture by reading from a file or URL.
     *
     * @param filename the name of the file (.png, .gif, or .jpg) or URL.
     */
    public Picture(String filename) {
        this.filename = filename;
        try {
            // try to read from file in working directory
            File file = new File(filename);
            if (file.isFile()) {
                image = ImageIO.read(file);
            }

            // now try to read from file in same directory as this .class file
            else {
                URL url = getClass().getResource(filename);
                if (url == null) {
                    url = new URL(filename);
                }
                image = ImageIO.read(url);
            }

            if (image == null) {
                throw new IllegalArgumentException("Invalid image file: " + filename);
            }

            width  = image.getWidth(null);
            height = image.getHeight(null);
        }
        catch (IOException e) {
            // e.printStackTrace();
            throw new RuntimeException("Could not open file: " + filename);
        }
    }

   /**
     * Initializes a picture by reading in a .png, .gif, or .jpg from a file.
     *
     * @param file the file
     */
    public Picture(File file) {
        try {
            image = ImageIO.read(file);
        }
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Could not open file: " + file);
        }
        if (image == null) {
            throw new RuntimeException("Invalid image file: " + file);
        }
        width  = image.getWidth(null);
        height = image.getHeight(null);
        filename = file.getName();
    }

   /**
     * Returns a JLabel containing this picture, for embedding in a JPanel,
     * JFrame or other GUI widget.
     *
     * @return the <tt>JLabel</tt>
     */
    public JLabel getJLabel() {
        if (image == null) return null;         // no image available
        ImageIcon icon = new ImageIcon(image);
        return new JLabel(icon);
    }

   /**
     * Sets the origin to be the upper left pixel. This is the default.
     */
    public void setOriginUpperLeft() {
        isOriginUpperLeft = true;
    }

   /**
     * Sets the origin to be the lower left pixel.
     */
    public void setOriginLowerLeft() {
        isOriginUpperLeft = false;
    }

   /**
     * Displays the picture in a window on the screen.
     */
    public void show() {

        // create the GUI for viewing the image if needed
        if (frame == null) {
            frame = new JFrame();

            JMenuBar menuBar = new JMenuBar();
            JMenu menu = new JMenu("File");
            menuBar.add(menu);
            JMenuItem menuItem1 = new JMenuItem(" Save...   ");
            menuItem1.addActionListener(this);
            menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                                     Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
            menu.add(menuItem1);
            frame.setJMenuBar(menuBar);



            frame.setContentPane(getJLabel());
            // f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            frame.setTitle(filename);
            frame.setResizable(false);
            frame.pack();
            frame.setVisible(true);
        }

        // draw
        frame.repaint();
    }

   /**
     * Returns the height of the picture.
     *
     * @return the height of the picture (in pixels)
     */
    public int height() {
        return height;
    }

   /**
     * Returns the width of the picture.
     *
     * @return the width of the picture (in pixels)
     */
    public int width() {
        return width;
    }

   /**
     * Returns the color of pixel (<tt>col</tt>, <tt>row</tt>).
     *
     * @param col the column index
     * @param row the row index
     * @return the color of pixel (<tt>col</tt>, <tt>row</tt>)
     * @throws IndexOutOfBoundsException unless both 0 &le; <tt>col</tt> &lt; <tt>width</tt>
     *         and 0 &le; <tt>row</tt> &lt; <tt>height</tt>
     */
    public Color get(int col, int row) {
        if (col < 0 || col >= width())  throw new IndexOutOfBoundsException("col must be between 0 and " + (width()-1));
        if (row < 0 || row >= height()) throw new IndexOutOfBoundsException("row must be between 0 and " + (height()-1));
        if (isOriginUpperLeft) return new Color(image.getRGB(col, row));
        else                   return new Color(image.getRGB(col, height - row - 1));
    }

   /**
     * Sets the color of pixel (<tt>col</tt>, <tt>row</tt>) to given color.
     *
     * @param col the column index
     * @param row the row index
     * @param color the color
     * @throws IndexOutOfBoundsException unless both 0 &le; <tt>col</tt> &lt; <tt>width</tt>
     *         and 0 &le; <tt>row</tt> &lt; <tt>height</tt>
     * @throws NullPointerException if <tt>color</tt> is <tt>null</tt>
     */
    public void set(int col, int row, Color color) {
        if (col < 0 || col >= width())  throw new IndexOutOfBoundsException("col must be between 0 and " + (width()-1));
        if (row < 0 || row >= height()) throw new IndexOutOfBoundsException("row must be between 0 and " + (height()-1));
        if (color == null) throw new NullPointerException("can't set Color to null");
        if (isOriginUpperLeft) image.setRGB(col, row, color.getRGB());
        else                   image.setRGB(col, height - row - 1, color.getRGB());
    }

   /**
     * Returns true if this picture is equal to the argument picture.
     *
     * @param other the other picture
     * @return <tt>true</tt> if this picture is the same dimension as <tt>other</tt>
     *         and if all pixels have the same color; <tt>false</tt> otherwise
     */
    public boolean equals(Object other) {
        if (other == this) return true;
        if (other == null) return false;
        if (other.getClass() != this.getClass()) return false;
        Picture that = (Picture) other;
        if (this.width()  != that.width())  return false;
        if (this.height() != that.height()) return false;
        for (int col = 0; col < width(); col++)
            for (int row = 0; row < height(); row++)
                if (!this.get(col, row).equals(that.get(col, row))) return false;
        return true;
    }

    /**
     * This operation is not supported because pictures are mutable.
     *
     * @return does not return a value
     * @throws UnsupportedOperationException if called
     */
    public int hashCode() {
        throw new UnsupportedOperationException("hashCode() is not supported because pictures are mutable");
    }

   /**
     * Saves the picture to a file in a standard image format.
     * The filetype must be .png or .jpg.
     *
     * @param name the name of the file
     */
    public void save(String name) {
        save(new File(name));
    }

   /**
     * Saves the picture to a file in a PNG or JPEG image format.
     *
     * @param file the file
     */
    public void save(File file) {
        filename = file.getName();
        if (frame != null) frame.setTitle(filename);
        String suffix = filename.substring(filename.lastIndexOf('.') + 1);
        suffix = suffix.toLowerCase();
        if (suffix.equals("jpg") || suffix.equals("png")) {
            try {
                ImageIO.write(image, suffix, file);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Error: filename must end in .jpg or .png");
        }
    }

   /**
     * Opens a save dialog box when the user selects "Save As" from the menu.
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        FileDialog chooser = new FileDialog(frame,
                             "Use a .png or .jpg extension", FileDialog.SAVE);
        chooser.setVisible(true);
        if (chooser.getFile() != null) {
            save(chooser.getDirectory() + File.separator + chooser.getFile());
        }
    }


   /**
     * Unit tests this <tt>Picture</tt> data type.
     * Reads a picture specified by the command-line argument,
     * and shows it in a window on the screen.
     */
    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);
        System.out.printf("%d-by-%d\n", picture.width(), picture.height());
        picture.show();
    }

}


/******************************************************************************
 *  Copyright 2002-2015, Robert Sedgewick and Kevin Wayne.
 *
 *  This file is part of algs4.jar, which accompanies the textbook
 *
 *      Algorithms, 4th edition by Robert Sedgewick and Kevin Wayne,
 *      Addison-Wesley Professional, 2011, ISBN 0-321-57351-X.
 *      http://algs4.cs.princeton.edu
 *
 *
 *  algs4.jar is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  algs4.jar is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with algs4.jar.  If not, see http://www.gnu.org/licenses.
 ******************************************************************************/
