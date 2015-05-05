package com;

import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.StringReader;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Image;
import com.itextpdf.text.ImgTemplate;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfTemplate;


import org.apache.batik.bridge.BridgeContext;
import org.apache.batik.bridge.DocumentLoader;
import org.apache.batik.bridge.GVTBuilder;
import org.apache.batik.bridge.UserAgent;
import org.apache.batik.bridge.UserAgentAdapter;
import org.apache.batik.dom.svg.SAXSVGDocumentFactory;
import org.apache.batik.dom.svg.SVGDocumentFactory;
import org.apache.batik.gvt.GraphicsNode;
import org.w3c.dom.svg.SVGDocument;

public class PDFUtil {

	public static Image drawUnscaledSvg(PdfContentByte contentByte,String svg)
            throws IOException {

        // First, lets create a graphics node for the SVG image.
        GraphicsNode imageGraphics = buildBatikGraphicsNode(svg);

        // SVG's width and height
        float width = (float) imageGraphics.getBounds().getWidth();
        float height = (float) imageGraphics.getBounds().getHeight();

        // Create a PDF template for the SVG image
        PdfTemplate template = contentByte.createTemplate(width, height);
        // Create Graphics2D rendered object from the template
        Graphics2D graphics = template.createGraphics(width, height);
        try {
            // SVGs can have their corner at coordinates other than (0,0).
            Rectangle2D bounds = imageGraphics.getBounds();
            graphics.translate(-bounds.getX(), -bounds.getY());

            // Paint SVG GraphicsNode with the 2d-renderer.
            imageGraphics.paint(graphics);

            // Create and return a iText Image element that contains the SVG
            // image.
            return new ImgTemplate(template);
        } catch (BadElementException e) {
            throw new RuntimeException("Couldn't generate PDF from SVG", e);
        } finally {
            // Manual cleaning (optional)
            graphics.dispose();
        }
    }
	
	/**
     * Use Batik SVG Toolkit to create GraphicsNode for the target SVG.
     * <ol>
     * <li>Create SVGDocument</li>
     * <li>Create BridgeContext</li>
     * <li>Build GVT tree. Results to GraphicsNode</li>
     * </ol>
     * 
     * @param svg
     *            SVG as a String
     * @return GraphicsNode
     * @throws IOException
     *             Thrown when SVG could not be read properly.
     */
    private static GraphicsNode buildBatikGraphicsNode(String svg) throws IOException {
        UserAgent agent = new UserAgentAdapter();

        SVGDocument svgdoc = createSVGDocument(svg, agent);

        DocumentLoader loader = new DocumentLoader(agent);
        BridgeContext bridgeContext = new BridgeContext(agent, loader);
        bridgeContext.setDynamicState(BridgeContext.STATIC);

        GVTBuilder builder = new GVTBuilder();

        GraphicsNode imageGraphics = builder.build(bridgeContext, svgdoc);

        return imageGraphics;
    }
    
    private static SVGDocument createSVGDocument(String svg, UserAgent agent)
            throws IOException {
        SVGDocumentFactory documentFactory = new SAXSVGDocumentFactory(
                agent.getXMLParserClassName(), true);

        SVGDocument svgdoc = documentFactory.createSVGDocument(null,
                new StringReader(svg));
        return svgdoc;
    }

    
    
	
}
