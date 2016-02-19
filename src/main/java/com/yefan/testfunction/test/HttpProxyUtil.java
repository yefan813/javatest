package com.yefan.testfunction.test;


import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

/**
 * 
 * 〈一个简单的下载网络图片工具类,可以下载内网或者代理下载外网图片〉
 *
 * @author yli
 */
public class HttpProxyUtil {

    public static void main(String[] args) throws MalformedURLException, IOException {

         //下载图片,不经过代理
         String url1 = "https://c1.staticflickr.com/3/2880/11233829404_e2df1b17cf_h.jpg";
         String saveUrl1 = "E://images/11233829404_e2df1b17cf_h.jpg";
         downImgFromInetDemo(url1, saveUrl1, null);
//        
        // 经过代理下载图片
//        String host = "12.173.71.197";  
//        int port = 8080;
//        Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress(host, port));
//        String url2 = "https://c1.staticflickr.com/3/2880/11233829404_3a62db5b2e_b.jpg";
//        String saveUrl2 = "E://images/11233829404_3a62db5b2e_b.jpg";
//        downImgFromInetDemo(url2, saveUrl2, proxy);
    }

    /**
     * 
     * 〈下载网络图片方法示例〉
     *
     * @param targetUrl[要下载的图片对应的网络地址]
     * @param saveUrl[要保存的地址]
     * @param proxy [不通过代理下载就直接传递null进来]
     * @throws MalformedURLException
     * @throws IOException
     */
    public static void downImgFromInetDemo(String targetUrl, String saveUrl, Proxy proxy)
            throws MalformedURLException, IOException {

        // 读取InputStream字节流,将图片内容读取到一个字节数组
        // 该字节数组就可以重复利用
        byte[] imgBytes = getByteArrayFromInputStream(proxy, targetUrl);

        // 获取图片基本信息:大小、宽度、高度和格式
        ImageInfo targetImg = getImageInfoFromInputStream(imgBytes);
        targetImg.setSize(imgBytes.length);

        // 实例化一个符合业务要求的图片对象
        ImageInfo validImg = new ImageInfo();
        validImg.setSize(2000 * 6000); // 最大允许500KB
        validImg.setWidth(6000); // 最宽允许 1500px
        validImg.setHeight(2000); // 最高允许1200px
        validImg.setValidTypes("jpg", "jpeg", "gif", "png", "bmp");

        // 校验图片是否符合业务要求
        if (targetImg.isValidImg(validImg)) {
            // 保存图片
            ByteArrayInputStream in = new ByteArrayInputStream(imgBytes);
            saveImgFromInputStream(in, saveUrl);
            System.out.println("===>图片符合要求,保存成功");
        } else {
            System.out.println("===>图片不符合要求！");
        }

        // 可以简单的打印图片基本信息
        System.out.println(targetImg);
        System.out.println(validImg);

    }

    /**
     * 
     * 〈将图片文件流转换成字节数组〉
     * 〈好处就是字节数组可以多次利用,而流一旦读取过一次之后就不能再使用了〉
     *
     * @param proxy
     * @param targetUrl
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    private static byte[] getByteArrayFromInputStream(Proxy proxy, String targetUrl)
            throws MalformedURLException, IOException {
        HttpURLConnection urlConnection;
        if (null == proxy) {
            urlConnection = (HttpURLConnection) new URL(targetUrl).openConnection();
        } else {
            // 如果有代理则通过代理下载
            urlConnection = (HttpURLConnection) new URL(targetUrl).openConnection(proxy);
        }
        // 把文件写到字节数组保存起来
        BufferedInputStream bis = new BufferedInputStream(urlConnection.getInputStream());
        ByteArrayOutputStream fos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = bis.read(buffer)) != -1) {
            fos.write(buffer, 0, len);
        }
        byte[] bytes = fos.toByteArray();
        bis.close();
        fos.close();
        urlConnection.disconnect();
        return bytes;
    }
    
    /**
     * 
     * 〈将流保存至本机或者图片服务器〉
     *
     * @param in
     * @param saveUrl
     * @throws IOException
     */
    public static void saveImgFromInputStream(InputStream in, String saveUrl) throws IOException {
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(saveUrl));
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = in.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        in.close();
    }

    /**
     * 
     * 〈如果你已经将图片文件流InputStream读取出来放到一个字节数组〉
     * 〈那么根据这个字节数组也是可以转换成对应的图片流，并再次获取图片基本信息〉
     *
     * @param imgBytes
     * @return
     */
    public static ImageInfo getImageInfoFromInputStream(byte[] imgBytes) {
        ByteArrayInputStream in = new ByteArrayInputStream(imgBytes);
        ImageInfo image = getImageInfoFromInputStream(in);
        return image;
    }
    
    /**
     * 
     * 〈从图片文件流读取图片文件的基本信息〉
     *
     * @param inputStream
     * @return
     */
    public static ImageInfo getImageInfoFromInputStream(InputStream inputStream) {
        ImageInputStream imgStream = null;
        try {
            // 创建Image流
            imgStream = ImageIO.createImageInputStream(inputStream);
            Iterator<ImageReader> iter = ImageIO.getImageReaders(imgStream);
            if (!iter.hasNext()) {
                return null;
            }
            // 读取流中一帧就可以获取到高宽以及各式
            ImageReader reader = iter.next();
            reader.setInput(imgStream, true);
            int width = reader.getWidth(0);
            int height = reader.getHeight(0);
            String type = reader.getFormatName();
            ImageInfo bean = new ImageInfo();
            bean.setWidth(width);
            bean.setHeight(height);
            bean.setType(type);
            return bean;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                imgStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

/**
 * 
 * 〈一般下载图片都会有一些简单的业务检验，比如大小格式等〉
 * 〈因此定义一个图片类，来描述图片的基本属性〉
 *
 * @author yli
 */
class ImageInfo {
    // 图片大小
    private long     size   = 0;
    // 图片宽度
    private int      width  = 0;
    // 图片高度
    private int      height = 0;
    // 图片类型
    private String   type   = "jpg";
    // 符号要求的图片类型
    private String[] validTypes;

    /**
     * 
     * 〈定义一个简单方法描述该图片是否符合要求〉
     *
     * @param validImg
     * @return
     */
    public boolean isValidImg(ImageInfo validImg) {
        if (null == validImg) {
            return true;
        }
        return (this.getSize() <= validImg.getSize()) && (this.getWidth() <= validImg.getWidth())
                && (this.getHeight() <= validImg.getHeight()) && isValidType(validImg);

    }

    private boolean isValidType(ImageInfo validImg) {
        if (null == validImg.getValidTypes()) {
            return true;
        }
        boolean isValid = false;
        for (String validType : validImg.getValidTypes()) {
            if (type.equalsIgnoreCase(validType)) {
                isValid = true;
                break;
            }
        }
        return isValid;
    }

    @Override
    public String toString() {
        return String.format("{size:%s,width:%s,height:%s,type:%s,validType:%s}", size, width,
                height, type, validTypeToString());
    }

    private String validTypeToString() {
        if (null == validTypes) {
            return "";
        }
        StringBuffer sb = new StringBuffer("[");
        for (String type : validTypes) {
            sb.append(type).append(" ");
        }
        sb.append("]");
        return sb.toString();
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String[] getValidTypes() {
        return validTypes;
    }

    public void setValidTypes(String... types) {
        this.validTypes = types;
    }
}