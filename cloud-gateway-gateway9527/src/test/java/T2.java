import java.time.ZonedDateTime;

public class T2 {
    public static void main(String[] args)
    {
        ZonedDateTime zbj = ZonedDateTime.now(); // 默认时区
        System.out.println(zbj);

//        2022-03-16T10:13:55.378+08:00[Asia/Shanghai]
    }

}
