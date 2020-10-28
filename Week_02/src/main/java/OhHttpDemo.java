import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * project_name:    study
 * package :        PACKAGE_NAME
 * describe :
 *
 * @author :        Luo
 * creat_date :     2020/10/28 16:57
 */
public class OhHttpDemo {

    private static final Logger LOGGER = LoggerFactory.getLogger(OhHttpDemo.class);

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .get()
                .url("http://127.0.0.1:8808/test")
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                LOGGER.info("failed", e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                LOGGER.info("success", response.body().string());
            }
        });
    }
}
