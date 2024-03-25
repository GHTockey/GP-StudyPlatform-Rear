package cn.tockey.config;
 
import java.nio.charset.Charset;
import java.util.List;
 
import javax.net.ssl.SSLContext;
 
//这里引用client5的包
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
 
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
 
@Configuration
public class RestTemplateConfig{
	@Bean("restTemplate")
	public RestTemplate RestTemplate() {
		HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
		httpRequestFactory.setConnectionRequestTimeout(30000);
		httpRequestFactory.setConnectTimeout(30000);
		//httpRequestFactory.setReadTimeout(30000);
		return new RestTemplate(httpRequestFactory);
	}
	
	/**
	 * 用于https请求，忽略认证
	 * @return	unSSLRestTemplate
	 */
	@Bean("unSSLRestTemplate")
	public RestTemplate restTemplateHttps()  {
        RestTemplate restTemplate = null;
        try {
            TrustStrategy acceptingTrustStrategy = (chain, authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);
 
            HttpClientBuilder clientBuilder = HttpClients.custom();
 
            //调整了这里
            CloseableHttpClient httpClient = clientBuilder.setConnectionManager(PoolingHttpClientConnectionManagerBuilder
                    .create().setSSLSocketFactory(sslsf).build()).build();
 
            HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
            httpRequestFactory.setConnectionRequestTimeout(30000);
            httpRequestFactory.setConnectTimeout(30000);
            //httpRequestFactory.setReadTimeout(30000);
    		
            httpRequestFactory.setHttpClient(httpClient);
 
            restTemplate = new RestTemplate(httpRequestFactory);
            //解决乱码
            List<HttpMessageConverter<?>> httpMessageConverters = restTemplate.getMessageConverters();
            httpMessageConverters.stream().forEach(httpMessageConverter ->{
            	if(httpMessageConverter instanceof StringHttpMessageConverter){
            		StringHttpMessageConverter messageConverter = (StringHttpMessageConverter)httpMessageConverter;
            		messageConverter.setDefaultCharset(Charset.forName("UTF-8"));
            	}
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return restTemplate;
    }
}