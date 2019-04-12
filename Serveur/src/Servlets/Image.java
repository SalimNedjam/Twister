package Servlets;

import Services.ImageManager;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.sql.Blob;
import java.sql.SQLException;

@MultipartConfig(maxFileSize = 16177215)
// upload file's size up to 16MB
public class Image extends HttpServlet {


    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("key");

        InputStream inputStream = null;
        Part filePart = request.getPart("photo");
        if (filePart != null) {
            inputStream = filePart.getInputStream();
        }

        JSONObject json = ImageManager.uploadImage(key, inputStream);

        response.setContentType(" text / json ");
        PrintWriter out = response.getWriter();
        out.println(json);
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String key = request.getParameter("key");
        String id = request.getParameter("id");

        try {

            JSONObject json = ImageManager.retreiveImage(key, id);
            BufferedOutputStream bos = new BufferedOutputStream(response.getOutputStream());
            bos.write(((Blob) json.get("data")).getBinaryStream().readAllBytes());
            bos.flush();
            bos.close();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
}