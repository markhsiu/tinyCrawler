package com.main;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.common.spring.SpringContextLocator;
import com.lizi.crawler.controller.JdCommentController;

public class CrawlerStarter {

    private static Logger log = Logger.getLogger(CrawlerStarter.class);

    public static void main(String[] args) throws Throwable {
        log.info("开始任务！");
        final JdCommentController controller = SpringContextLocator.getJdCommentController();
        List<String> search = new ArrayList<>();
        search.add("文胸");
        controller.start(search);
    }

}
