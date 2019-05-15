package com.example.bookthiti.masai2;

import com.example.bookthiti.masai2.devicediscoveryscreen.DeviceDiscoveryModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.CVEModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.DeviceModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.ServiceModel;
import com.google.gson.GsonBuilder;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void whenDeserializingToNestedObjectsThenCorrect() {
        String json = "{\n" +
                "  \"timeStats\": {\n" +
                "    \"startTime\": \"Sun Jan 20 23:47:33 2019\",\n" +
                "    \"finishTime\": \"Sun Jan 20 23:49:16 2019\",\n" +
                "    \"elapsed\": \"106.65\"\n" +
                "  },\n" +
                "  \"hostStats\": {\n" +
                "    \"up\": \"8\",\n" +
                "    \"down\": \"248\",\n" +
                "    \"total\": \"256\"\n" +
                "  },\n" +
                "  \"hosts\": [\n" +
                "    {\n" +
                "      \"status\": \"up\",\n" +
                "      \"ipv4\": \"192.168.1.1\",\n" +
                "      \"deviceType\": \"general purpose\",\n" +
                "      \"osName\": \"Linux 2.6.23\",\n" +
                "      \"osVendor\": \"Linux\",\n" +
                "      \"osCpe\": [\"cpe:/o:linux:linux_kernel:2.6.23\"],\n" +
                "      \"services\": [\n" +
                "        {\n" +
                "          \"port\": \"21\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"ftp\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"22\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"ssh\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"23\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"telnet\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"53\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"domain\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"80\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"http\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"443\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"https\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"990\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"ftps\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        }\n" +
                "      ],\n" +
                "      \"mac\": \"8C:15:C7:22:CB:7F\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"status\": \"up\",\n" +
                "      \"ipv4\": \"192.168.1.4\",\n" +
                "      \"deviceType\": null,\n" +
                "      \"osName\": null,\n" +
                "      \"osVendor\": null,\n" +
                "      \"osCpe\": null,\n" +
                "      \"services\": [],\n" +
                "      \"mac\": \"BC:83:85:13:70:CE\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"status\": \"up\",\n" +
                "      \"ipv4\": \"192.168.1.5\",\n" +
                "      \"deviceType\": \"general purpose\",\n" +
                "      \"osName\": \"Microsoft Windows Server 2008 SP1 or Windows Server 2008 R2\",\n" +
                "      \"osVendor\": \"Microsoft\",\n" +
                "      \"osCpe\": [\n" +
                "        \"cpe:/o:microsoft:windows_server_2008::sp1\",\n" +
                "        \"cpe:/o:microsoft:windows_server_2008:r2\"\n" +
                "      ],\n" +
                "      \"services\": [\n" +
                "        {\n" +
                "          \"port\": \"135\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"msrpc\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"139\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"netbios-ssn\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"445\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"microsoft-ds\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        }\n" +
                "      ],\n" +
                "      \"mac\": \"D4:6A:6A:5B:CE:AF\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"status\": \"up\",\n" +
                "      \"ipv4\": \"192.168.1.6\",\n" +
                "      \"deviceType\": \"phone\",\n" +
                "      \"osName\": \"Apple iOS 11.0\",\n" +
                "      \"osVendor\": \"Apple\",\n" +
                "      \"osCpe\": [\"cpe:/o:apple:iphone_os:11.0\"],\n" +
                "      \"services\": [\n" +
                "        {\n" +
                "          \"port\": \"83\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"mit-ml-dev\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"458\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"appleqtc\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"777\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"multiling-http\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"1033\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"netinfo\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"1044\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"dcutility\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"1201\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"nucleus-sand\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"1533\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"virtual-places\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"1583\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"simbaexpress\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"1812\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"radius\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"1974\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"drp\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"8400\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"cvd\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"10004\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"emcrmirccd\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"16018\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"unknown\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"40911\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"unknown\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"62078\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"iphone-sync\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"64680\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"filtered\",\n" +
                "          \"name\": \"unknown\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        }\n" +
                "      ],\n" +
                "      \"mac\": \"14:20:5E:9A:D9:69\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"status\": \"up\",\n" +
                "      \"ipv4\": \"192.168.1.7\",\n" +
                "      \"deviceType\": null,\n" +
                "      \"osName\": \"Linux 2.6.32 - 3.10\",\n" +
                "      \"osVendor\": null,\n" +
                "      \"osCpe\": null,\n" +
                "      \"services\": [\n" +
                "        {\n" +
                "          \"port\": \"8008\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"http\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"8009\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"ajp13\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"8443\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"https-alt\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"9000\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"cslistener\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"10001\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"scp-config\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        }\n" +
                "      ],\n" +
                "      \"mac\": \"20:DF:B9:AC:92:10\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"status\": \"up\",\n" +
                "      \"ipv4\": \"192.168.1.9\",\n" +
                "      \"deviceType\": null,\n" +
                "      \"osName\": null,\n" +
                "      \"osVendor\": null,\n" +
                "      \"osCpe\": null,\n" +
                "      \"services\": [],\n" +
                "      \"mac\": \"B4:6B:FC:4C:F5:81\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"status\": \"up\",\n" +
                "      \"ipv4\": \"192.168.1.14\",\n" +
                "      \"deviceType\": \"specialized\",\n" +
                "      \"osName\": \"AVtech Room Alert 26W environmental monitor\",\n" +
                "      \"osVendor\": \"AVtech\",\n" +
                "      \"osCpe\": null,\n" +
                "      \"services\": [\n" +
                "        {\n" +
                "          \"port\": \"8081\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"blackice-icecap\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        }\n" +
                "      ],\n" +
                "      \"mac\": \"3C:F8:62:EB:FD:7A\"\n" +
                "    },\n" +
                "    {\n" +
                "      \"status\": \"up\",\n" +
                "      \"ipv4\": \"192.168.1.13\",\n" +
                "      \"deviceType\": null,\n" +
                "      \"osName\": \"Linux 3.8 - 4.14\",\n" +
                "      \"osVendor\": null,\n" +
                "      \"osCpe\": null,\n" +
                "      \"services\": [\n" +
                "        {\n" +
                "          \"port\": \"22\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"ssh\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"5901\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"vnc-1\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "          \"port\": \"6001\",\n" +
                "          \"protocol\": \"tcp\",\n" +
                "          \"state\": \"open\",\n" +
                "          \"name\": \"X11:1\",\n" +
                "          \"cpe\": [],\n" +
                "          \"cves\": []\n" +
                "        }\n" +
                "      ]\n" +
                "    }\n" +
                "  ]\n" +
                "}";
