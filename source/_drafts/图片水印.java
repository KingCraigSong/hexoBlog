

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.util.Base64;

/**
 * 〈文字水印〉
 *
 * @create 2019/8/5
 * @since 1.0.0
 */
public class WatermarkUtil {

    public static final String FONT_NAME = "微软雅黑";
    public static final int FONT_STYLE = Font.PLAIN;
    public static final int FONT_SIZE = 30;
    public static final Color FONT_COLOR = Color.BLACK;


    public static final float ALPHA = 0.1F;

    /**
     * @return int
     * @Author songtg2
     * @Date 14:09 2019/8/5
     * @Param [text]
     * @Description 获取文字长度
     **/
    public static int getTextLength(String text) {
        int length = text.length();
        for (int i = 0; i < text.length(); i++) {
            String s = String.valueOf(text.charAt(i));
            if (s.getBytes().length > 1) {
                length++;
            }
        }
        length = (length % 2 == 0) ? length / 2 : length / 2 + 1;
        return length;
    }

    /**
     * @return void
     * @Author songtg2
     * @Date 14:09 2019/8/5
     * @Param [bufferedImage, out, content]
     * @Description 生成多个文字的水印
     **/
    public static String multiWatermark(BufferedImage bufferedImage, String content) {
        //1、创建图片缓存对象
        String base64 = "";
        try {
            //获取原图信息
            Image image2 = bufferedImage;
            int width = image2.getWidth(null);
            int height = image2.getHeight(null);

            BufferedImage bfImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            //2、创建绘图工具对象
            Graphics2D graph = bfImage.createGraphics();

            //3、使用绘图工具对象将原图绘制到缓存图像对象中
            graph.drawImage(image2, 0, 0, width, height, null);
            graph.setFont(new Font(FONT_NAME, FONT_STYLE, FONT_SIZE));
            graph.setColor(FONT_COLOR);

            //4、使用绘图工具对象将水印（文字/图片）绘制到缓存图片对象中
            int textW = FONT_SIZE * getTextLength(content);
            int textH = FONT_SIZE;

            //设置透明度
            graph.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_ATOP, ALPHA));

            graph.rotate(Math.toRadians(30), bfImage.getWidth() / 2, bfImage.getHeight() / 2);

            int x = -width / 2;
            int y = -height / 2;
            while (x < width * 1.5) {
                y = -height / 2;
                while (y < height * 1.5) {
                    // y保证至少可以显示一个水印的高度
                    graph.drawString(content, x, y);
                    // 200为间隔值，即每个水印之间的间隔
                    y += textH + 180;
                }
                x += textW + 180;
            }
            graph.dispose();

            ByteArrayOutputStream bs = null;
            try {
                bs = new ByteArrayOutputStream();
                ImageIO.write(bfImage, "png", bs);
                base64 = Base64.getEncoder().encodeToString(bs.toByteArray());
            } catch (Exception e) {
                e.toString();
            }finally {
                bs.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return base64;
    }

}
 
