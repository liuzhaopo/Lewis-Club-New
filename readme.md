## 前言 

### 提高识别度

#### 代码处理，放大图像
不清晰的图像，像素不好的，如果已经是最好效果了，建议不要处理（放大），就这样子直接识别。
```
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
```
#### 图片倾斜处理
```
BufferedImage bi = ImageIO.read(imageFile);
ImageDeskew id = new ImageDeskew(bi);
double imageSkewAngle = id.getSkewAngle(); //获取倾斜角度
if ((imageSkewAngle > 0.05d || imageSkewAngle < -(0.05d))) {
    bi = ImageHelper.rotateImage(bi, -imageSkewAngle); //纠偏图像
}
```

## 1.识别模糊图像
```
/**
 * 模糊图像，像素不好，建议不放大。
 */
testMHYzm();
```
![](http://cdn.liuzhaopo.top/readImage1.png)

## 2.识别清晰图像
```
/**
 * 清晰图像，建议放大，识别度更精准。
 */
testConvertYzm();
```
![](http://cdn.liuzhaopo.top/readImage2.png)

## 3.识别英文图像
```
/**
 * 识别英文图像
 */
testConvertEn();
```
![](http://cdn.liuzhaopo.top/readImage3.png)

## 4.识别中文图像
```
/**
 * 识别中文图像
 */
testConvertZh();
```
![](http://cdn.liuzhaopo.top/readImage4.png)

## 源代码地址
[https://github.com/liuzhaopo/readImage](https://github.com/liuzhaopo/readImage)