/**
 * The class 'Unicode' is a util class, which is associated to the class 'Scriptlet'.
 * Both classes are taken advantages in producing scriptlet in jasper reports.
 */
public class Unicode
{
    public String toUnicode(String originalText)
    {
        String outPutText = "";

        for (int i = 0; i < originalText.length(); i++) {
            if ((originalText.substring(i, i + 1).compareTo("0") >= 0) && (originalText.substring(i, i + 1).compareTo("9") <= 0))
            {
                int ii = Character.getNumericValue(originalText.charAt(i)) + 1632;

                char ch = (char)ii;
                outPutText = outPutText + Character.toString(ch);
            } else {
                outPutText = outPutText + originalText.charAt(i);
            }

        }

        return outPutText == null ? "" : outPutText;
    }

    public String toAnsii(String unicodeText) {
        String outPutText = new String();

        char c = '\000';

        for (int i = 0; i < unicodeText.length(); ) {
            c = unicodeText.charAt(i);

            if (c == '\\') {
                i++;
                c = unicodeText.charAt(i);

                switch (c) {
                    case 'u':
                        outPutText = outPutText + (char)Integer.parseInt(unicodeText.substring(i + 1, i + 5).toString(), 16);

                        i += 5;

                        break;
                }
            } else {
                outPutText = outPutText + c;
                i++;
            }

        }

        return outPutText == null ? "" : outPutText;
    }
}