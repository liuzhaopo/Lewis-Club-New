package com.lzhpo;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.util.ImageHelper;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

/**
 * <p> Author：lzhpo </p>
 * <p> Title：</p>
 * <p> Description：
 *
 * 图像倾斜的处理办法：
         BufferedImage bi = ImageIO.read(imageFile);
         ImageDeskew id = new ImageDeskew(bi);
         double imageSkewAngle = id.getSkewAngle(); //获取倾斜角度
         if ((imageSkewAngle > 0.05d || imageSkewAngle < -(0.05d))) {
            bi = ImageHelper.rotateImage(bi, -imageSkewAngle); //纠偏图像
         }
 * </p>
 */
public class ReadImages {

    public static void main(String[] args) throws Exception{

        /**
         * 模糊图像，像素不好，建议不放大。
         */
//        testMHYzm();

        /**
         * 清晰图像，建议放大，识别度更精准。
         */
//        testConvertYzm();

        /**
         * 识别英文图像
         */
//        testConvertEn();

        /**
         * 识别中文图像
         */
        testConvertZh();
    }

    /**
     * 使用英文字库 - 识别图片，不清晰验证码（mohu-yzm.png）不处理放大，现在已经是最好效果。
     * @throws Exception
     */
    private static void testMHYzm() throws Exception {
        //图片路径：当你的代码所在的目录是maven的父工程的时候，你就可以像我这样写相对路径，注意一定要是在maven父工程；否则，就写物理路径！不然会无法读取。
        //File imageFile = new File("./images/yzm.png");
        File imageFile = new File("./images/mohu-yzm.png");//这种验证码建议不要处理，不要放大，因为它不是很清晰，放大之后有偏差。
        BufferedImage image = ImageIO.read(imageFile);
        //对图片进行处理，提高识别度，如果图片清晰度已经可以了，建议不要处理，放大之后可能会有偏差。
        //image = convertImage(image);
        ITesseract instance = new Tesseract();//JNA Interface Mapping
        //设置tessdata的路径
        //instance.setDatapath(""); //当你的代码所在的目录是maven的父工程的时候，不需要写，注意一定要是在maven父工程；否则，就写物理路径！不然会无法读取。
        String result = instance.doOCR(image); //识别
        System.out.println(result);
    }

    /**
     * 使用英文字库 - 识别图片，清晰验证码处理放大
     * @throws Exception
     */
    private static void testConvertYzm() throws Exception {
        //图片路径：当你的代码所在的目录是maven的父工程的时候，你就可以像我这样写相对路径，注意一定要是在maven父工程；否则，就写物理路径！不然会无法读取。
        File imageFile = new File("./images/yzm.png");
        //File imageFile = new File("./images/mohu-yzm.png");
        BufferedImage image = ImageIO.read(imageFile);
        //对图片进行处理，提高识别度，如果图片清晰度已经可以了，建议不要处理，放大之后可能会有偏差。
        image = convertImage(image);
        ITesseract instance = new Tesseract();//JNA Interface Mapping
        //设置tessdata的路径
        //instance.setDatapath(""); //当你的代码所在的目录是maven的父工程的时候，不需要写，注意一定要是在maven父工程；否则，就写物理路径！不然会无法读取。
        String result = instance.doOCR(image); //识别
        System.out.println(result);
    }

    /**
     * 使用英文字库 - 识别图片，并处理放大
     * @throws Exception
     */
    private static void testConvertEn() throws Exception {
        //图片路径：当你的代码所在的目录是maven的父工程的时候，你就可以像我这样写相对路径，注意一定要是在maven父工程；否则，就写物理路径！不然会无法读取。
        File imageFile = new File("./images/en.png");
        //读取图片路径，并提高识别度
        BufferedImage image = ImageIO.read(imageFile);
        //对图片进行处理，提高识别度，如果图片清晰度已经可以了，建议不要处理，放大之后可能会有偏差。
        image = convertImage(image);
        ITesseract instance = new Tesseract();//JNA Interface Mapping
        //设置tessdata的路径
        //instance.setDatapath(""); //当你的代码所在的目录是maven的父工程的时候，不需要写，注意一定要是在maven父工程；否则，就写物理路径！不然会无法读取。
        String result = instance.doOCR(image); //识别
        System.out.println(result);
    }

    /**
     * 使用中文字库 - 识别图片，并处理放大
     * @throws Exception
     */
    private static void testConvertZh() throws Exception {
        //图片路径：当你的代码所在的目录是maven的父工程的时候，你就可以像我这样写相对路径，注意一定要是在maven父工程；否则，就写物理路径！不然会无法读取。
        File imageFile = new File("./images/zh.png");
        //读取图片路径，并提高识别度
        BufferedImage image = ImageIO.read(imageFile);
        //对图片进行处理，提高识别度，如果图片清晰度已经可以了，建议不要处理，放大之后可能会有偏差。
        image = convertImage(image);
        ITesseract instance = new Tesseract();//JNA Interface Mapping
        //设置tessdata的路径
        //instance.setDatapath(""); //当你的代码所在的目录是maven的父工程的时候，不需要写，注意一定要是在maven父工程；否则，就写物理路径！不然会无法读取。
        instance.setLanguage("chi_sim");//使用中文字库
        String result = instance.doOCR(image); //识别
        System.out.println(result);
    }

    /** 对图片进行处理 - 提高识别度 **/
    private static BufferedImage convertImage(BufferedImage image) throws Exception {
        //按指定宽高创建一个图像副本
        //image = ImageHelper.getSubImage(image, 0, 0, image.getWidth(), image.getHeight());
        //图像转换成灰度的简单方法 - 黑白处理
        image = ImageHelper.convertImageToGrayscale(image);
        //图像缩放 - 放大n倍图像
        image = ImageHelper.getScaledInstance(image, image.getWidth() * 3, image.getHeight() * 3);
        return image;
    }

}
