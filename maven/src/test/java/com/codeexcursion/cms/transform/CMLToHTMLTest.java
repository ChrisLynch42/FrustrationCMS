package com.codeexcursion.cms.transform;

import java.nio.file.Paths;
import java.io.IOException;
import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.nodes.Element;
import org.jsoup.Jsoup;;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Node;

public class CMLToHTMLTest {

    @Test
    public void jsoupSelectTest() {
        File htmlFile = Paths.get("src/test/artifacts/content/post/2018/getting-tiger-vnc-to-work-on-fedora-28.html")
                .toFile();

        try {
            Document html = Jsoup.parse(htmlFile, "UTF-8");

            Elements metaTags = html.select("meta");
            Assert.assertTrue("Did not find multiple meta tags.", metaTags.size() > 5);
            
            Elements terminals = html.select("terminal");
            Assert.assertTrue("Did not find multiple terminal tags.", terminals.size() > 6);

            Elements metaType = html.select("meta[name=type]");
            Assert.assertTrue("Did not find meta tag of type.", metaType.size() == 1);
            Assert.assertEquals("Did not find meta tag of type.", "post", metaType.get(0).attr("content"));


        } catch (IOException exception) {
            Assert.fail("Unable to find file " + htmlFile.getAbsolutePath());
        }
    }

    @Test
    public void jsoupChangeTest() {
        File htmlFile = Paths.get("src/test/artifacts/content/post/2018/getting-tiger-vnc-to-work-on-fedora-28.html")
                .toFile();

        try {
            Document html = Jsoup.parse(htmlFile, "UTF-8");

            Elements terminals = html.select("terminal");
            Assert.assertTrue("Did not find multiple terminal tags.", terminals.size() > 6);

            Elements captions = html.select("span[class=codeCaption]");
            Assert.assertTrue("Should not find any code captions.", captions.size() < 1);

            Elements preTerminals = html.select("pre[class=terminal]");
            Assert.assertTrue("Should not find any code captions.", preTerminals.size() < 1);

            Element terminal = terminals.get(0);
            String caption = terminal.attr("caption");
            Assert.assertEquals("Did not have correct caption.", "Stop Tiger VNC", caption );

            Element captionNode = new Element("span");
            captionNode.attr("class", "codeCaption");
            captionNode.appendChild(new TextNode(caption));
            terminal.before(captionNode);

            Element terminalNode = new Element("pre");
            terminalNode.attr("class", "terminal");
            terminalNode.appendChild(new TextNode(terminal.html()));
            terminal.replaceWith(terminalNode);

            captions = html.select("span[class=codeCaption]");
            Assert.assertTrue("Should not find any code captions.", captions.size() > 0);

            preTerminals = html.select("pre[class=terminal]");
            Assert.assertTrue("Should not find any code captions.", preTerminals.size() > 0);


        } catch (IOException exception) {
            Assert.fail("Unable to find file " + htmlFile.getAbsolutePath());
        }
    }

}