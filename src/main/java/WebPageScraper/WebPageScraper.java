package WebPageScraper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class WebPageScraper {
    private static final String BASE_URL = "https://www.nature.com/nature/articles";
    private static Map<String, String> articlesTypes;

    public static void main(String[] args) {
        try {
            start();
        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static void start() throws IOException {
        int pagesNumber = getNumberOfPages();

        articlesTypes = getArticlesTypes();
        String userArticle = getUserChoice(articlesTypes);

        for (int pageNumber = 1; pageNumber <= pagesNumber; pageNumber++) {
            saveArticles(userArticle, pageNumber);
        }
    }

    private static int getNumberOfPages() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("How many pages do you want to save: > ");
        while (true) {
            try {
                int pages = Integer.parseInt(scanner.nextLine());
                if (pages > 0) {
                    return pages;
                } else {
                    System.out.println("Number must be greater than 0");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number");
            }
        }
    }

    private static Map<String, String> getArticlesTypes() throws IOException {
        Document response = Jsoup.connect(BASE_URL + "?year=2024").get();

        Map<String, String> typesMap = new HashMap<>();
        Element nav = response.selectFirst("nav#Article-Type-target");
        if (nav != null) {
            Elements types = nav.select("a");
            for (Element type : types) {
                String articleType = getArticleType(type.text());
                String articleUrl = type.attr("href");
                typesMap.put(articleType, articleUrl);
            }
        }
        return typesMap;
    }

    private static String getArticleType(String text) {
        String[] parts = text.split(" ");
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < parts.length - 1; i++) {
            if (i > 0) result.append(" ");
            result.append(parts[i]);
        }
        return result.toString();
    }

    private static String getUserChoice(Map<String, String> types) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("What type of articles do you want to save:");
        types.keySet().forEach(System.out::println);

        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();
            if (types.containsKey(input)) {
                return input;
            }
            System.out.println("Invalid type. Please choose from the list above.");
        }
    }

    private static void saveArticles(String articleName, int pageNumber) throws IOException {
        String pageUrl = BASE_URL + articlesTypes.get(articleName) + "&page=" + pageNumber;
        Document document = Jsoup.connect(pageUrl).get();
        String sep = File.separator;

        String pageDir = "Articles" + sep + articleName + sep + "Page_" + pageNumber;
        File pageDirFile = new File(pageDir);

        if (pageDirFile.exists()) {
            System.out.println("You already have page №" + pageNumber);
            return;
        }

        System.out.println("Page " + pageNumber + ":");

        Map<String, String> articles = getArticles(document);
        if (articles.isEmpty()) {
            System.out.println("All articles are unavailable on page №" + pageNumber);
            return;
        }

        if (!pageDirFile.mkdirs()) {
            throw new IOException("Failed to create directory: " + pageDir);
        }

        for (Map.Entry<String, String> entry : articles.entrySet()) {
            String filePath = pageDir + sep + entry.getKey();
            Files.writeString(Paths.get(filePath), entry.getValue());
        }
    }

    private static Map<String, String> getArticles(Document document) throws IOException {
        Map<String, String> articles = new HashMap<>();
        Elements articleLinks = document.select("a[data-track-action='view article']");

        for (Element article : articleLinks) {
            String articleName = getArticleFileName(article.text());
            String articleUrl = getArticleUrl(article);
            String articleContent = getArticleContent(articleUrl);

            if (articleContent != null) {
                articles.put(articleName, articleContent);
                System.out.println("\"" + article.text() + "\"\nSaved\n");
            } else {
                System.out.println("\"" + article.text() + "\"\nThe content is unavailable\n");
            }
        }

        return articles;
    }

    private static String getArticleFileName(String name) {
        return name.replaceAll("[\\p{Punct}\\s—’‘–]", "_") + ".txt";
    }

    private static String getArticleUrl(Element article) {
        String mainUrl = "https://www.nature.com";
        return mainUrl + article.attr("href");
    }

    private static String getArticleContent(String url) throws IOException {
        Document document = Jsoup.connect(url).get();
        Element body = document.selectFirst("div.c-article-body.main-content");

        if (body == null) {
            body = document.selectFirst("div.c-article-body");
        }

        if (body != null) {
            StringBuilder content = new StringBuilder();
            Elements paragraphs = body.select("p");
            for (Element paragraph : paragraphs) {
                content.append(paragraph.text()).append("\n");
            }
            return content.toString().strip();
        }

        return null;
    }
}
