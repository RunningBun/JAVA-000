import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;

/**
 * project_name:    class01
 * package :        PACKAGE_NAME
 * describe :
 *
 * @author :        Luo
 * creat_date :     2020/10/19 17:33
 */
public class TestClassLoader extends ClassLoader {

    public static void main(String[] args) throws InvocationTargetException {
        try {
            Class helloCls = new TestClassLoader().findClass("Hello");
            helloCls.getDeclaredMethod("hello").invoke(helloCls.newInstance());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected Class<?> findClass(String name) throws ClassNotFoundException {
        InputStream inputStream = this.getResourceAsStream("Hello.xlass");
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        byte[] tempBytes = new byte[1];
        try {
            while ((inputStream.read(tempBytes)) != -1) {
                byteArrayOutputStream.write(tempBytes);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
                byteArrayOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        byte[] readBytes = byteArrayOutputStream.toByteArray();
        byte[] result = new byte[readBytes.length];
        for (int i = 0; i < readBytes.length; i++) {
            result[i] = (byte) (255 - readBytes[i]);
        }

        return defineClass(name, result, 0, result.length);
    }
}
