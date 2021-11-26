package data;

import java.io.File;

public class Data {
    private static final String SP = File.separator;
    public static final String SERVER_PATH = "127.0.0.1";
    public static final int SERVER_PORT = 9001;
    private static final String PATH  ="C:" + SP;
    public static final String SERVER_DATA_PATH = PATH  + "File-Server-GUIv1" + SP + "src" + SP + "main" + SP + "java"
            + SP + "server" + SP + "data" + SP;

    public static final String CLIENT_DATA_PATH = PATH +  "File-Server-GUIv1" + SP + "src" + SP +
            "main" + SP + "java" + SP + "client" + SP + "data" + SP;
    public static final String META_PATH = PATH +  "File-Server-GUIv1src" + SP + "main " + SP + "java " + SP +
            "server" + SP + "resources" + SP + "meta.bin" + SP;

}

