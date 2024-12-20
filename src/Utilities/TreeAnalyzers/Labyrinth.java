package Utilities.TreeAnalyzers;
import Graph.*;
import Tests.GraphGenerators.Grid;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


public class Labyrinth extends JPanel {
	
	private static final long serialVersionUID = 2192694920147985L;
	private int halfSide = 5;
	private int vertexMargin = 1;
	private int corridorMargin = 2;
	private final int corridorLength = 2 * getHalfSide();
	private int side;
	private int vertexRadius;
	private int vertexWidth;
	private int corridorWidth;
	private int corridorStartShift;
	private int colorGradientCycleLength = 150; // > 0
	private int brightnessSlope= 3; // 0 <= x <= 100
	private int minBrightness = 40; // 0 <= x <= 100
	private Color backgroundColor = Color.black;

	private final Grid grid;
	private final RootedTree tree;
	private final ArrayList<NonDirectedEdge> nonDirectedEdges;

	private final BufferedImage image;

	public Labyrinth(Grid grid, RootedTree tree) {
		this.grid = grid;
		this.tree = tree;
		this.nonDirectedEdges = new ArrayList<>();
		recomputeDefaultValues();
		setPreferredSize(new Dimension(getSide() * grid.width(), getSide() *grid.height()));
		this.image = new BufferedImage(
				getSide() * grid.width(),
				getSide() * grid.height(),
				BufferedImage.TYPE_3BYTE_BGR
		);
	}

	/**
	 * Recomputes the default values for various properties based on the current side and margins.
	 * These include the side length, vertex width, corridor width, corridor start shift, and vertex radius.
	 */
	private void recomputeDefaultValues() {
		setSide(2 * getHalfSide() + 1);
		setVertexWidth(getSide() - 2 * getVertexMargin());
		setCorridorWidth(getSide() - 2 * getCorridorMargin());
		setCorridorStartShift(getSide() / 2 + 1);
		setVertexRadius(getHalfSide() - getVertexMargin());
	}

	@Override
	public void paintComponent(Graphics graphics) {
		Graphics2D g = (Graphics2D) graphics;
		g.drawImage(getImage(),0,0,null);
	}

	/**
	 * Adds an undirected edge to the collection of edges.
	 *
	 * @param e The undirected edge to be added.
	 */
	public void addEdge(NonDirectedEdge e) {
		getNonDirectedEdges().add(e);
	}

	/**
	 * Saves the current image to the specified file path in PNG format.
	 *
	 * @param path The file path where the image will be saved.
	 * @throws IOException If an error occurs during image writing.
	 */
	public void saveImage(String path) throws IOException {
		ImageIO.write(getImage(),"PNG", new File(path));
	}

	/**
	 * Draws the labyrinth on the current image.
	 * The method draws the background, edges, vertices, and the tree (if it exists),
	 * using antialiasing for smoother rendering.
	 */
	public void drawLabyrinth() {
		Graphics2D g = getImage().createGraphics();
		RenderingHints rh = new RenderingHints(
				RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON
		);
		g.setRenderingHints(rh);
		drawBackground(g);

		g.setColor(Color.white);
		for (NonDirectedEdge e : getNonDirectedEdges()) {
			if (getGrid().isHorizontal(e)) {
				drawHorizontalEdge(g, e);
			}
			else {
				drawVerticalEdge(g, e);
			}
		}
		for (int i = 0; i < getGrid().getGraph().order(); i++) {
			drawVertex(g, i);
		}
		if (getTree() != null) drawRoot(g, getTree().getRoot());

		g.dispose();
		repaint();
	}

	/**
	 * Draws the background of the labyrinth or grid, setting the background color
	 * and triggering a component repaint.
	 *
	 * @param g The graphics context used for drawing.
	 */
	private void drawBackground(Graphics2D g) {
		super.setBackground(getBackgroundColor());
		super.paintComponent(g);
	}

