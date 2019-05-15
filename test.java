//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package est;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(
        name = "EstelamResult3",
        propOrder = {"deathDate", "exceptionMessage"}
)
@XmlSeeAlso({EstelamResult4.class})
public class EstelamResult3 extends EstelamResult {
    protected String deathDate;
    protected String exceptionMessage;

    public EstelamResult3() {
    }

    public String getDeathDate() {
        return this.deathDate;
    }

    public void setDeathDate(String value) {
        this.deathDate = value;
    }

    public String getExceptionMessage() {
        return this.exceptionMessage;
    }

    public void setExceptionMessage(String value) {
        this.exceptionMessage = value;
    }
}
