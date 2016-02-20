package it.unibo.oop.model;

import static it.unibo.oop.utilities.CharactersSettings.BASIC_ENEMY;

import it.unibo.oop.exceptions.CollisionHandlingException;
import it.unibo.oop.utilities.Position;
import it.unibo.oop.utilities.Vector2;
import it.unibo.oop.utilities.Velocity;;

public class BasicMonster extends AbstractEnemy{

	private static final int SCORE_VALUE = 10;
	private final static int DMG = 1;
	
	public BasicMonster(final double startingX, final double startingY, final Vector2 movementVector, final Velocity speedValue, final MovementBehavior movBeh) {
		super(startingX, startingY, movementVector, speedValue, movBeh);
	}
	
	public BasicMonster(final double startingX, final double startingY, final Vector2 movementVector, final Velocity speedValue) {
		super(startingX, startingY, movementVector, speedValue);
		this.attachBehavior(new BasicEnemyBehavior(this));
	}
	
	public void update(){
		
		final Vector2 newMovement = this.getBehavior().get().getNextMove(this.getEnvironment().getMainChar().get().getPosition());
		//newMovement = newMovement.clamp(this.getVelocity().getMinVelocity(), this.getVelocity().getMaxVelocity());
		try {
			this.checkCollision(this.getPosition().sumVector(newMovement));
			this.setMovement(newMovement);
			this.move();
		} catch (CollisionHandlingException e) {
		}
		
	}
	public void checkCollision(final Position newPosition) throws CollisionHandlingException{
		final BasicMonster tmpEnemy = new BasicMonster(newPosition.getIntX(), newPosition.getIntY(), this.getMovement(), this.getVelocity());
		//Checks if the entity in the next move is inside the rectanuglar Arena
		if (!this.getEnvironment().getArena().isInside(tmpEnemy)){
			throw new CollisionHandlingException();
		}
		final long numWallCollisions = this.getEnvironment().getStableList().stream()
				  													  .filter(x -> x instanceof Wall)
				  													  .filter(tmpEnemy::intersecate)
				  													  .count();
		
//		List<Enemy> enemyCollisions = this.getEnvironment().getMovableList().stream()
//																			.filter(x -> x instanceof Enemy)
//																			.filter(tmpEnemy::intersecate)
//																			.map(x -> (Enemy)x)
//																			.collect(Collectors.toList());
		
		if (numWallCollisions > 0){
			throw new CollisionHandlingException();
		}	
		//TODO
		
	}
	protected int getEntityHeight() {
		return BASIC_ENEMY.getHeight();
	}

	protected int getEntityWidth() {
		return BASIC_ENEMY.getWidth();
	}

	@Override
	public int getScoreValue() {
		return BasicMonster.SCORE_VALUE;
	}
	@Override
	public int getDamage() {
		return BasicMonster.DMG;
	}	

}