	/**
	 * Draws a vertex at a specified position on the grid using a circular shape.
	 * The vertex color is determined by the vertex's properties.
	 *
	 * @param g The graphics context used for drawing.
	 * @param vertex The index of the vertex to be drawn.
	 */
	private void drawVertex(Graphics2D g, int vertex) {
		int xMin = getSide() * getGrid().abscissaOfVertex(vertex) + getVertexMargin();
		int yMin = getSide() * getGrid().ordinateOfVertex(vertex) + getVertexMargin();
		Shape ell = new Ellipse2D.Float(xMin, yMin, getVertexWidth(), getVertexWidth());
		g.setPaint(getVertexColor(vertex));
		g.fill(ell);
	}

	/**
	 * Draws the root of the tree (if present) as a white square at the specified grid position.
	 *
	 * @param g The graphics context used for drawing.
	 * @param vertex The index of the root vertex to be drawn.
	 */
	private void drawRoot(Graphics2D g, int vertex) {
		int i = getGrid().abscissaOfVertex(vertex);
		int j = getGrid().ordinateOfVertex(vertex);
		g.setColor(Color.white);
		g.fillRect(getSide() * i, getSide() * j, getSide(), getSide());
	}

	/**
	 * Draws a horizontal edge between two vertices using a gradient color from the source to the destination.
	 * The edge is drawn as a rectangle with the appropriate size and gradient paint.
	 *
	 * @param context The graphics context used for drawing.
	 * @param nonDirectedEdge The undirected edge to be drawn.
	 */
	private void drawHorizontalEdge(Graphics2D context, NonDirectedEdge nonDirectedEdge) {
		int source = Math.min(nonDirectedEdge.getSource(), nonDirectedEdge.getDestination());
		int destination = Math.max(nonDirectedEdge.getSource(), nonDirectedEdge.getDestination());
		int xMin = getSide() * getGrid().abscissaOfVertex(source) + getCorridorStartShift();
		int yMin = getSide() * getGrid().ordinateOfVertex(source) + getCorridorMargin();
		Rectangle rect = new Rectangle(xMin, yMin, 2 * getHalfSide(), getCorridorWidth());
		GradientPaint gradient = new GradientPaint(
				xMin + getVertexRadius() - 1, yMin, getVertexColor(source),
				xMin + 2 * getHalfSide() - getVertexRadius(), yMin, getVertexColor(destination)
		);
		context.setPaint(gradient);
		context.fill(rect);
	}

	/**
	 * Draws a vertical edge between two vertices using a gradient color from the source to the destination.
	 * The edge is drawn as a rectangle with the appropriate size and gradient paint.
	 *
	 * @param context The graphics context used for drawing.
	 * @param nonDirectedEdge The undirected edge to be drawn.
	 */
	private void drawVerticalEdge(Graphics2D context, NonDirectedEdge nonDirectedEdge) {
		int source = Math.min(nonDirectedEdge.getSource(), nonDirectedEdge.getDestination());
		int destination = Math.max(nonDirectedEdge.getSource(), nonDirectedEdge.getDestination());
		int xMin = getSide() * getGrid().abscissaOfVertex(source) + getCorridorMargin();
		int yMin = getSide() * getGrid().ordinateOfVertex(source) + getCorridorStartShift();
		Rectangle rect = new Rectangle(xMin, yMin, getCorridorWidth(), 2 * getHalfSide());
		GradientPaint gradient = new GradientPaint(
				xMin, yMin + getVertexRadius() - 1, getVertexColor(source),
				xMin, yMin + 2 * getHalfSide() - getVertexRadius(), getVertexColor(destination)
		);
		context.setPaint(gradient);
		context.fill(rect);
	}

	/**
	 * Calculates the color of a vertex based on its depth and height in the tree.
	 * The color is determined using HSB (Hue, Saturation, Brightness) values,
	 * where hue is based on the vertex depth, and brightness is based on the vertex height.
	 *
	 * @param vertex The vertex for which the color is to be calculated.
	 * @return The calculated color for the vertex.
	 */
	private Color getVertexColor(int vertex) {
		if (getTree() == null) return Color.white;
		int depth = getTree().getDepth(vertex);
		int height = getTree().getHeight(vertex) + 1;
		float hue = (float)
				(depth % getColorGradientCycleLength()) / getColorGradientCycleLength();
		float saturation = (float) 0.7;
		float brightness = (float)
				Math.min(100, getBrightnessSlope() * height + getMinBrightness()) / 100;
		return Color.getHSBColor(hue, saturation, brightness);

	}

