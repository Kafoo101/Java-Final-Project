package myJava.misc;
import java.awt.image.BufferedImage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;
import javax.imageio.ImageIO;
import java.net.URL;

import jakarta.mail.*;
import jakarta.mail.internet.*;
import jakarta.activation.*;

import java.io.*;
import java.util.Properties;

public class JavaMailer {
    private static final String SENDER_EMAIL = "portalnews096@gmail.com";
    private static final String SENDER_PASSWORD = "brvylmivbywtybnf";

    // Helper context to hold mutable contentStream and current y coordinate
    private static class PDFWriteContext {
        PDPageContentStream contentStream;
        float y;
        PDPage page;

        PDFWriteContext(PDPageContentStream cs, float y, PDPage page) {
            this.contentStream = cs;
            this.y = y;
            this.page = page;
        }
    }

    public static File generatePDFfromURL(String url) throws IOException {
        Document doc = Jsoup.connect(url).get();

        // Extract main article content - tweak selector as needed
        Element article = doc.selectFirst("article");
        if (article == null) {
            article = doc.selectFirst("div.l-container");
        }
        if (article == null) {
            throw new IOException("Cannot find main article content");
        }
        article.select("div[data-component-name=video-resource], div.video-resource").remove();

        File pdfFile = File.createTempFile("news-", ".pdf");

        final PDRectangle PAGE_SIZE = PDRectangle.LETTER;
        final float MARGIN = 50;
        final float WIDTH = PAGE_SIZE.getWidth() - 2 * MARGIN;
        final float HEIGHT = PAGE_SIZE.getHeight() - 2 * MARGIN;
        final float FONT_SIZE = 12;
        final float LEADING = 1.5f * FONT_SIZE;

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PAGE_SIZE);
            document.addPage(page);

            PDPageContentStream contentStream = new PDPageContentStream(document, page);
            float startY = PAGE_SIZE.getHeight() - MARGIN;