//        String json = loadJsonFromAsset();
//        System.out.print(json);
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DeviceModel.class, new DeviceModel.DeviceModelDeserializer());
        gsonBuilder.registerTypeAdapter(ServiceModel.class, new ServiceModel.ServiceModelDeserializer());
        gsonBuilder.registerTypeAdapter(CVEModel.Severity.class, new CVEModel.SeverityDeserializer());
        DeviceDiscoveryModel deviceDiscoveryModel = gsonBuilder.create().fromJson(json, DeviceDiscoveryModel.class);
        assertEquals(deviceDiscoveryModel.getHostStats().getTotal(), 256);
        assertEquals(deviceDiscoveryModel.getTimeStats().getElapsed(), 106.65, 0.01);
        System.out.println(deviceDiscoveryModel.getHosts().get(0).getIpAddress());
        System.out.println(deviceDiscoveryModel.getHosts().get(0).getServiceModels().get(1).getCves());
    }

    public String loadJsonFromAsset() {
        StringBuilder sb = new StringBuilder();
        try {
            InputStream in = this.getClass().getClassLoader().getResourceAsStream("nmap_result.json");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in));
            String line = bufferedReader.readLine();
            while(line != null) {
                sb.append(line + "\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();

//            InputStream inputStream = getAssets().open("nmap_result.json");
//            byte[] buffer = new byte[1024];
//            int byteReads;
//            while((byteReads = inputStream.read(buffer)) != -1) {
//                sb.append(new String(buffer, 0, byteReads));
//                Log.i(TAG_INFO, sb.toString());
//            }
//            inputStream.close();
//            inputStream.read(buffer);
//            Log.i(TAG_INFO, sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
//        Log.i(TAG_INFO, sb.toString());
        return sb.toString();
    }
}