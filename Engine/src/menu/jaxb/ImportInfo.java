package menu.jaxb;


import scheme3.generateClasses.RizpaStockExchangeDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ImportInfo {

    private final static String PACKAGE_NAME_FOR_JAXB = "scheme3.generateClasses";

    public RizpaStockExchangeDescriptor unmarshall(InputStream in) throws JAXBException {
        //InputStream inputStream = new FileInputStream(new File(in));
        RizpaStockExchangeDescriptor descriptor = deserializeFrom(in);

        return descriptor;
    }

    private static RizpaStockExchangeDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(PACKAGE_NAME_FOR_JAXB);
        Unmarshaller unmarshaller = jc.createUnmarshaller();
        return (RizpaStockExchangeDescriptor) unmarshaller.unmarshal(in);
    }

    @Override
    public String toString() {
        return "ImportInfo{}" ;
    }
}