            // Start writing text
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, FONT_SIZE);
            contentStream.setLeading(LEADING);
            contentStream.newLineAtOffset(MARGIN, startY);

            PDFWriteContext ctx = new PDFWriteContext(contentStream, startY, page);

            // Recursively render the article content
            writeElement(document, ctx, article, FONT_SIZE, LEADING, MARGIN, WIDTH, HEIGHT);

            ctx.contentStream.endText();
            ctx.contentStream.close();

            document.save(pdfFile);
        }
        return pdfFile;
    }

    // Recursive method to write Jsoup Element content to PDF with styles and page breaks
    private static void writeElement(PDDocument document, PDFWriteContext ctx,
                                     Node node, float fontSize, float leading,
                                     float margin, float width, float height) throws IOException {

        // Base fonts
        PDFont fontRegular = PDType1Font.HELVETICA;
        PDFont fontBold = PDType1Font.HELVETICA_BOLD;
        PDFont fontItalic = PDType1Font.HELVETICA_OBLIQUE;

        if (node instanceof TextNode) {
            String text = ((TextNode) node).text().replaceAll("\\s+", " ");
            if (text.trim().isEmpty()) return;

            // Split text into words
            String[] words = text.split(" ");
            StringBuilder line = new StringBuilder();
            PDFont currentFont = fontRegular;

            // We'll ignore styles on text nodes here, styles come from parents

            for (String word : words) {
                String append = (line.length() == 0 ? "" : " ") + word;
                float lineWidth = currentFont.getStringWidth(line.toString() + append) / 1000 * fontSize;
                if (lineWidth > width) {
                    // write current line
                    ctx.contentStream.showText(line.toString());
                    ctx.contentStream.newLine();
                    ctx.y -= leading;

                    if (ctx.y <= margin) {
                        // close current content stream and start new page
                        ctx.contentStream.endText();
                        ctx.contentStream.close();

                        ctx.page = new PDPage(PDRectangle.LETTER);
                        document.addPage(ctx.page);
                        ctx.contentStream = new PDPageContentStream(document, ctx.page);
                        ctx.contentStream.beginText();
                        ctx.contentStream.setFont(currentFont, fontSize);
                        ctx.contentStream.setLeading(leading);
                        ctx.contentStream.newLineAtOffset(margin, PDRectangle.LETTER.getHeight() - margin);
                        ctx.y = PDRectangle.LETTER.getHeight() - margin;
                    }
                    line = new StringBuilder(word);
                } else {
                    line.append(append);
                }
            }
            if (line.length() > 0) {
                ctx.contentStream.showText(line.toString());
                ctx.contentStream.newLine();
                ctx.y -= leading;
            }
        } else if (node instanceof Element) {
            Element el = (Element) node;

            // Handle images
            if (el.tagName().equalsIgnoreCase("img")) {
                String src = el.absUrl("src");
                if (!src.isEmpty()) {
                    try {
                        BufferedImage image = ImageIO.read(new URL(src));
                        if (image != null) {
                            float maxImgWidth = width;
                            float maxImgHeight = height / 2;

                            int imgWidth = image.getWidth();
                            int imgHeight = image.getHeight();

                            // Calculate scaling factor to fit within max width and height
                            float widthScale = maxImgWidth / imgWidth;
                            float heightScale = maxImgHeight / imgHeight;
                            float scale = Math.min(widthScale, heightScale);

                            float drawWidth = imgWidth * scale;
                            float drawHeight = imgHeight * scale;

                            // Check space on current page, else create new page
                            if (ctx.y - drawHeight <= margin) {
                                ctx.contentStream.endText();
                                ctx.contentStream.close();

                                ctx.page = new PDPage(PDRectangle.LETTER);
                                document.addPage(ctx.page);
                                ctx.contentStream = new PDPageContentStream(document, ctx.page);
                                ctx.contentStream.beginText();
                                ctx.contentStream.setFont(PDType1Font.HELVETICA, fontSize);
                                ctx.contentStream.setLeading(leading);
                                ctx.contentStream.newLineAtOffset(margin, PDRectangle.LETTER.getHeight() - margin);
                                ctx.y = PDRectangle.LETTER.getHeight() - margin;
                            }

                            // Draw the image (convert to PDImageXObject)
                            PDImageXObject pdImage = LosslessFactory.createFromImage(document, image);
                            ctx.contentStream.endText();
                            ctx.contentStream.drawImage(pdImage, margin, ctx.y - drawHeight, drawWidth, drawHeight);
                            ctx.y -= (drawHeight + leading);
                            ctx.contentStream.beginText();
                            ctx.contentStream.setFont(PDType1Font.HELVETICA, fontSize);
                            ctx.contentStream.setLeading(leading);
                            ctx.contentStream.newLineAtOffset(margin, ctx.y);
                        }
                    } catch (Exception ex) {
                        System.err.println("Failed to load image: " + src);
                        // Proceed without image
                    }
                }
            } else {
                // Determine font for styles
                PDFont fontToUse = fontRegular;
                if (el.tagName().equalsIgnoreCase("b") || el.tagName().equalsIgnoreCase("strong")) {
                    fontToUse = fontBold;
                } else if (el.tagName().equalsIgnoreCase("i") || el.tagName().equalsIgnoreCase("em")) {
                    fontToUse = fontItalic;
                }

                ctx.contentStream.setFont(fontToUse, fontSize);

                for (Node child : el.childNodes()) {
                    writeElement(document, ctx, child, fontSize, leading, margin, width, height);
                }

                // Add space after paragraph
                if (el.tagName().equalsIgnoreCase("p")) {
                    ctx.contentStream.newLine();
                    ctx.y -= leading;
                }
            }
        }
    }

    public static void sendEmailWithPDF(String recipient, File pdfFile) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(SENDER_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
        message.setSubject("News PDF");

        // Email body text
        MimeBodyPart messageBodyPart = new MimeBodyPart();
        messageBodyPart.setText("Please find the news PDF attached.");

        // Attachment part
        MimeBodyPart attachmentPart = new MimeBodyPart();
        DataSource source = new FileDataSource(pdfFile);
        attachmentPart.setDataHandler(new DataHandler(source));
        attachmentPart.setFileName(pdfFile.getName());

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(messageBodyPart);
        multipart.addBodyPart(attachmentPart);

        message.setContent(multipart);

        Transport.send(message);
    }

    // Example usage:
    public static void main(String[] args) {
        String url = "https://edition.cnn.com/2025/06/09/politics/trump-national-guard-los-angeles-hegseth";
        String recipientEmail = "kafoo0aa@gmail.com";

        try {
            File pdf = generatePDFfromURL(url);
            sendEmailWithPDF(recipientEmail, pdf);
            System.out.println("Email sent successfully!");
            pdf.delete(); // clean up temp file
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}