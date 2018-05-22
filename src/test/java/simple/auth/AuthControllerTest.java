package simple.auth;

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private WebClient wc;

    @MockBean
    AuthClient authClient;

    @Test
    public void successfulLogin() throws Exception {
        when(authClient.login("paul", "secret")).thenReturn(new User(123, "paul", "Paul", "Williams"));

        Page page = wc.getPage("/loginForm");
        assertThat(page.isHtmlPage(), is(true));

        HtmlElement body = ((HtmlPage)page).getBody();
        body.<HtmlTextInput>querySelector("#txtUsername").setText("paul");

        body.<HtmlPasswordInput>querySelector("#txtPassword").setText("secret");

        Page homepage = body.<HtmlSubmitInput>querySelector("#btnLogin").click();

        verify(authClient).login("paul", "secret");
        assertThat(homepage.isHtmlPage(), is(true));
        assertThat(((HtmlPage)homepage).getBody().querySelector("p").getTextContent(), is("Hi, Paul Williams"));
    }
}