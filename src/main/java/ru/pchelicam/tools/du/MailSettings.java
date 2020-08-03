package ru.pchelicam.tools.du;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlRootElement(name = "mailsettings")
@XmlType(propOrder = {"email", "username", "password", "host", "port", "encryption", "auth", "starttls_enable"})
/**
 * Contains data for sending emails to user
 */
public class MailSettings {
    @XmlElement
    private String email;
    @XmlElement
    private String username;
    @XmlElement
    private String password;
    @XmlElement
    private String host;
    @XmlElement
    private String port;
    @XmlElement
    private String encryption;
    @XmlElement
    private String auth;
    @XmlElement
    private String starttls_enable;

    public MailSettings() {

    }

    public MailSettings(String email, String username, String password, String host, String port, String encryption, String auth, String starttls_enable) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.encryption = encryption;
        this.auth = auth;
        this.starttls_enable = starttls_enable;
    }


    public String getEmail() {
        return email;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getEncryption() {
        return encryption;
    }

    public String getAuth() {
        return auth;
    }

    public String getStarttls_enable() {
        return starttls_enable;
    }
}