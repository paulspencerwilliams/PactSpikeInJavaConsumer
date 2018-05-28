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

import java.io.IOException;

import static org.hamcrest.Matchers.containsString;
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
        when(authClient.login("paul", "secret")).thenReturn(new LoginResponse(new User(123, "paul", "Paul", "Williams")));

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

    @Test
    public void registrationFormUsernameTooShort() throws IOException {
        Page page = wc.getPage("/registrationForm");
        assertThat(page.isHtmlPage(), is(true));

        HtmlElement body = ((HtmlPage)page).getBody();
        body.<HtmlTextInput>querySelector("#txtUsername").setText("abc");

        Page returnedForm = body.<HtmlSubmitInput>querySelector("#btnRegister").click();
        assertThat(returnedForm.isHtmlPage(), is(true));
        assertThat(((HtmlPage) returnedForm).querySelector("#errorUsername").getTextContent(), containsString("size must be between 4 and 30"));
    }

    @Test
    public void registrationFormUsernameTooLong() throws IOException {
        Page page = wc.getPage("/registrationForm");
        assertThat(page.isHtmlPage(), is(true));

        HtmlElement body = ((HtmlPage)page).getBody();
        body.<HtmlTextInput>querySelector("#txtUsername").setText("abcdefghijklmnopqrstuvwxyzabcde");

        Page returnedForm = body.<HtmlSubmitInput>querySelector("#btnRegister").click();
        assertThat(returnedForm.isHtmlPage(), is(true));
        assertThat(((HtmlPage) returnedForm).querySelector("#errorUsername").getTextContent(), containsString("size must be between 4 and 30"));
    }

    @Test
    public void registrationFormAgeEmpty() throws IOException {
        Page page = wc.getPage("/registrationForm");
        assertThat(page.isHtmlPage(), is(true));

        HtmlElement body = ((HtmlPage)page).getBody();

        Page returnedForm = body.<HtmlSubmitInput>querySelector("#btnRegister").click();
        assertThat(returnedForm.isHtmlPage(), is(true));
        assertThat(((HtmlPage) returnedForm).querySelector("#errorAge").getTextContent(), containsString("must not be null"));
    }

    @Test
    public void registrationFormAgeContainsAlpha() throws IOException {
        Page page = wc.getPage("/registrationForm");
        assertThat(page.isHtmlPage(), is(true));

        HtmlElement body = ((HtmlPage)page).getBody();
        body.<HtmlTextInput>querySelector("#txtAge").setText("a");

        Page returnedForm = body.<HtmlSubmitInput>querySelector("#btnRegister").click();
        assertThat(returnedForm.isHtmlPage(), is(true));
        assertThat(((HtmlPage) returnedForm).querySelector("#errorAge").getTextContent(), containsString("must not be null"));
    }

    @Test
    public void registrationFormPasswordTooShort() throws IOException {
        Page page = wc.getPage("/registrationForm");
        assertThat(page.isHtmlPage(), is(true));

        HtmlElement body = ((HtmlPage)page).getBody();
        body.<HtmlPasswordInput>querySelector("#txtPassword").setText("abcde");

        Page returnedForm = body.<HtmlSubmitInput>querySelector("#btnRegister").click();
        assertThat(returnedForm.isHtmlPage(), is(true));
        assertThat(((HtmlPage) returnedForm).querySelector("#errorPassword").getTextContent(), containsString("size must be between 6 and 128"));
    }

    @Test
    public void registrationFormPasswordTooLong() throws IOException {
        Page page = wc.getPage("/registrationForm");
        assertThat(page.isHtmlPage(), is(true));

        HtmlElement body = ((HtmlPage)page).getBody();
        body.<HtmlPasswordInput>querySelector("#txtPassword").setText("lskjfsdfsfsdffsdf");

        Page returnedForm = body.<HtmlSubmitInput>querySelector("#btnRegister").click();
        assertThat(returnedForm.isHtmlPage(), is(true));
        assertThat(((HtmlPage) returnedForm).querySelector("#errorPassword").getTextContent(), containsString("size must be between 6 and 128"));
    }
}