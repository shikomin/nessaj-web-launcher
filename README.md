# nessaj-web-launcher
java-launcher
### java-launcher version-0.1

java-launcher 可以帮你动态启动一个springboot工程
```java
public class JavaLauncherTest{

    public static void main(String[] args) {
        Launcher launcher = new Launcher("E:\\etc\\demo1");
        launcher.launch(args);
    }
    
}

