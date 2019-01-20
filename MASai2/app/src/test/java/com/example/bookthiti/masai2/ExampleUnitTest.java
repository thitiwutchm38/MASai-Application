package com.example.bookthiti.masai2;

import com.example.bookthiti.masai2.devicediscoveryscreen.DeviceDiscoveryModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.CVEModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.DeviceModel;
import com.example.bookthiti.masai2.devicediscoveryscreen.device.ServiceModel;
import com.google.gson.GsonBuilder;

import org.junit.Test;

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
        String json = "{\"hostStats\":{\"up\":1, \"down\":255, \"total\":256}, \"timeStats\":{\"startTime\":\"Wednesday\", \"finishTime\":\"Thursday\", \"elapsed\":4.0}, \"hosts\":[{\n" +
                "    \"status\": \"up\",\n" +
                "    \"ipv4\": \"192.168.1.1\",\n" +
                "    \"deviceType\": \"general purpose\",\n" +
                "    \"osName\": \"Linux 2.6.23 - 2.6.38\",\n" +
                "    \"osVendor\": \"Linux\",\n" +
                "    \"osCpe\": \"cpe:/o:linux:linux_kernel:2.6\",\n" +
                "    \"services\": [\n" +
                "        {\n" +
                "            \"port\": \"22\",\n" +
                "            \"protocol\": \"tcp\",\n" +
                "            \"state\": \"open\",\n" +
                "            \"name\": \"ssh\",\n" +
                "            \"product\": \"Dropbear sshd\",\n" +
                "            \"version\": \"2017.75\",\n" +
                "            \"cpe\": [\n" +
                "                \"cpe:/a:matt_johnston:dropbear_ssh_server:2017.75\",\n" +
                "                \"cpe:/o:linux:linux_kernel\"\n" +
                "            ],\n" +
                "            \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "            \"port\": \"23\",\n" +
                "            \"protocol\": \"tcp\",\n" +
                "            \"state\": \"open\",\n" +
                "            \"name\": \"telnet\",\n" +
                "            \"product\": \"BusyBox telnetd\",\n" +
                "            \"version\": \"1.14.0 or later\",\n" +
                "            \"cpe\": [\n" +
                "                \"cpe:/a:busybox:busybox:1.14.0 or later\"\n" +
                "            ],\n" +
                "            \"cves\": [\n" +
                "                {\n" +
                "                    \"id\": \"CVE-2011-2716\",\n" +
                "                    \"description\": \"The DHCP client (udhcpc) in BusyBox before 1.20.0 allows remote DHCP servers to execute arbitrary commands via shell metacharacters in the (1) HOST_NAME, (2) DOMAIN_NAME, (3) NIS_DOMAIN, and (4) TFTP_SERVER_NAME host name options.\",\n" +
                "                    \"severity\": {\n" +
                "                        \"severity\": \"moderate\",\n" +
                "                        \"topVulnerable\": false,\n" +
                "                        \"topAlert\": false,\n" +
                "                        \"cvss2\": [\n" +
                "                            {\n" +
                "                                \"accessComplexity\": \"high\",\n" +
                "                                \"accessVector\": \"adjacent_network\",\n" +
                "                                \"authentication\": \"none\",\n" +
                "                                \"availability\": \"complete\",\n" +
                "                                \"base\": \"6.8\",\n" +
                "                                \"confidentiality\": \"complete\",\n" +
                "                                \"exploitability\": \"3.2\",\n" +
                "                                \"impact\": \"10.0\",\n" +
                "                                \"integrity\": \"complete\",\n" +
                "                                \"vector\": \"AV:A/AC:H/Au:N/C:C/I:C/A:C\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    }\n" +
                "                },\n" +
                "                {\n" +
                "                    \"id\": \"CVE-2013-1813\",\n" +
                "                    \"description\": \"util-linux/mdev.c in BusyBox before 1.21.0 uses 0777 permissions for parent directories when creating nested directories under /dev/, which allows local users to have unknown impact and attack vectors.\",\n" +
                "                    \"severity\": {\n" +
                "                        \"severity\": \"high\",\n" +
                "                        \"topVulnerable\": false,\n" +
                "                        \"topAlert\": false,\n" +
                "                        \"cvss2\": [\n" +
                "                            {\n" +
                "                                \"accessComplexity\": \"low\",\n" +
                "                                \"accessVector\": \"local\",\n" +
                "                                \"authentication\": \"none\",\n" +
                "                                \"availability\": \"complete\",\n" +
                "                                \"base\": \"7.2\",\n" +
                "                                \"confidentiality\": \"complete\",\n" +
                "                                \"exploitability\": \"3.9\",\n" +
                "                                \"impact\": \"10.0\",\n" +
                "                                \"integrity\": \"complete\",\n" +
                "                                \"vector\": \"AV:L/AC:L/Au:N/C:C/I:C/A:C\"\n" +
                "                            }\n" +
                "                        ]\n" +
                "                    }\n" +
                "                }\n" +
                "            ]\n" +
                "        },\n" +
                "        {\n" +
                "            \"port\": \"80\",\n" +
                "            \"protocol\": \"tcp\",\n" +
                "            \"state\": \"open\",\n" +
                "            \"name\": \"http\",\n" +
                "            \"cpe\": [],\n" +
                "            \"cves\": []\n" +
                "        },\n" +
                "        {\n" +
                "            \"port\": \"1900\",\n" +
                "            \"protocol\": \"tcp\",\n" +
                "            \"state\": \"open\",\n" +
                "            \"name\": \"upnp\",\n" +
                "            \"product\": \"Portable SDK for UPnP devices\",\n" +
                "            \"version\": \"1.6.19\",\n" +
                "            \"cpe\": [\n" +
                "                \"cpe:/o:linux:linux_kernel:2.6.36\"\n" +
                "            ],\n" +
                "            \"cves\": []\n" +
                "        }\n" +
                "    ],\n" +
                "    \"mac\": \"0C:80:63:C2:BB:B6\"\n" +
                "}]}";
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(DeviceModel.class, new DeviceModel.DeviceModelDeserializer());
        gsonBuilder.registerTypeAdapter(ServiceModel.class, new ServiceModel.ServiceModelDeserializer());
        gsonBuilder.registerTypeAdapter(CVEModel.Severity.class, new CVEModel.SeverityDeserializer());
        DeviceDiscoveryModel deviceDiscoveryModel = gsonBuilder.create().fromJson(json, DeviceDiscoveryModel.class);
        assertEquals(deviceDiscoveryModel.getHostStats().getTotal(), 256);
        assertEquals(deviceDiscoveryModel.getTimeStats().getElapsed(), 4.0, 0.0);
        System.out.println(deviceDiscoveryModel.getHosts().get(0).getIpAddress());
        System.out.println(deviceDiscoveryModel.getHosts().get(0).getServiceModels().get(1).getCves().get(0).getDescription());
    }
}