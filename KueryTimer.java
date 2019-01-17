import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Kashif on 1/17/2019.
 */

public class KueryTimer {

    static Map<String, Float> timeMap = new HashMap<>();
    private static final String COMMA_DELIMITER = ",";
    private static final String NEW_LINE_SEPARATOR = "\n";
    private static final String FILE_HEADER = "url, time";
	static String extraArgs;

    public static void main(String[] args) {

        String fileName = args[0];
        extraArgs = args[1];

        if (fileName == null || fileName.isEmpty()) {
            System.out.println("ileName == null");
        } else {
            readFile(fileName);
            printMap( fileName+"_output.csv" );
        }

    }

    static void readFile(String fileName) {

        System.out.println("readFile="+fileName);
        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(fileName));

            String line = reader.readLine();
            while (line != null) {
                System.out.println("line="+line);
                fetchTime(line);
                line = reader.readLine();
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    static void fetchTime(String line) {

        if (line != null && !line.isEmpty()) {

            try{

                if( line.contains("requrl") && line.contains("difftime") ){
                    System.out.println("fetchTime contains");

                    int colan = line.indexOf(":") + 2;
                    int comma = line.indexOf(',')-1;
                    System.out.println("colan="+colan +", comma=" +comma );

                    String url = line.substring( colan, comma );
					
					if( extraArgs!=null && extraArgs.equals("-d") && url.contains("?") ){
						url = url.substring(0, url.indexOf("?") );
					}
					
                    System.out.println("url="+url);

                    int colan2 = line.indexOf( ':', comma );
                    int comma2 = line.indexOf(',', colan2);
                    System.out.println("colan2="+colan2 +", comma2=" +comma2 );

                    String strTime = line.substring( colan2+1, comma2 );
                    System.out.println("strTime="+strTime);
                    float difftime = Float.parseFloat(strTime);

                    if( timeMap.containsKey(url) ) {
                        System.out.println("timeMap contains");

                        float prevTime = timeMap.get(url);
                        if( difftime > prevTime ){
                            System.out.println("difftime > prevTime ");
                            timeMap.put(url, difftime);
                        }

                    }else{
                        System.out.println("timeMap.put");
                        timeMap.put(url, difftime);
                    }

                }

            }catch (Exception e){
                e.printStackTrace();
            }

        }

    }

    static void printMap(String fileName){

        System.out.println("printMap - "+fileName);
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(fileName);

            //Write the CSV file header
            fileWriter.append(FILE_HEADER.toString());

            //Add a new line separator after the header
            fileWriter.append(NEW_LINE_SEPARATOR);

            for (Map.Entry<String, Float> entry : timeMap.entrySet()) {
                String key = entry.getKey();
                Float value = entry.getValue();

                fileWriter.append( key );
                fileWriter.append(COMMA_DELIMITER);
                fileWriter.append(String.valueOf( value ));
                fileWriter.append(NEW_LINE_SEPARATOR);

            }

            System.out.println("CSV file was created successfully !!!");

        } catch (Exception e) {
            System.out.println("Error in CsvFileWriter !!!");
            e.printStackTrace();
        } finally {

            try {
                if(fileWriter!=null) {
                    fileWriter.flush();
                    fileWriter.close();
                }
            } catch (IOException e) {
                System.out.println("Error while flushing/closing fileWriter !!!");
                e.printStackTrace();
            }

        }

    }


}
