package searchengine.services.Impl;

import searchengine.model.Site;

import java.sql.Timestamp;
import java.time.LocalDateTime;

class ObjectConverter {
    public static Site convertSiteConfigIntoSiteModel(searchengine.config.Site siteConfig){
        Site site=new Site();
        site.setUrl(siteConfig.getUrl());
        site.setName(siteConfig.getName());
        site.setStatusTime(Timestamp.valueOf(LocalDateTime.now()));
        return site;
    }
}
