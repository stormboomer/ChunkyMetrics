package de.stormboomer.chunky.plugin;

import com.google.gson.JsonObject;
import de.stormboomer.chunky.plugin.models.HardwareInfo;
import de.stormboomer.chunky.plugin.models.MetricStat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MetricDataSender {

    public static String sendPost(HttpURLConnection con, String jsonInputString) throws IOException {
        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        StringBuilder response = new StringBuilder();
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {

            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            //System.out.println(response.toString());
        }
        return response.toString();
    }

    public static void registerPC(HardwareInfo hwinfo) throws IOException {
        if(!MetricPlugin.config.enableMetricsOnline){
            return;
        }
        URL url = new URL(MetricPlugin.config.api + "registerpc/"+hwinfo.os.ComputerID);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");

        String jsonInputString = MetricPlugin.jsonHelperMinified.toJson(hwinfo);
        sendPost(con, jsonInputString);
    }

    public static void registerScene(String ComputerID, long RunNumber, String SceneJson, String chunkyVersion) throws IOException {
        if(!MetricPlugin.config.enableMetricsOnline){
            return;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("SceneJson", SceneJson);
        jsonObject.addProperty("RunNumber", RunNumber);
        jsonObject.addProperty("javaVendor", System.getProperty("java.vendor"));
        jsonObject.addProperty("javaVendorURL", System.getProperty("java.vendor.url"));
        jsonObject.addProperty("javaVersion", System.getProperty("java.version"));
        jsonObject.addProperty("chunkyVersion", chunkyVersion);


        URL url = new URL(MetricPlugin.config.api + "registerscene/"+ComputerID);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");

        String jsonInputString = MetricPlugin.jsonHelperMinified.toJson(jsonObject);
        //Logger.debug("POST SCENE DATA: " + jsonInputString);
        sendPost(con, jsonInputString);
    }

    public static void sendStats( String ComputerID, MetricStat stat) throws IOException {
        if(!MetricPlugin.config.enableMetricsOnline){
            return;
        }
        URL url = new URL(MetricPlugin.config.api + "send_metric/"+ComputerID);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setDoOutput(true);
        con.setRequestMethod("POST");
        con.setRequestProperty("Content-Type", "application/json; utf-8");

        String jsonInputString = MetricPlugin.jsonHelperMinified.toJson(stat);

        try(OutputStream os = con.getOutputStream()) {
            byte[] input = jsonInputString.getBytes("utf-8");
            os.write(input, 0, input.length);
        }
        try(BufferedReader br = new BufferedReader(
                new InputStreamReader(con.getInputStream(), "utf-8"))) {
            StringBuilder response = new StringBuilder();
            String responseLine = null;
            while ((responseLine = br.readLine()) != null) {
                response.append(responseLine.trim());
            }
            //System.out.println(response.toString());
        }
    }
}
