package com.schools.newyork.testmodules;

import com.schools.newyork.modules.NetworkModule;
import com.schools.newyork.network.NewYorkServiceInterface;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.components.SingletonComponent;
import dagger.hilt.testing.TestInstallIn;
import okhttp3.OkHttpClient;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@TestInstallIn(
        components = SingletonComponent.class,
        replaces = NetworkModule.class
)
public class FakeNetworkModule {

    private static final int SERVER_TIMEOUT = 1000;
    private final MockWebServer mockWebServer = new MockWebServer();

    @Singleton
    @Provides
    public NewYorkServiceInterface getNewYorkServiceInterface(Retrofit retrofit) {
        return retrofit.create(NewYorkServiceInterface.class);
    }

    @Singleton
    @Provides
    public Retrofit getNewYorkRetrofitInstance() {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(SERVER_TIMEOUT, TimeUnit.MILLISECONDS)
                .readTimeout(SERVER_TIMEOUT, TimeUnit.MILLISECONDS)
                .writeTimeout(SERVER_TIMEOUT, TimeUnit.MILLISECONDS)
                .build();

        return new Retrofit.Builder()
                .baseUrl(mockWebServer.url("/test/"))
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    public MockWebServer getMockWebServer() {
        return mockWebServer;
    }
}
