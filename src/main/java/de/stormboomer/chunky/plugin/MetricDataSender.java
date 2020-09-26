package de.stormboomer.chunky.plugin;

import de.stormboomer.chunky.plugin.models.HardwareInfo;
import de.stormboomer.chunky.plugin.models.MetricStat;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MetricDataSender {

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
