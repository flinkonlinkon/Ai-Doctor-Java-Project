package com.dermavisionai.service;

import com.dermavisionai.model.Report;
import com.dermavisionai.model.User;
import java.awt.Color;
import java.io.File;
import java.io.IOException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

public class PdfReportService {
    private static final PDType1Font BOLD = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
    private static final PDType1Font REGULAR = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

    public void export(User user, Report report, File destination) throws IOException {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.LETTER);
            document.addPage(page);
            try (PDPageContentStream content = new PDPageContentStream(document, page)) {
                float y = 740;
                content.setNonStrokingColor(new Color(48, 82, 140));
                content.addRect(0, 700, 612, 92);
                content.fill();
                write(content, BOLD, 24, 48, y, "DermaVision AI Skin Report", Color.WHITE);
                y -= 54;
                write(content, REGULAR, 11, 48, y, "Educational report. Not a medical diagnosis.", Color.WHITE);

                y = 660;
                write(content, BOLD, 14, 48, y, "User Information", Color.BLACK);
                y -= 22;
                write(content, REGULAR, 11, 48, y, "Name: " + user.getFullName(), Color.DARK_GRAY);
                y -= 16;
                write(content, REGULAR, 11, 48, y, "Email: " + user.getEmail(), Color.DARK_GRAY);
                y -= 16;
                write(content, REGULAR, 11, 48, y, "Date: " + report.getCreatedAt(), Color.DARK_GRAY);

                File imageFile = new File(report.getImagePath());
                if (imageFile.exists()) {
                    PDImageXObject image = PDImageXObject.createFromFileByExtension(imageFile, document);
                    content.drawImage(image, 390, 510, 150, 150);
                }

                y -= 42;
                write(content, BOLD, 14, 48, y, "Analysis Summary", Color.BLACK);
                y = writeBlock(content, report.getAnalysisResult().toSummaryText(), 48, y - 22);
                write(content, BOLD, 14, 48, y - 10, "Recommendations", Color.BLACK);
                writeBlock(content, report.getRecommendation().toReadableText(), 48, y - 34);
            }
            document.save(destination);
        }
    }

    private void write(PDPageContentStream content, PDType1Font font, int size, float x, float y,
                       String text, Color color) throws IOException {
        content.beginText();
        content.setFont(font, size);
        content.setNonStrokingColor(color);
        content.newLineAtOffset(x, y);
        content.showText(sanitize(text));
        content.endText();
    }

    private float writeBlock(PDPageContentStream content, String text, float x, float startY) throws IOException {
        float y = startY;
        for (String line : text.split("\\R")) {
            if (y < 54) {
                break;
            }
            write(content, REGULAR, 10, x, y, truncate(line, 95), Color.DARK_GRAY);
            y -= 14;
        }
        return y;
    }

    private String truncate(String value, int max) {
        return value.length() <= max ? value : value.substring(0, max - 3) + "...";
    }

    private String sanitize(String text) {
        return text == null ? "" : text.replaceAll("[^\\x20-\\x7E]", " ");
    }
}
