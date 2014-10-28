package ru.friends.model.VK;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.methods.PostMethod;
import java.io.IOException;

public class VKRequestsManager {

    private static final String cookie = "";
    private HttpClient client;
    private PostMethod postMethod;

    public VKRequestsManager() {
        client = new HttpClient();
        postMethod = new PostMethod();

        postMethod.addRequestHeader("Referer", "http://vk.com/friends?section=all");
        postMethod.addRequestHeader("Host", "vk.com");
        postMethod.addRequestHeader("ru.friends.User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:32.0) Gecko/20100101 Firefox/32.0");
        postMethod.addRequestHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        postMethod.addRequestHeader("Accept-Language", "ru-RU,ru;q=0.8,en-US;q=0.5,en;q=0.3");
        //postMethod.addRequestHeader("Accept-Encoding", "gzip, deflate");
        postMethod.addRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        postMethod.addRequestHeader("X-Requested-With", "XMLHttpRequest");
        postMethod.addRequestHeader("Cookie", cookie);
        //postMethod.addRequestHeader("Content-Length", "45");
        postMethod.addRequestHeader("Pragma", "no-cache");
        postMethod.addRequestHeader("Cache-control", "no-cache");
    }

    public String executeReqest(String url) throws IOException {
        postMethod.setURI(new URI(url, true, postMethod.getParams().getUriCharset()));
        int status = client.executeMethod(postMethod);
        System.out.println("STATUS = " + status);
        return postMethod.getResponseBodyAsString();
    }


}
