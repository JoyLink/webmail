package cs601.webmail.entities;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by xyz on 11/9/14.
 */
public class QP {

    public static String qpEncodeing(String str)
    {
        char[] encode = str.toCharArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < encode.length; i++)
        {
            if ((encode[i] >= '!') && (encode[i] <= '~') && (encode[i] != '=')
                    && (encode[i] != '\n'))
            {
                sb.append(encode[i]);
            }
            else if (encode[i] == '=')
            {
                sb.append("=3D");
            }
            else if (encode[i] == '\n')
            {
                sb.append("\n");
            }
            else
            {
                StringBuffer sbother = new StringBuffer();
                sbother.append(encode[i]);
                String ss = sbother.toString();
                byte[] buf = null;
                try
                {
                    buf = ss.getBytes("utf-8");
                }
                catch (UnsupportedEncodingException e)
                {
                    e.printStackTrace();
                }
                if (buf.length == 3)
                {
                    for (int j = 0; j < 3; j++)
                    {
                        String s16 = String.valueOf(Integer.toHexString(buf[j]));

                        char c16_6;
                        char c16_7;
                        if (s16.charAt(6) >= 97 && s16.charAt(6) <= 122)
                        {
                            c16_6 = (char) (s16.charAt(6) - 32);
                        }
                        else
                        {
                            c16_6 = s16.charAt(6);
                        }
                        if (s16.charAt(7) >= 97 && s16.charAt(7) <= 122)
                        {
                            c16_7 = (char) (s16.charAt(7) - 32);
                        }
                        else
                        {
                            c16_7 = s16.charAt(7);
                        }
                        sb.append("=" + c16_6 + c16_7);
                    }
                }
            }
        }
        return sb.toString();
    }

    public final static String qpDecoding(String str)
    {
        if (str == null)
        {
            return "";
        }
        try
        {
            StringBuffer sb = new StringBuffer(str);
            for (int i = 0; i < sb.length(); i++)
            {
                if (sb.charAt(i) == '\n' && sb.charAt(i - 1) == '=')
                {

                    sb.deleteCharAt(i - 1);
                }
            }
            str = sb.toString();
            byte[] bytes = str.getBytes("US-ASCII");
            for (int i = 0; i < bytes.length; i++)
            {
                byte b = bytes[i];
                if (b != 95)
                {
                    bytes[i] = b;
                }
                else
                {
                    bytes[i] = 32;
                }
            }
            if (bytes == null)
            {
                return "";
            }
            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            for (int i = 0; i < bytes.length; i++)
            {
                int b = bytes[i];
                if (b == '=')
                {
                    try
                    {
                        int u = Character.digit((char) bytes[++i], 16);
                        int l = Character.digit((char) bytes[++i], 16);
                        if (u == -1 || l == -1)
                        {
                            continue;
                        }
                        buffer.write((char) ((u << 4) + l));
                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {
                        e.printStackTrace();
                    }
                }
                else
                {
                    buffer.write(b);
                }
            }
            return new String(buffer.toByteArray(), "UTF-8");
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return "";
        }
    }
//    public static void main(String args[]) {
//        String s  = "<html><head><meta http-equiv=3D\"content-type\" content=3D\"text/html;charset=\n" +
//                "\" +\n" +
//                "                \"=3DUTF-8\\\" /><title>=E5=8A=A9=E6=82=A8=E5=85=85=E5=88=86=E5=88=A9=E7=94=A8=\\n\" +\n" +
//                "                \"=E5=A5=BDGmail=E7=9A=84=E4=B8=89=E7=82=B9=E6=8F=90=E7=A4=BA</title>";
//        System.out.println(qpDecoding(s));
//    }

}
