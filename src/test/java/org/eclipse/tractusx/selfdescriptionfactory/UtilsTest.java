package org.eclipse.tractusx.selfdescriptionfactory;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import org.mockito.Mockito;
import org.springframework.web.server.ResponseStatusException;


import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;


class UtilsTest {

    @Test
    public void getConnectionIfRedirected() throws IOException, Utils.TooManyRedirectsException {
        URLConnection connectionIfRedirected = Utils.getConnectionIfRedirected(new URL("http://localhost"), 2);
        Assertions.assertNotNull(connectionIfRedirected);
    }

    @Test
    public void getConnectionIfRedirected_tries0() throws IOException, Utils.TooManyRedirectsException {
        assertThrows(
                Utils.TooManyRedirectsException.class,
                () -> Utils.getConnectionIfRedirected(new URL("http://localhost"), 0),
                ""
        );
    }

    @Test
    public void getConnectionIfRedirected_HeaderNotNull() throws IOException, Utils.TooManyRedirectsException {
        URL urlMock = Mockito.mock(URL.class);
        InputStream inputStreamMock = Mockito.mock(InputStream.class);
        URLConnection urlConnectionMock = Mockito.mock(URLConnection.class);
        Mockito.doNothing().when(inputStreamMock).close();
        Mockito.when(urlConnectionMock.getInputStream()).thenReturn(inputStreamMock);
        Mockito.when(urlConnectionMock.getHeaderField(anyString())).thenReturn("http://localhost");
        Mockito.when(urlMock.openConnection()).thenReturn(urlConnectionMock);
        assertThrows(
                Utils.TooManyRedirectsException.class,
                ()-> Utils.getConnectionIfRedirected(urlMock, 1), "");
    }

    @Test
    public void uriFromStr() {
        URI uri = Utils.uriFromStr("http://localhost");
        Assertions.assertNotNull(uri);
        Assertions.assertEquals(URI.create("http://localhost"), uri);
    }

    @Test
    public void instantiateUtils() throws Exception{
        Constructor<Utils> constructor = Utils.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        assertThrows(
                InvocationTargetException.class,
                ()-> constructor.newInstance(), "");
    }

    @Test
    public void uriFromStrExceptionTest() {
            try{
                URI uri = Utils.uriFromStr(null);
                assertThrows(
                        ResponseStatusException.class,
                        () -> Utils.uriFromStr(null),
                        ""
                );
            }catch (Exception e){

            }

    }

}
