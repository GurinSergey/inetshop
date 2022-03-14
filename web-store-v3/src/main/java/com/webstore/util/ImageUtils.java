package com.webstore.util;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public final class ImageUtils {
    private ImageUtils(){}

    private static int getType(BufferedImage image){
        return image.getType() == 0? BufferedImage.TYPE_INT_ARGB : image.getType();
    }

    private static BufferedImage resizeTo(BufferedImage image, int size){
        BufferedImage newImage = new BufferedImage(size, size, getType(image));
        Graphics2D graphics2D = newImage.createGraphics();
        graphics2D.drawImage(image, 0, 0, size, size, null);
        graphics2D.dispose();
        return newImage;
    }

    public static BufferedImage resizeTo128(BufferedImage image){
        return resizeTo(image, 512);
    }

    public static BufferedImage resizeTo64(BufferedImage image){
        return resizeTo(image, 256);
    }

    public static BufferedImage resizeTo32(BufferedImage image){
        return resizeTo(image, 128);
    }

    public static void save(String pathWithName, BufferedImage image) throws IOException {
        ImageIO.write(image, "png", new File(pathWithName + ".png"));
    }
}
