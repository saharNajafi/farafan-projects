package gam.nocr.ems.web;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

@RunWith(Parameterized.class)
public class NewExactTest {

    String ccosExactVersions;

    public NewExactTest(String ccosExactVersions) {
        this.ccosExactVersions = ccosExactVersions;
    }

    @Parameterized.Parameters
    public static List<String> generateRandomRecords() {
        List<String> myList = new ArrayList<String>();

        myList.add("2.8.6.10,2.8.6.11,2.8.6.*");
        myList.add("2.7.6. ,2.5.6.66 , 2.8.6.*");
        myList.add("2.s.q. ,2 , 2.*");
        myList.add("2.s.q.* ,2.5.. , 2.");
        myList.add("q.***** ,1.*.* , 2.1");
        myList.add("***** ,1.1.1.***   , *");
        myList.add(".d , .1.1 , *.*");

        return myList;
    }

    @Test
    public void getCompatibleClientVerList() {
        List<String> newVerCode = new ArrayList();
        String beforeChange = new String(ccosExactVersions);
        try {

            ccosExactVersions = removeInvalidItems(ccosExactVersions);
            ccosExactVersions.replaceAll("\\s+", "");
            String[] verCode = ccosExactVersions.split(",");
            String temp;
            for (int m = 0; m < verCode.length; m++) {
                temp = verCode[m].trim();
                if (!temp.isEmpty()) {
                    newVerCode.add(temp);
                }
            }
        } catch (Exception ex) {

        }

        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < newVerCode.size(); i++)
            stringBuilder.append(newVerCode.get(i)).append(",");

        System.out.println(beforeChange + "====>" + stringBuilder.toString());
    }

    private static String removeInvalidItems(String record) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] verCode = record.split(",");
        String temp;
        for (int i = 0; i < verCode.length; i++) {
            temp = verCode[i].trim();
            if (Pattern.compile("^\\d+((\\.\\d+))*$").matcher(temp).matches()
                    ||
                    Pattern.compile("^\\d+((\\.\\d))*+(.\\*)$").matcher(temp).matches()) {
                stringBuilder.append(temp).append(",");
            }
        }

        String validItems = stringBuilder.toString().trim();
        if (validItems.isEmpty())
            return "";

        return validItems.substring(0, validItems.length() - 1);
    }
}
