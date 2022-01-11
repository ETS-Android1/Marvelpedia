package com.project_future_2021.marvelpedia.network;

import android.content.Context;

import com.project_future_2021.marvelpedia.data.Hero;

import java.util.List;

public class HeroApi {

    public interface TalkWithServer {
        List<Hero> getHeroesFromServer(Context context, String url, String requestTag, int offset, int limit);

        List<Hero> getHeroesFromServerWithName(Context context, String url, String requestTag, String heroName);

    }

}
