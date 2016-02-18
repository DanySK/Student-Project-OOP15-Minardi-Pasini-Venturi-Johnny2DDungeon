package it.unibo.oop.view;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import it.unibo.oop.utilities.Direction;

import static it.unibo.oop.utilities.Direction.*;

/**
 * A class representing a sprite sheet.
 */
public class SpriteSheet {
	
	private BufferedImage sheet;
	private Map<Direction, BufferedImage> sprites;
	private final ImageLoader loader;
	private boolean isSplitted = false;
	
	/**
	 * Constructs a new sprite sheet.
	 * @param sheetName
	 * 		the name of the sprite sheet
	 */
	public SpriteSheet(String sheetName) {
		this.loader = new ImageLoader();
		try {
			this.sheet = this.loader.load(sheetName);
		} catch (IOException e) {
			System.out.println("Sheet not found");
		}
		this.sprites = new HashMap<>();
	}
	
	private BufferedImage grabSprite(int x, int y, int width, int height) {
		return sheet.getSubimage(x, y, width, height);
	}
	
	/**
	 * Splits every sprite in the {@link it.unibo.oop.view.SpriteSheet}.
	 * It considers only the first sprite for every row in the sheet
	 * (only one animation).
	 * @param imagesWidth
	 * 		the width of each sprite in the {@link it.unibo.oop.view.SpriteSheet}
	 * @param imagesHeight
	 * 		the height of each sprite in the {@link it.unibo.oop.view.SpriteSheet}
	 * @return
	 * 		a {@link java.util.Map} of {@link java.awt.image.BufferedImage} with
	 * 		all the sprites in the {@link it.unibo.oop.view.SpriteSheet}
	 */
	public Map<Direction, BufferedImage> split(int imagesWidth, int imagesHeight) {
		isSplitted = ((sheet.getHeight() % imagesHeight == 0) &&
				(sheet.getWidth() % imagesWidth == 0)) ? true : false;
		if ((sheet != null) && isSplitted) {
			for (int y = 0; y < sheet.getHeight(); y += imagesHeight) {
				for(int x = 0; x < imagesWidth; x += imagesWidth) {
					sprites.put(grabSprite(x, y, imagesWidth, imagesHeight));
				}
			}
		}
		return sprites;
	}
	
	/**
	 * Returns the sprite in the {@link java.util.map} corresponding to the given
	 * {@link it.unibo.oop.utilities.Direction}.
	 * @param key
	 * 		the direction of the sprite
	 * @return
	 * 		the corresponding sprite
	 */
	public BufferedImage getSprite(Direction dir) {
		return sprites.get(dir);
	}
}