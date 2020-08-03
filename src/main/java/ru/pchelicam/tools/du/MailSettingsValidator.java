package ru.pchelicam.tools.du;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import java.io.File;

/**
 * Check if inputted mails settings are all not null
 */
public class MailSettingsValidator {
    public static MailSettings loadSettings() {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(MailSettings.class);
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

            if ((new File(System.getProperty("user.dir") + "/IDiskSettings.xml")).exists()) {
                MailSettings mailSettings = (MailSettings) unmarshaller.unmarshal(new File(System.getProperty("user.dir") + "/IDiskSettings.xml"));
                if (mailSettings.getEmail().isEmpty() || mailSettings.getUsername().isEmpty()
                        || mailSettings.getPassword().isEmpty() || mailSettings.getHost().isEmpty()
                        || mailSettings.getPort().isEmpty() || mailSettings.getEncryption().isEmpty()
                        || mailSettings.getAuth().isEmpty() || mailSettings.getStarttls_enable().isEmpty()) {
                    return null;
                } else {
                    return mailSettings;
                }
            } else {
                DiskLogger.info("There is no file .xml in project directory, so email will not be sent.");
            }
        } catch (Exception e) {
            DiskLogger.error(e.getMessage(), e);
            return null;
        }
        return null;
    }
}
