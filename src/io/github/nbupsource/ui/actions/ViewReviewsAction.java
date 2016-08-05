package io.github.nbupsource.ui.actions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.Base64;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.netbeans.api.progress.ProgressHandle;
import org.netbeans.api.progress.ProgressHandleFactory;
import org.openide.*;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionRegistration;
import org.openide.util.Exceptions;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.IOProvider;
import org.openide.windows.InputOutput;

@ActionID(
        category = "Team",
        id = "io.github.nbupsource.ui.actions.ViewReviewsAction"
)
@ActionRegistration(
        displayName = "#CTL_ViewReviewsAction"
)
@ActionReference(path = "Menu/Versioning", position = 0, separatorBefore = -50, separatorAfter = 50)
@Messages("CTL_ViewReviewsAction=Reviews")
public final class ViewReviewsAction implements ActionListener {

    private static final String REVIEWS_SUBPATH = "/~rpc/getRevisionsList";

    private static final String DEFAULT_USER = "maidaniuk";
    private static final String DEFAULT_PASS = "******";

    @Override
    public void actionPerformed(ActionEvent e) {
        ProgressHandle handle = ProgressHandleFactory.createHandle("Requesting upsource reviews...");

        String txt = "Upsource URL: ";
        String title = "Input URL to Upsource web server";

        NotifyDescriptor.InputLine urlInput = new NotifyDescriptor.InputLine(txt, title);
        urlInput.setInputText("http://your-upsource-host");
        Object result = DialogDisplayer.getDefault().notify(urlInput);
        if (result != NotifyDescriptor.OK_OPTION) {
            return;
        }
        String upsorceBaseUrl = urlInput.getInputText();

        HttpPost httpPost;
        try {
            handle.start();
            httpPost = new HttpPost(upsorceBaseUrl + REVIEWS_SUBPATH);
            String auth = DEFAULT_USER + ":" + DEFAULT_PASS;
            byte[] encodedAuth = Base64.getEncoder().encode(auth.getBytes(Charset.forName("UTF-8")));
            String authHeader = "Basic " + new String(encodedAuth);
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, authHeader);
            httpPost.setEntity(new StringEntity("{\"projectId\":\"crx-platform\", \"limit\":50}"));

            CloseableHttpClient client = HttpClients.createDefault();
            CloseableHttpResponse response = client.execute(httpPost);
            
            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode == HttpStatus.SC_OK) {
                printOutResponse(response.getEntity().getContent());
            }
            else {
                Exceptions.printStackTrace(
                        new IllegalStateException("Some problem with connection. HTTP code: " + statusCode));
            }
        }
        catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
        finally {
            handle.finish();
        }
    }

    public void printOutResponse(InputStream is) throws IOException {
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String answer = readAll(rd);
            NotifyDescriptor nd = new NotifyDescriptor.Message(answer, NotifyDescriptor.INFORMATION_MESSAGE);
            DialogDisplayer.getDefault().notify(nd);
            InputOutput io = IOProvider.getDefault().getIO("Upsource Reviews Output", true);
            io.getOut().println(answer);
            io.getOut().close();
        }
        finally {
            is.close();
        }
    }

    private String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }

}
