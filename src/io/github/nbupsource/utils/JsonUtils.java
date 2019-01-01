package io.github.nbupsource.utils;

import io.github.nbupsource.dto.UpsourceProperties;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.openide.util.Exceptions;

public final class JsonUtils {

    private JsonUtils() {
    }

    public static List<UpsourceProperties> readPropertiesFromFile(File propertiesFile) {
        JSONParser parser = new JSONParser();
        try {
            JSONArray array = (JSONArray) parser.parse(new FileReader(propertiesFile));
            List<UpsourceProperties> properties = new ArrayList<>();

            for (Object o : array) {
                JSONObject property = (JSONObject) o;

                String projectName = (String) property.get("projectName");
                String upsourceUrl = (String) property.get("upsourceUrl");
                String userName = (String) property.get("userName");
                String passwordHash = (String) property.get("passwordHash");
                UpsourceProperties uProperties = new UpsourceProperties(projectName, upsourceUrl, 
                                                                        userName, passwordHash);
                properties.add(uProperties);
            }
            return properties;
        }
        catch (IOException | ParseException ex) {
            return null;
        }
    }
    
    public static void writePropertiesToFile(List<UpsourceProperties> properties, File propertiesFile) {

        if (properties != null && !properties.isEmpty()) {
            JSONArray list = new JSONArray();

            for (UpsourceProperties prop : properties) {
                JSONObject obj = new JSONObject();
                obj.put("projectName", prop.getProjectName());
                obj.put("upsourceUrl", prop.getUpsourceUrl());
                obj.put("userName", prop.getUserName());
                obj.put("passwordHash", prop.getPasswordHash());
                
                list.add(obj);
            }

            try (FileWriter file = new FileWriter(propertiesFile)) {
                file.write(list.toJSONString());
                file.flush();
            }
            catch (IOException ex) {
                Exceptions.printStackTrace(ex);
            }
        }
    }

}