	/**
	 * Sets the shape of the nodes to be large with specific margins.
	 * Recomputes the default values after setting the shape.
	 */
	public void setShapeBigNodes() {
		setHalfSide(10);
		setVertexMargin(1);
		setCorridorMargin(5);
		recomputeDefaultValues();
	}

	/**
	 * Sets the shape of the nodes to be smooth and small with specific margins.
	 * Recomputes the default values after setting the shape.
	 */
	public void setShapeSmoothSmallNodes() {
		setHalfSide(5);
		setVertexMargin(1);
		setCorridorMargin(1);
		recomputeDefaultValues();
	}

	/**
	 * Sets the shape of the nodes to be small and fully compact with no margins.
	 * Recomputes the default values after setting the shape.
	 */
	public void setShapeSmallAndFull() {
		setHalfSide(5);
		setVertexMargin(0);
		setCorridorMargin(0);
		recomputeDefaultValues();
	}

	/**
	 * Sets the style to bright with a specific color gradient and background color.
	 */
	public void setStyleBright() {
		setColorGradientCycleLength(150);
		setBrightnessSlope(0);
		setMinBrightness(100);
		setBackgroundColor(Color.black);
	}

	/**
	 * Sets the style to inverted with a specific color gradient and background color.
	 */
	public void setStyleInverted() {
		setColorGradientCycleLength(150);
		setBrightnessSlope(2);
		setMinBrightness(10);
		setBackgroundColor(Color.gray);
	}

	/**
	 * Sets the style to balanced with a specific color gradient and background color.
	 */
	public void setStyleBalanced() {
		setColorGradientCycleLength(150);
		setBrightnessSlope(3);
		setMinBrightness(40);
		setBackgroundColor(Color.black);
	}

	private void setCorridorMargin(int value) {
		this.corridorMargin = value;
	}

	private void setVertexMargin(int value) {
		this.vertexMargin = value;
	}

	private void setHalfSide(int value) {
		this.halfSide = value;
	}

	private void setColorGradientCycleLength(int value) {
		this.colorGradientCycleLength = value;
	}

	private void setBrightnessSlope(int value) {
		this.brightnessSlope = value;
	}

	private void setMinBrightness(int value) {
		this.minBrightness = value;
	}

	private void setBackgroundColor(Color value) {
		this.backgroundColor = value;
	}

	private void setVertexWidth(int value) {
		this.vertexWidth = value;
	}

	private void setCorridorWidth(int value) {
		this.corridorWidth = value;
	}

	private void setCorridorStartShift(int value) {
		this.corridorStartShift = value;
	}

	private void setVertexRadius(int value) {
		this.vertexRadius = value;
	}

	private void setSide(int value) {
		this.side = value;
	}

	private int getCorridorLength() {
		return corridorLength;
	}

	private int getSide() {
		return side;
	}

	private int getHalfSide() {
		return halfSide;
	}

	private int getVertexMargin() {
		return vertexMargin;
	}

	private int getCorridorMargin() {
		return corridorMargin;
	}

	private ArrayList<NonDirectedEdge> getNonDirectedEdges() {
		return nonDirectedEdges;
	}

	private RootedTree getTree() {
		return tree;
	}

	private int getCorridorWidth() {
		return corridorWidth;
	}

	private int getColorGradientCycleLength() {
		return colorGradientCycleLength;
	}

	private int getMinBrightness() {
		return minBrightness;
	}

	private int getBrightnessSlope() {
		return brightnessSlope;
	}

	private Grid getGrid() {
		return grid;
	}

	private BufferedImage getImage() {
		return image;
	}

	private int getVertexRadius() {
		return vertexRadius;
	}

	private int getCorridorStartShift() {
		return corridorStartShift;
	}

	private int getVertexWidth() {
		return vertexWidth;
	}

	private Color getBackgroundColor() {
		return backgroundColor;
	}
}
