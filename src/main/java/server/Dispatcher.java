package server;
import data.Data;
import data.Request;
import data.Response;
import files.FileHandler;

import java.io.*;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Dispatcher {
    private Socket socket;
    private MapHandler mapHandler;


    public Dispatcher(Socket socket, MapHandler mapHandler) {
        this.socket = socket;
        this.mapHandler = mapHandler;
    }




    public void delete(Request request){
        File file = null;
        if (request.isById()){
            file = mapHandler.getFile(request.getFileId());



        }

        else if (request.isByName()){
            file = mapHandler.getFile(request.getFileName());

        }
        if (Objects.requireNonNull(file).exists()){
            synchronized (file){
                mapHandler.deleteFile(file);

                if( file.delete()){
                    sendResponse( createResponse(200));
                }
            }

        }

        sendResponse(createResponse(404));
        mapHandler.serializationMap();
    }



    public void put(Request request) {
        String fileName = Data.SERVER_DATA_PATH + createFileName(request.getFileName());
        File file = new File(fileName);
        System.out.println(file);
        System.out.println(file.exists());
        if (!file.exists()) {
            synchronized (file) {
                Integer fileId = createFileId(fileName);
                mapHandler.putFile(fileName, fileId);
                if (request.getFileContent().length > 0) {
                    try (FileOutputStream outputStream = new FileOutputStream(file)) {
                        outputStream.write(request.getFileContent());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                sendResponse(createResponse(200,fileId));
            }

        }
        sendResponse(createResponse(403));
        mapHandler.serializationMap();
    }




    public void getFile(Request request){
        File file = null;
        if (request.isById())
            file = mapHandler.getFile(request.getFileId());

        else if (request.isByName()){
            file = mapHandler.getFile(request.getFileName());

        }

        if (Objects.requireNonNull(file).exists()) {
            synchronized (file) {
                byte[] content = FileHandler.loadFile(file.getPath());
                String fileName = createFileName(file.getPath());
                System.out.println(fileName);
                sendResponse(createResponse(fileName,200,content));
            }
        }

        else {
            sendResponse(createResponse(404));
        }

        mapHandler.serializationMap();








    }

    private Response createResponse(String fileName, int i, byte[] content) {
        Response response = createResponse(i,content);
        response.setFileName(fileName);
        return  response;
    }


    private String createFileName(String filePath){
        String[] comps = filePath.split("\\\\");
        return comps[comps.length-1];
    }


    private Integer createFileId(String fileName) {
        File file = new File(fileName);
        return file.hashCode();
    }




    /*отправляет клиенту ответ сервера*/
    public void sendResponse(Response response) {
        try {

            ObjectOutputStream outputStream =
                    new ObjectOutputStream(
                            socket.getOutputStream());

            outputStream.writeObject(response);
            outputStream.flush();


        } catch (IOException  e) {
            e.printStackTrace();
        }
    }



    private boolean findFile(Request request) {
        File file = null;
        if (request.isByName()) {
            file = mapHandler.getFile(request.getFileName());
        } else file = mapHandler.getFile(request.getFileId());

        return file != null;
    }








    //считывает запрос со стрима -> создает его -> возвращает
    public Request getClientRequest() {
        Request request = new Request();
        try {
            ObjectInputStream objectInputStream =
                    new ObjectInputStream(
                            socket.getInputStream());
            {
                request = (Request) objectInputStream.readObject();
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return request;

    }


















    /*создает ответ для PUT*/
    public static Response createResponse(int statusCode, int fileId){
        Response response = new  Response();
        response.setStatusCode(statusCode);
        response.setFileId(fileId);
        return response;
    }
    /*создает ответ для GET*/
    private static Response createResponse(int statusCode,byte[] fileContent){
        Response response = new Response();
        response.setFileContent(fileContent);
        response.setStatusCode(statusCode);
        return response;
    }

    /*создает ответ для статус кода*/
    public static Response createResponse(int statusCode){
        Response response = new Response();
        response.setStatusCode(statusCode);
        return response;

    }

    public void getMap() {
        try {

            HashMap<Integer,File> map = mapHandler.getMap();
            HashMap<Integer,String> newMap = new HashMap<>();
            for (Map.Entry<Integer,File> entry : map.entrySet()){
                newMap.put(entry.getKey(),entry.getValue().getName());

            }
            ObjectOutputStream outputStream =
                    new ObjectOutputStream(
                            socket.getOutputStream());

            outputStream.writeObject(newMap);
            outputStream.flush();
            Thread.sleep(1);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
