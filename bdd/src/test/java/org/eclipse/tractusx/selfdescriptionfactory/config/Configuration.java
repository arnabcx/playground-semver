package org.eclipse.tractusx.selfdescriptionfactory.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class Configuration {

    private static final String ENV_CREATE_VC_CLIENT = "CREATE_VC_CLIENT";
    private static final String ENV_CREATE_VC_CLIENT_SECRET = "CREATE_VC_CLIENT_SECRET";
    private static final String ENV_DTR_CLIENT = "DTR_CLIENT";
    private static final String ENV_DTR_CLIENT_SECRET = "DTR_CLIENT_SECRET";
    private static final String IDP_HOST = "IDP_HOST";
    public static final String SDF_HOST = "SDF_HOST";
    private static final String XRAY_CLIENT_ID = "XRAY_CLIENT_ID";
    private static final String XRAY_CLIENT_SECRET = "XRAY_CLIENT_SECRET";
    private static final String XRAY_HOST = "XRAY_HOST";
    private static final String XRAY_FLAG = "XRAY_FLAG";

    public static String registerEndpoints() throws IOException {
        return Files.readString(Path.of("src/test/resources/RegisterEndpoints.json"));
    }

    public static String searchEndpoints() throws IOException {
        return Files.readString(Path.of("src/test/resources/SearchEndpoints.json"));
    }

    public static String initialEndpoints() throws IOException {
        return Files.readString(Path.of("src/test/resources/InitialEndpoints.json"));
    }

    private static Configuration configuration;


    private final Map<String, String> env;

    private Configuration() {
        env = System.getenv();
    }


    public static Configuration getInstance() {
        if (configuration == null) {
            configuration = new Configuration();
        }
        return configuration;
    }

    private Map<String, String> getEnv() {
        return env;
    }


    public static String getEnvCreateVcClient() {
        return getInstance().getEnv().get(ENV_CREATE_VC_CLIENT);
    }

    public static String getEnvCreateVcClientSecret() {
        return getInstance().getEnv().get(ENV_CREATE_VC_CLIENT_SECRET);
    }

    public static String getEnvDtrClientId() {
        return getInstance().getEnv().get(ENV_DTR_CLIENT);
    }

    public static String getEnvDtrClientSecret() {
        return getInstance().getEnv().get(ENV_DTR_CLIENT_SECRET);
    }

    public static String getXrayClientId(){
        return getInstance().getEnv().get(XRAY_CLIENT_ID);
    }

    public static String getXrayClientSecret() {
        return getInstance().getEnv().get(XRAY_CLIENT_SECRET);
    }
    public static String getXrayHost() {
        return getInstance().getEnv().get(XRAY_HOST);
    }
    public static String getXrayFlag() {
        return getInstance().getEnv().get(XRAY_FLAG);
    }

    public static String getSdfHost() {
        return getInstance().getEnv().get(SDF_HOST);
    }
    public static String getIdpHost() {
        return getInstance().getEnv().get(IDP_HOST);
    }
}
