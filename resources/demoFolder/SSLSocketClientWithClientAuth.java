/*
* Q&D java demo for communicating with kortathjonustan's RPCS
*
* Gunnar Mar Gunnarsson 9. Dec 2003
*/
import java.net.*;
import java.io.*;
import javax.net.ssl.*;
import javax.security.cert.X509Certificate;
import java.security.KeyStore;

public class SSLSocketClientWithClientAuth 
{
  public static void main(String[] args) throws Exception 
  {
    // Parameters for host connection
    String strHost = new String("test.kortathjonustan.is");
    int iPort = 8443;
    String strPath = new String("https://test.kortathjonustan.is/rpc/RequestAuthorisation");
    int iRequestTimeout = 60000;
    
    // Parameters for user authentication on server
    String strUser = "ticket01";
    String strPwd = "test";
    
    // Parameters for client authentication keystore
    String strKeystore = "c:\\demo\\testkeys.jks";
    String strKeystorePass = "changeit";
    
    // Parameters for server authentication truststore
    String strTestTrustStore = "c:\\demo\\cacerts_test";
    String strTestTrustStorePass = "changeit";
    
    // Test indicator
    boolean bTestServer = true;

    System.out.println("Host = " + strHost);
    System.out.println("Port = " + iPort);
    System.out.println("Path = " + strPath);

    long lStartTime = System.currentTimeMillis();
    try 
    {
      SSLSocketFactory factory = null;
      try 
      {
        SSLContext oSSLCtx;
        KeyManagerFactory oSSLKMF;
        KeyStore oSSLKS;
        char[] passphrase = strKeystorePass.toCharArray();

        // Set the truststore that contains the root certificates used
        // to authenticate the test server.
        // The production server uses Verisign certificates and therefore
        // uses the default "cacerts" file that is located in the 
        // Java Runtime folder /jre/lib/security
        if (bTestServer)
        {
          System.setProperty("javax.net.ssl.trustStore", strTestTrustStore);
          System.setProperty("javax.net.ssl.trustStorePassword", strTestTrustStorePass);
        }

        oSSLCtx = SSLContext.getInstance("TLS");
        oSSLKMF = KeyManagerFactory.getInstance("SunX509");
        oSSLKS = KeyStore.getInstance("JKS");

        oSSLKS.load(new FileInputStream(strKeystore), passphrase);

        oSSLKMF.init(oSSLKS, passphrase);
        oSSLCtx.init(oSSLKMF.getKeyManagers(), null, null);

        factory = oSSLCtx.getSocketFactory();
      } 
      catch (Exception e) 
      {
        throw new IOException(e.getMessage());
      }

      SSLSocket socket = (SSLSocket)factory.createSocket(strHost, iPort);

      // Do the SSL handshake
      socket.startHandshake();

      // Do the SSL handshake
      String strPostData= new String("site=13&user=ticket01&pwd=test&d41=00010004&d42=90000001");

      PrintWriter out = new PrintWriter(
        new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
      // Write the http header (w3c spec.)
      out.print("POST " + strPath + " HTTP/1.1\r\n");
      out.print("Content-Length: " + strPostData.length() + "\r\n");
      out.print("Content-Type: application/x-www-form-urlencoded\r\n");
      out.print("Authorization: Basic " + encodeBase64(strUser + ":" + strPwd) +"\r\n");
      out.print("\r\n");
      out.print(strPostData);
      out.flush();

      System.out.println("Connect time = " + (System.currentTimeMillis() - lStartTime));

      // Read the response (With timeout)
      BufferedReader in = new BufferedReader(
                        new InputStreamReader(
                        socket.getInputStream()));
      long lEndTime = System.currentTimeMillis() + iRequestTimeout;
      int iTimeLeft = iRequestTimeout;
      String strResponse = null;
      try
      {
        // Read response headers
        String strHdrLine;
        int iLength = 0;
        socket.setSoTimeout(iTimeLeft);
        while ((strHdrLine = in.readLine()) != null)
        {
          // Determine timeout for next read from server...
          iTimeLeft = (int)(lEndTime - System.currentTimeMillis());
          if (iTimeLeft < 0)
            throw new Exception("Request timed out");
          socket.setSoTimeout(iTimeLeft);
          // Process the line that was read from the server
          if (strHdrLine.startsWith("Content-Length:"))
          {
            // Get the response length from the header field
            // Content-Length: <number>
            iLength = Integer.parseInt(strHdrLine.substring(15, strHdrLine.length()).trim());
          }
          // If carriage return line feed.. 
          // We are done with the headers.
          else if (strHdrLine.equals(""))
          {
            if (iLength > 0)
            {
              // Now read the response string
              char[] achar = new char[iLength];
              in.read(achar);
              strResponse = new String(achar);
              // and break from the loop
            }
            break;
          }
        }
        if (strResponse == null)
          throw new Exception("No response from host");
        else if (!strResponse.startsWith("d39="))
          throw new Exception("Invalid response from host");
        else
          System.out.println("Response from host [" + strResponse + "]");
      }
      catch (java.net.SocketTimeoutException e)
      {
        throw e;
      }
      finally
      {
        try {in.close();} catch (Exception ignore) {}
        try {out.close();} catch (Exception ignore) {}
        try {socket.close();} catch (Exception ignore) {}
        System.out.println("Total Processing Time = " + (System.currentTimeMillis() - lStartTime));
      }
    } 
    catch (SSLHandshakeException e) 
    {
      e.printStackTrace();
    }
    catch (java.net.UnknownHostException e)
    {
      e.printStackTrace();
    }
    catch (java.net.ConnectException e)
    {
      e.printStackTrace();
    }
    catch (java.net.NoRouteToHostException e)
    {
      e.printStackTrace();
    }
    catch (javax.net.ssl.SSLException e)
    {
      e.printStackTrace();
    }
    catch (java.net.SocketException e)
    {
      e.printStackTrace();
    }
    catch (java.io.IOException e)
    {
      e.printStackTrace();
    }
    catch (Exception e) 
    {
      e.printStackTrace();
    }
  }

  private static String encodeBase64(String _strData)
  {
    Base64 oB64 = new Base64();
    return oB64.encode(_strData.getBytes());
  }
}
