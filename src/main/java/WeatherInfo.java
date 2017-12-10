import model.Weather;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Regions;

import java.io.*;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class WeatherInfo {
    private static final String baseUrl = "https://www.metoffice.gov.uk/climate/uk/summaries/datasets#yearOrdered";
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "region_code,weather_param,year,key,value";
    private static List<Weather> weatherList = new ArrayList<>();
    private static final File basePath;

    static {
        basePath = new File("C:\\Weather Data");
        if (!basePath.exists())
            basePath.mkdir();
    }

    public static void main(String[] args) throws IOException {
        downloadAllFiles();
        parseAllFiles();
        writeToCsvFile(basePath.getPath() + File.separator + "weather.csv");
    }

    private static void parseAllFiles() {
        for (File file : Objects.requireNonNull(basePath.listFiles())) {

            try {
                BufferedReader br = new BufferedReader(new FileReader(file));
                String fileRead = br.readLine();
                String[] splitTitle = fileRead.split(" ");
                String regionCode = splitTitle[0];
                String weatherParam = getWeatherParam(splitTitle);
                for (int i = 0; i <= 7; i++)
                    fileRead = br.readLine();

                while (fileRead != null) {
                    String trimedString = fileRead.trim().replaceAll(" +", " ");
                    String[] tokenize = trimedString.split(" ");
                    for (int i = 0; i <= tokenize.length; i += 2)
                        if (i < tokenize.length)
                            weatherList.add(new Weather(regionCode, weatherParam, Integer.parseInt(tokenize[i + 1]), getMonth(i), Float.parseFloat(tokenize[i])));
                    fileRead = br.readLine();
                }
                br.close();
            } catch (FileNotFoundException fnfe) {
                System.out.println("File not found");
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        }
    }

    private static void downloadAllFiles() throws IOException {
        Document doc = Jsoup.connect(baseUrl).get();
        Elements links = doc.select("a[href]");
        for (Element l : links)
            if (l.attr("abs:href").endsWith(".txt"))
                for (String region : getRegionList())
                    if (l.attr("title").equals(region))
                        dowloadFile(l, region);
    }

    private static String getWeatherParam(String[] splitTitle) {
        StringBuilder key = new StringBuilder();
        for (int i = 1; i < splitTitle.length; i++)
            key.append(splitTitle[i]).append(" ");
        return getShortForm(key.toString());
    }

    private static void dowloadFile(Element l, String city) {
        try {
            URL website = new URL(getSecuredUrl(l.attr("abs:href")));
            String filepath = basePath.getPath() + File.separator + city + ".txt";
            ReadableByteChannel channel = Channels.newChannel(website.openStream());
            FileOutputStream stream = new FileOutputStream(filepath);
            stream.getChannel().transferFrom(channel, 0, Long.MAX_VALUE);

            System.out.println("Download successfull.");
        } catch (Exception e) {
            System.out.println("Download was not successfull.");
        }
    }

    private static String getSecuredUrl(String url) {
        return url.substring(0, 4) + "s" + url.substring(4, url.length());
    }


    private static List<String> getRegionList() {
        List<String> regionList = new ArrayList<>();
        for (Regions region : Regions.values()) {
            regionList.add(region + " Ranked Tmax");
            regionList.add(region + " Ranked Tmin");
            regionList.add(region + " Ranked Tmean");
            regionList.add(region + " Ranked Sunshine");
            regionList.add(region + " Ranked Rainfall");
        }
        return regionList;
    }

    private static String getShortForm(String value) {
        if (value.contains("Maximum Temperature"))
            return "Max Temp";
        else if (value.contains("Minimum Temperature"))
            return "Min Temp";
        else if (value.contains("Rainfall"))
            return "Rainfall";
        else if (value.contains("Sunshine"))
            return "Sunshine";
        else if (value.contains("Mean Temperature"))
            return "Mean Temp";
        return "";
    }

    private static String getMonth(int i) {
        switch (i) {
            case 0:
                return "JAN";
            case 2:
                return "FEB";
            case 4:
                return "MAR";
            case 6:
                return "APR";
            case 8:
                return "MAY";
            case 10:
                return "JUN";
            case 12:
                return "JUL";
            case 14:
                return "AUG";
            case 16:
                return "SEP";
            case 18:
                return "OCT";
            case 20:
                return "NOV";
            case 22:
                return "DEC";
            case 24:
                return "WIN";
            case 26:
                return "SPR";
            case 28:
                return "SUM";
            case 30:
                return "AUT";
            case 32:
                return "ANN";
            default:
                return "";
        }
    }

    private static void writeToCsvFile(String fileName) {
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);
            fileWriter.append(FILE_HEADER);
            fileWriter.append(NEW_LINE_SEPARATOR);
            for (Weather weather : weatherList) {
                fileWriter.append(String.valueOf(weather.getRegionCode()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(weather.getWeatherParam());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(weather.getYear()));
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(weather.getKey());
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf(weather.getValue()));
                fileWriter.append(NEW_LINE_SEPARATOR);
            }
            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in writing to CSV file !!!");
            e.printStackTrace();
        } finally {
            try {
                assert fileWriter != null;
                fileWriter.flush();
                fileWriter.close();
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }
        }
    }
}
