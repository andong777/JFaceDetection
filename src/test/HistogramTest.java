package test;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.MemoryImageSource;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;

public class HistogramTest extends JFrame {

    //保存当前操作的像素矩阵
    private int currentPixArray[]=null;

    //图像的路径
    private String fileString=null;
    //用于显示图像的标签
    private JLabel imageLabel=null;

    //加载的图像
    private BufferedImage newImage;

    //图像的高和宽
    private int h,w;

    //保存历史操作图像矩阵
    private LinkedList<int[]> imageStack=new LinkedList<int[]>();
    private LinkedList<int[]> tempImageStack=new LinkedList<int[]>();





    public HistogramTest(String title){
        super(title);
        this.setSize(800,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //创建菜单
        JMenuBar jb=new JMenuBar();
        JMenu fileMenu=new JMenu("文件");
        jb.add(fileMenu);

        JMenuItem openImageMenuItem=new JMenuItem("打开图像");
        fileMenu.add(openImageMenuItem);
        openImageMenuItem.addActionListener(new OpenListener());

        JMenuItem exitMenu=new JMenuItem("退出");
        fileMenu.add(exitMenu);
        exitMenu.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                System.exit(0);
            }
        });

        JMenu operateMenu=new JMenu("图像处理");
        jb.add(operateMenu);

        JMenuItem RGBtoGrayMenuItem=new JMenuItem("灰度图像转换");
        operateMenu.add(RGBtoGrayMenuItem);
        RGBtoGrayMenuItem.addActionListener(new RGBtoGrayActionListener());

        JMenuItem balanceMenuItem=new JMenuItem("均衡化");
        operateMenu.add(balanceMenuItem);
        balanceMenuItem.addActionListener(new BalanceActionListener());

        JMenu frontAndBackMenu=new JMenu("历史操作");
        jb.add(frontAndBackMenu);

        JMenuItem backMenuItem=new JMenuItem("后退");
        frontAndBackMenu.add(backMenuItem);
        backMenuItem.addActionListener(new BackActionListener());

        JMenuItem frontMenuItem=new JMenuItem("前进");
        frontAndBackMenu.add(frontMenuItem);
        frontMenuItem.addActionListener(new FrontActionListener());

        this.setJMenuBar(jb);

        imageLabel=new JLabel("");
        JScrollPane pane = new    JScrollPane(imageLabel);
        this.add(pane,BorderLayout.CENTER);

        this.setVisible(true);

    }

    private class OpenListener implements ActionListener{
        public void actionPerformed(ActionEvent e){
            JFileChooser jc=new JFileChooser();
            int returnValue=jc.showOpenDialog(null);
            if (returnValue ==    JFileChooser.APPROVE_OPTION) {
                File selectedFile =    jc.getSelectedFile();
                if (selectedFile != null) {
                    fileString=selectedFile.getAbsolutePath();
                    try{
                        newImage =ImageIO.read(new File(fileString));
                        w=newImage.getWidth();
                        h=newImage.getHeight();
                        currentPixArray=getPixArray(newImage,w,h);
                        imageStack.clear();
                        tempImageStack.clear();
                        imageStack.addLast(currentPixArray);
                        imageLabel.setIcon(new ImageIcon(newImage));

                    }catch(IOException ex){
                        System.out.println(ex);
                    }

                }
            }
            HistogramTest.this.repaint();
            //HistogramTest.this.pack();
        }
    }


    //////////////////菜单监听器///////////
    private class RGBtoGrayActionListener implements ActionListener{

        public void actionPerformed(ActionEvent e){
            int[] resultArray=RGBtoGray(currentPixArray);
            imageStack.addLast(resultArray);
            currentPixArray=resultArray;
            showImage(resultArray);
            tempImageStack.clear();
        }

    }

    private class BalanceActionListener implements ActionListener{

        public void actionPerformed(ActionEvent e){
            int[] resultArray=balance(currentPixArray);
            imageStack.addLast(resultArray);
            currentPixArray=resultArray;
            showImage(resultArray);
            tempImageStack.clear();
        }

    }

    private class BackActionListener implements ActionListener{

        public void actionPerformed(ActionEvent e){
            if(imageStack.size()<=1){
                JOptionPane.showMessageDialog(null,"此幅图片的处理已经没有后退历史操作了","提示",
                        JOptionPane.INFORMATION_MESSAGE);
            }else{
                tempImageStack.addLast(imageStack.removeLast());
                currentPixArray=imageStack.getLast();
                showImage(currentPixArray);
            }
        }

    }

    private class FrontActionListener implements ActionListener{

        public void actionPerformed(ActionEvent e){
            if(tempImageStack.size()<1){
                JOptionPane.showMessageDialog(null,"此幅图片的处理已经没有前进历史操作了","提示",
                        JOptionPane.INFORMATION_MESSAGE);
            }else{
                currentPixArray=tempImageStack.removeFirst();
                imageStack.addLast(currentPixArray);
                showImage(currentPixArray);
            }
        }

    }


    //////////////////获取图像像素矩阵/////////
    private int[]getPixArray(Image im,int w,int h){
        int[] pix=new int[w*h];
        PixelGrabber pg=null;
        try{
            pg = new PixelGrabber(im, 0, 0, w, h, pix, 0, w);
            if(pg.grabPixels()!=true)
                try{
                    throw new java.awt.AWTException("pg error"+pg.status());
                }catch(Exception eq){
                    eq.printStackTrace();
                }
        } catch(Exception ex){
            ex.printStackTrace();

        }
        return pix;
    }



    //////////////////显示图片///////////
    private void showImage(int[] srcPixArray){
        Image pic=createImage(new MemoryImageSource(w,h,srcPixArray,0,w));
        ImageIcon ic=new ImageIcon(pic);
        imageLabel.setIcon(ic);
        imageLabel.repaint();
    }


    //////////////////灰度转换///////////
    private int[] RGBtoGray(int[] ImageSource){
        int[]grayArray=new int[h*w];
        ColorModel colorModel=ColorModel.getRGBdefault();
        int i ,j,k,r,g,b;
        for(i = 0; i < h;i++){
            for(j = 0;j < w;j++){
                k = i*w+j;
                r = colorModel.getRed(ImageSource[k]);
                g = colorModel.getGreen(ImageSource[k]);
                b = colorModel.getBlue(ImageSource[k]);
                int gray=(int)(r*0.3+g*0.59+b*0.11);
                r=g=b=gray;
                grayArray[i*w+j]=(255 << 24) | (r << 16) | (g << 8 )| b;
            }
        }
        return grayArray;
    }


    /////////////////图像均衡化///////////
    private int[] balance(int[] srcPixArray){
        int[] histogram=new int[256];
        int[] dinPixArray=new int[w*h];

        for(int i=0;i<h;i++){
            for(int j=0;j<w;j++){
                int grey=srcPixArray[i*w+j]&0xff;
                histogram[grey]++;
            }
        }
        double a=(double)255/(w*h);
        double[] c=new double[256];
        c[0]=(a*histogram[0]);
        for(int i=1;i<256;i++){
            c[i]=c[i-1]+(int)(a*histogram[i]);
        }
        for(int i=0;i<h;i++){
            for(int j=0;j<w;j++){
                int grey=srcPixArray[i*w+j]&0x0000ff;
                int hist=(int)c[grey];

                dinPixArray[i*w+j]=255<<24|hist<<16|hist<<8|hist;
            }
        }
        return dinPixArray;
    }




    public static void main(String[] args) {
        new HistogramTest("ShowImage");
    }

}